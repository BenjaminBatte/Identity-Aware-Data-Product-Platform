package com.benjaminbatte.platform.features.org.mapper;

import com.benjaminbatte.platform.features.org.domain.Org;
import com.benjaminbatte.platform.features.org.dto.OrgDto;

public final class OrgMapper {
    private OrgMapper() {}

    public static OrgDto toDto(Org o) {
        if (o == null) return null;
        return OrgDto.builder()
                .id(o.getId())
                .name(o.getName())
                .slug(o.getSlug())
                .description(o.getDescription())
                .createdAt(o.getCreatedAt())
                .updatedAt(o.getUpdatedAt())
                .build();
    }
}
