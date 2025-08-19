package com.benjaminbatte.platform.features.user.domain;

import com.benjaminbatte.platform.features.org.domain.UserOrgRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "ux_users_email", columnList = "email", unique = true),
                @Index(name = "ux_users_external_id", columnList = "external_id", unique = true)
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    @Email
    @Size(max = 320) // RFC-ish upper bound, matches your column
    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(name = "status", nullable = false)
    private boolean active; // renamed for clarity (maps to status column)

    @Size(max = 200)
    @Column(name = "display_name", length = 200)
    private String displayName;

    /** Stable identifier from the identity provider (OIDC `sub`). */
    @NotBlank
    @Size(max = 255)
    @Column(name = "external_id", nullable = false, unique = true, length = 255)
    private String externalId;

    @Builder.Default
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true, // <-- set true if removing from the set should delete the row
            fetch = FetchType.LAZY
    )
    @JsonIgnore // prevent recursion if entity is ever serialized directly
    private Set<UserOrgRole> memberships = new HashSet<>();

    @Version
    private Long version; // optimistic locking to prevent lost updates

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // --- Helpers to keep both sides of the association in sync ---
    public void addMembership(UserOrgRole membership) {
        memberships.add(membership);
        membership.setUser(this);
    }

    public void removeMembership(UserOrgRole membership) {
        memberships.remove(membership);
        membership.setUser(null);
    }

    // Normalize email to lowercase to keep the unique index consistent
    @PrePersist @PreUpdate
    private void normalize() {
        if (email != null) {
            email = email.trim().toLowerCase();
        }
        if (displayName != null) {
            displayName = displayName.trim();
        }
        if (externalId != null) {
            externalId = externalId.trim();
        }
    }

    // Equality on primary key only (safe once persisted)
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override public int hashCode() {
        return 31; // stable, avoids hash changes pre/post persist
    }
}
