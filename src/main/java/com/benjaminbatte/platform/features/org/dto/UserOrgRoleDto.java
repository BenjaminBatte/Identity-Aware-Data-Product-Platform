package com.benjaminbatte.platform.features.org.dto;

import com.benjaminbatte.platform.features.org.domain.Role;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class UserOrgRoleDto {
    UUID userId;
    UUID orgId;
    Role role;
}
