package com.benjaminbatte.platform.features.dataset.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "dataset_profile_ref",
        indexes = @Index(name = "ix_profile_version", columnList = "dataset_version_id", unique = true))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DatasetProfileRef {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "dataset_version_id", nullable = false, unique = true)
    private DatasetVersion datasetVersion;

    /** MongoDB document _id */
    @Column(nullable = false, length = 128)
    private String profileDocId;

    @Column(nullable = false)
    private Instant generatedAt;
}
