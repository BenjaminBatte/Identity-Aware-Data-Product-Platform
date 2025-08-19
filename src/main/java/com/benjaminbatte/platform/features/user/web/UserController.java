package com.benjaminbatte.platform.features.user.web;

import com.benjaminbatte.platform.common.web.versioning.ApiV1;          // <-- adds /v1 prefix
import com.benjaminbatte.platform.features.user.dto.UserDto;
import com.benjaminbatte.platform.features.user.mapper.UserMapper;
import com.benjaminbatte.platform.features.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * User APIs (v1).
 *
 * Final routes (given server.servlet.context-path=/api and @ApiV1):
 *   PUT  /api/v1/users/me      -> ensureCurrentUser
 *   GET  /api/v1/users/me      -> getMe
 *   GET  /api/v1/users/{id}    -> getUser
 *   GET  /api/v1/users         -> listUsers
 */
@RestController
@ApiV1
@RequestMapping("/users") // do NOT include /api or /v1 here
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    /** Ensure a user exists from JWT (first login bootstrap).
     *  Idempotent: using PUT is appropriate. */
    @PutMapping("/me")
    public ResponseEntity<UserDto> ensureCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userMapper.toDto(userService.ensureFromJwt(jwt)));
    }

    /** Get my profile (must be authenticated). */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userMapper.toDto(userService.ensureFromJwt(jwt)));
    }

    /** Get a user by id (ADMIN or the user themselves). */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isSelf(#id)")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID id) {
        return userService.findById(id)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** List users (ADMIN only). */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDto> listUsers(Pageable pageable) {
        return userService.findAll(pageable).map(userMapper::toDto);
    }
}
