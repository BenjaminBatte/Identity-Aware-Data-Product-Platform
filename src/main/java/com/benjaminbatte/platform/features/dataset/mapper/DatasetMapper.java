package com.benjaminbatte.platform.features.dataset.mapper;

import com.benjaminbatte.platform.features.dataset.domain.Dataset;
import com.benjaminbatte.platform.features.dataset.dto.DatasetDto;

public final class DatasetMapper {
    private DatasetMapper() {}

    public static DatasetDto toDto(Dataset d) {
        var owner = d.getOwner();
        return DatasetDto.builder()
                .id(d.getId())
                .orgId(d.getOrg().getId())
                .name(d.getName())
                .description(d.getDescription())
                .ownerId(owner != null ? owner.getId() : null)
                .tags(d.getTags())
                .createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt())
                .build();
    }
}
