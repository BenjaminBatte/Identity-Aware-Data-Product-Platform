// src/test/java/.../SecurityContextTestUtils.java
package com.benjaminbatte.platform;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class SecurityContextTestUtils {
    private SecurityContextTestUtils() {}

    public static AbstractAuthenticationToken jwtAuth(String sub, Map<String,Object> extraClaims,
                                                      Collection<? extends GrantedAuthority> authorities) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("sub", sub);
        claims.put("preferred_username", "tester");
        claims.put("email", "tester@example.com");
        if (extraClaims != null) claims.putAll(extraClaims);

        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(3600),
                Map.of("alg", "none"), claims);
        return new JwtAuthenticationToken(jwt, authorities, sub);
    }

    public static void runWithAuth(AbstractAuthenticationToken auth, Runnable r) {
        var ctx = SecurityContextHolder.createEmptyContext();
        ctx.setAuthentication(auth);
        SecurityContextHolder.setContext(ctx);
        try { r.run(); } finally { SecurityContextHolder.clearContext(); }
    }
}
