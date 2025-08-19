package com.benjaminbatte.platform.features.user.service;

import com.benjaminbatte.platform.features.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User ensureFromJwt(Jwt jwt);

    Optional<User> findById(UUID id);

    Page<User> findAll(Pageable pageable);
}
