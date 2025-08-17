package com.benjaminbatte.platform.features.user.service;

import com.benjaminbatte.platform.features.user.domain.User;
import com.benjaminbatte.platform.features.user.repo.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements com.benjaminbatte.platform.features.user.service.UserService {

    private final UserRepository userRepo;

    @Override
    @Transactional
    public User ensureFromJwt(Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        if (email == null || email.isBlank()) {
            email = jwt.getClaimAsString("preferred_username");
        }
        String display = jwt.getClaimAsString("name");
        if (display == null || display.isBlank()) {
            display = email != null ? email : jwt.getSubject();
        }

        String finalEmail = email;
        String finalDisplay = display;
        return userRepo.findByEmail(finalEmail)
                .orElseGet(() -> userRepo.save(
                        User.builder()
                                .email(finalEmail)
                                .displayName(finalDisplay)
                                .active(true)
                                .build()));
    }
}
