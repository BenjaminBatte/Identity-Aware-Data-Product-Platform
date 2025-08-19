package com.benjaminbatte.platform.config;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

/**
 * Validates that the JWT "aud" (audience) contains the expected value.
 * Prevents tokens meant for other services from being accepted here.
 */
class RequiredAudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final String requiredAudience;

    private static final OAuth2Error MISSING_AUD =
            new OAuth2Error("invalid_token", "Required audience missing", null);

    RequiredAudienceValidator(String requiredAudience) {
        this.requiredAudience = requiredAudience;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        List<String> audiences = token.getAudience();
        return (audiences != null && audiences.contains(requiredAudience))
                ? OAuth2TokenValidatorResult.success()
                : OAuth2TokenValidatorResult.failure(MISSING_AUD);
    }
}
