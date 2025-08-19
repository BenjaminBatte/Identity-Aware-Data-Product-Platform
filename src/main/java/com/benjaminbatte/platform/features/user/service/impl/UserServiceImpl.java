package com.benjaminbatte.platform.features.user.service.impl;
import com.benjaminbatte.platform.common.exception.UnauthorizedException;
import com.benjaminbatte.platform.features.user.domain.User;
import com.benjaminbatte.platform.features.user.repo.UserRepository;
import com.benjaminbatte.platform.features.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

        @Override
        @Transactional
        public User ensureFromJwt(Jwt jwt) {
            final String externalId = jwt.getSubject(); // OIDC "sub"
            if (externalId == null || externalId.isBlank()) {
                throw new UnauthorizedException("JWT missing required subject (sub)");
            }

            String email = normalizeEmail(jwt.getClaimAsString("email"));
            if (email == null) {
                String preferred = jwt.getClaimAsString("preferred_username");
                if (preferred != null && preferred.contains("@")) {
                    email = normalizeEmail(preferred);
                }
            }

            String display = jwt.getClaimAsString("name");
            if (display == null || display.isBlank()) {
                display = (email != null && !email.isBlank()) ? email : externalId;
            }

            // 1) Lookup by externalId
            User user = userRepo.findByExternalId(externalId).orElse(null);
            if (user != null) {
                boolean changed = false;
                if (email != null && !email.equals(user.getEmail())) {
                    user.setEmail(email);
                    changed = true;
                }
                if (display != null && !display.equals(user.getDisplayName())) {
                    user.setDisplayName(display);
                    changed = true;
                }
                if (user.getExternalId() == null) {
                    user.setExternalId(externalId);
                    changed = true;
                }
                return changed ? userRepo.save(user) : user;
            }

            // 2) Legacy rows by email
            if (email != null) {
                user = userRepo.findByEmail(email).orElse(null);
                if (user != null) {
                    user.setExternalId(externalId);
                    if (display != null && !display.isBlank()) user.setDisplayName(display);
                    if (user.getEmail() == null || user.getEmail().isBlank()) user.setEmail(email);
                    return userRepo.save(user);
                }
            }

            // 3) Create new
            return userRepo.save(User.builder()
                    .externalId(externalId)
                    .email(email)
                    .displayName(display)
                    .isActive(true)
                    .build());
        }

        @Override
        @Transactional(readOnly = true)
        public Optional<User> findById(UUID id) {
            return userRepo.findById(id);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<User> findAll(Pageable pageable) {
            return userRepo.findAll(pageable);
        }
        private static String normalizeEmail(String raw) {
            if (raw == null) return null;
            String e = raw.trim().toLowerCase();
            return e.isEmpty() ? null : e;
        }
}