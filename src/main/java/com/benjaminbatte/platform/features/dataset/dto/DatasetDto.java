package com.benjaminbatte.platform.features.dataset.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class DatasetDto {
    UUID id;
    UUID orgId;
    String name;
    String description;
    UUID ownerId;
    Set<String> tags;
    Instant createdAt;
    Instant updatedAt;
}
