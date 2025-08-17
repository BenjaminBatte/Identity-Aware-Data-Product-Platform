package com.benjaminbatte.platform.features.user.mapper;

import com.benjaminbatte.platform.features.org.domain.UserOrgRole;
import com.benjaminbatte.platform.features.user.domain.User;
import com.benjaminbatte.platform.features.user.dto.UserDto;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class UserMapper {
    private UserMapper() {}

    public static UserDto toDto(User u) {
        if (u == null) return null;
        Set<UUID> orgIds = u.getMemberships() == null ? Set.of()
                : u.getMemberships().stream().map(UserOrgRole::getOrg).map(o -> o.getId()).collect(Collectors.toSet());
        return UserDto.builder()
                .id(u.getId())
                .email(u.getEmail())
                .displayName(u.getDisplayName())
                .externalId(u.getExternalId())
                .orgIds(orgIds)
                .createdAt(u.getCreatedAt())
                .updatedAt(u.getUpdatedAt())
                .build();
    }
}
