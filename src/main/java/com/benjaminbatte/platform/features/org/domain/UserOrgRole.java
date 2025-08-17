package com.benjaminbatte.platform.features.org.domain;

import com.benjaminbatte.platform.features.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_org_roles",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_org", columnNames = {"user_id","org_id"}),
        indexes = {
                @Index(name = "ix_uor_user", columnList = "user_id"),
                @Index(name = "ix_uor_org", columnList = "org_id")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserOrgRole {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    private Org org;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Role role;

    @Column(nullable = false)
    private Instant grantedAt;
}
