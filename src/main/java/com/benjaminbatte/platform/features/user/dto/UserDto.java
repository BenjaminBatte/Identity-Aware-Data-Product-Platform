package com.benjaminbatte.platform.features.user.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class UserDto {
    UUID id;
    String email;
    String displayName;
    String externalId;
    Set<UUID> orgIds;
    Instant createdAt;
    Instant updatedAt;
}
