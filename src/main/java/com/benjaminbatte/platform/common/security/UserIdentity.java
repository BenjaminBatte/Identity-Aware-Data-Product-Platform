// src/main/java/com/benjaminbatte/platform/common/security/UserIdentity.java
package com.benjaminbatte.platform.common.security;

import java.util.Set;

public record UserIdentity(
        String externalId,     // JWT sub
        String username,       // preferred_username (if present)
        String email,          // email (if present)
        Set<String> roles      // ROLE_* authorities granted by converter
) {}
