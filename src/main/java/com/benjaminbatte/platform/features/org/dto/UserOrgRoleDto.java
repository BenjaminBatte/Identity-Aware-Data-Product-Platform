package com.benjaminbatte.platform.features.org.dto;

import com.benjaminbatte.platform.features.org.domain.Role;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserOrgRoleDto {
    UUID userId;
    UUID orgId;
    Role role;
}
