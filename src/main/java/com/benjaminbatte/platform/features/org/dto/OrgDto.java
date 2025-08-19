package com.benjaminbatte.platform.features.org.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

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
