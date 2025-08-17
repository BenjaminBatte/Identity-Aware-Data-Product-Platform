package com.benjaminbatte.platform.features.user.service;

import com.benjaminbatte.platform.features.user.domain.User;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {
    /**
     * Look up or create a User from the given JWT.
     * Typically uses `sub` as stable external ID and `email` claim.
     *
     * @param jwt Spring Security validated JWT
     * @return persisted User entity
     */
    User ensureFromJwt(Jwt jwt);
}
