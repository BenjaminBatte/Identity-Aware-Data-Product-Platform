package com.benjaminbatte.platform.features.org.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class OrgDto {
    UUID id;
    String name;
    String slug;
    String description;
    Instant createdAt;
    Instant updatedAt;
}
