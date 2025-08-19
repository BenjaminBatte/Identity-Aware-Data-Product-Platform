package com.benjaminbatte.platform.features.org.mapper;

import com.benjaminbatte.platform.features.org.domain.UserOrgRole;
import com.benjaminbatte.platform.features.org.dto.UserOrgRoleDto;

public final class UserOrgRoleMapper {
    private UserOrgRoleMapper() {}

    public static UserOrgRoleDto toDto(UserOrgRole uor) {
        if (uor == null) return null;
        return UserOrgRoleDto.builder()
                .userId(uor.getUser().getId())
                .orgId(uor.getOrg().getId())
                .role(uor.getRole())
                .build();
    }
}
