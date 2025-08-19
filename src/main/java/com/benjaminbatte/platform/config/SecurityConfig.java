package com.benjaminbatte.platform.config;

import com.benjaminbatte.platform.common.exception.ErrorResponse;
import com.benjaminbatte.platform.common.logging.UserMdcFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Instant;
import java.util.List;

@Configuration
@EnableMethodSecurity // enables @PreAuthorize, @RolesAllowed, etc.
public class SecurityConfig {

    // ===== Injected settings from application.yml =====
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer; // Keycloak realm issuer URI (e.g., http://localhost:8080/realms/master)

    @Value("${app.security.jwt.audience}")
    private String requiredAudience; // Expected aud claim (e.g., platform-api)

    @Value("${app.security.jwt.client-id}")
    private String resourceClientId; // Keycloak client id whose roles we want to read

    @Value("${app.cors.allowed-origins}")
    private List<String> allowedOrigins; // Allowed web origins (Angular UI, etc.)

    // Provide the MDC filter as a bean (if you already use @Component on UserMdcFilter, you can remove this)
    @Bean
    UserMdcFilter userMdcFilter() {
        return new UserMdcFilter();
    }

    /**
     * Main Spring Security filter chain.
     * Makes the API stateless, validates JWTs, sets up CORS, and locks down endpoints by role.
     */
    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http,
            Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthConverter,
            JwtDecoder jwtDecoder,
            ObjectMapper mapper,            // used to write JSON bodies for 401/403
            UserMdcFilter userMdcFilter     // MDC filter that needs SecurityContext populated first
    ) throws Exception {

        http
                // --- Cross-Origin Resource Sharing ---
                .cors(Customizer.withDefaults())

                // --- CSRF disabled for token-based APIs (no cookies/forms) ---
                .csrf(AbstractHttpConfigurer::disable)

                // --- Stateless: never create/read HTTP sessions ---
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // keep a per-request SecurityContext, but DO NOT persist it or create HTTP session
                .securityContext(sc -> sc.securityContextRepository(new NullSecurityContextRepository()))

                // avoid saving requests (no redirect-to-login semantics in APIs)
                .requestCache(rc -> rc.requestCache(new NullRequestCache()))

                // --- Add secure headers (CSP + frame options for Swagger/i framed consoles) ---
                .headers(headers -> headers
                        .contentSecurityPolicy(csp ->
                                csp.policyDirectives("default-src 'self'"))
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                // --- Exception handling: API-style (401/403), no redirects ---
                // Return JSON bodies that match your ErrorResponse shape
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> {
                            res.setStatus(HttpStatus.UNAUTHORIZED.value());
                            res.setContentType("application/json");
                            var body = ErrorResponse.builder()
                                    .timestamp(Instant.now())
                                    .status(HttpStatus.UNAUTHORIZED.value())
                                    .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                                    .message("Authentication required")
                                    .code("UNAUTHORIZED")
                                    .path(req.getRequestURI())
                                    .build();
                            mapper.writeValue(res.getOutputStream(), body);
                        })
                        .accessDeniedHandler((req, res, e) -> {
                            res.setStatus(HttpStatus.FORBIDDEN.value());
                            res.setContentType("application/json");
                            var body = ErrorResponse.builder()
                                    .timestamp(Instant.now())
                                    .status(HttpStatus.FORBIDDEN.value())
                                    .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                                    .message("Access is denied")
                                    .code("ACCESS_DENIED")
                                    .path(req.getRequestURI())
                                    .build();
                            mapper.writeValue(res.getOutputStream(), body);
                        })
                )

                // --- Authorization rules ---
                .authorizeHttpRequests(auth -> auth
                        // Preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Open API docs (dev only; prod is disabled in your profile)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Actuator lives under /manage and on a separate port per your YAML
                        .requestMatchers("/manage/healthcheck").permitAll()       // liveness
                        .requestMatchers("/manage/**").hasRole("MONITORING")      // everything else under /manage requires MONITORING

                        // App-specific admin endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Everything else requires a valid JWT
                        .anyRequest().authenticated()
                )

                // --- Resource server with JWT ---
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder)                        // our custom decoder with validators
                                .jwtAuthenticationConverter(jwtAuthConverter) // maps Keycloak roles -> GrantedAuthorities
                        )
                );

        // 🔽 make MDC filter run AFTER SecurityContext is set up by Spring Security
        http.addFilterAfter(userMdcFilter, SecurityContextHolderFilter.class);

        return http.build();
    }

    /**
     * JWT Decoder that:
     * - Downloads the JWK set from the realm issuer
     * - Validates issuer, audience, and timestamps
     */
    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = JwtDecoders.fromIssuerLocation(issuer);

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new RequiredAudienceValidator(requiredAudience);
        OAuth2TokenValidator<Jwt> withTimestamp = new JwtTimestampValidator();

        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience, withTimestamp));
        return decoder;
    }

    /**
     * Converts a Keycloak JWT into Spring Security authorities.
     * Reads roles from:
     * - realm_access.roles
     * - resource_access[clientId].roles
     * Applies ROLE_ prefix and uppercases for Spring conventions.
     * Also sets principal name to JWT 'sub' so Authentication.getName() == externalId.
     */
    @Bean
    Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthConverter() {
        return new KeycloakJwtAuthConverter(resourceClientId);
    }

    /**
     * CORS configuration: which frontends can call us, and with which methods/headers.
     * Origins come from application.yml: app.cors.allowed-origins
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(allowedOrigins);
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of(
                "Authorization", "Content-Type", "Accept", "Origin",
                "User-Agent", "X-Requested-With", "X-XSRF-TOKEN"
        ));
        cfg.setExposedHeaders(List.of("X-XSRF-TOKEN"));
        cfg.setAllowCredentials(true); // OK because I'm using Authorization: Bearer (no cookies)
        cfg.setMaxAge(3600L); // cache preflight for 1h

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
