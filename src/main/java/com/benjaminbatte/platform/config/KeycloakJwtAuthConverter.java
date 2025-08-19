package com.benjaminbatte.platform.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Maps Keycloak roles in a JWT to Spring Security authorities:
 * - realm_access.roles          -> global roles
 * - resource_access[client].roles -> client-scoped roles (e.g. your API client)
 *
 * Each role becomes ROLE_<UPPERCASE>. Example: "admin" -> "ROLE_ADMIN"
 */
class KeycloakJwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final String resourceClientId;

    KeycloakJwtAuthConverter(String resourceClientId) {
        this.resourceClientId = resourceClientId;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Set<String> roles = new HashSet<>();

        // 1) Realm roles
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.get("roles") instanceof Collection<?> rr) {
            rr.forEach(r -> roles.add(String.valueOf(r)));
        }

        // 2) Client roles: resource_access[clientId].roles
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess instanceof Map<?, ?> ra) {
            Object clientObj = ra.get(resourceClientId);
            if (clientObj instanceof Map<?, ?> clientMap) {
                Object clientRoles = ((Map<?, ?>) clientMap).get("roles");
                if (clientRoles instanceof Collection<?> cr) {
                    cr.forEach(r -> roles.add(String.valueOf(r)));
                }
            }
        }

        // Normalize -> ROLE_*
        var authorities = roles.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.startsWith("ROLE_") ? s : "ROLE_" + s.toUpperCase(Locale.ROOT))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());

        // Principal name: preferred_username (fallback to sub)
        return new JwtAuthenticationToken(jwt, authorities, extractUsername(jwt));
    }

    private String extractUsername(Jwt jwt) {
        String preferred = jwt.getClaimAsString("preferred_username");
        return (preferred != null && !preferred.isBlank()) ? preferred : jwt.getSubject();
    }
}
