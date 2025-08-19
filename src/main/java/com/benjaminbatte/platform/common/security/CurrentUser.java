// src/main/java/com/benjaminbatte/platform/common/security/CurrentUser.java
package com.benjaminbatte.platform.common.security;

import com.benjaminbatte.platform.common.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

public final class CurrentUser {
    private CurrentUser() {}

    /** External ID (OIDC sub) set as Authentication.getName(). */
    public static String externalIdOrThrow() {
        Authentication auth = authOrThrow();
        String name = auth.getName();
        if (name == null || name.isBlank()) {
            throw new UnauthorizedException("Invalid authentication principal");
        }
        return name;
    }

    /** Full identity (sub, username, email, roles). */
    public static UserIdentity identityOrThrow() {
        Authentication auth = authOrThrow();
        if (!(auth instanceof JwtAuthenticationToken jat)) {
            throw new UnauthorizedException("Unsupported authentication principal");
        }
        Jwt jwt = (Jwt) jat.getToken();

        String sub = auth.getName(); // we set this to sub in KeycloakJwtAuthConverter
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        Set<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // e.g. ROLE_ADMIN
                .collect(Collectors.toUnmodifiableSet());

        return new UserIdentity(sub, username, email, roles);
    }

    private static Authentication authOrThrow() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("Authentication required");
        }
        return auth;
    }
}
