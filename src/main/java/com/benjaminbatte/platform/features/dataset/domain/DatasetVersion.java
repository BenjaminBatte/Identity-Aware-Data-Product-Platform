package com.benjaminbatte.platform.features.dataset.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "dataset_versions",
        uniqueConstraints = @UniqueConstraint(name = "uk_dataset_version", columnNames = {"dataset_id","version"}),
        indexes = @Index(name = "ix_dsv_dataset", columnList = "dataset_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DatasetVersion {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "dataset_id")
    private Dataset dataset;

    @Column(nullable = false)
    private int version;

    @Column(nullable = false, length = 1000)
    private String storageUri;

    @Column(columnDefinition = "text")
    private String schemaJson;

    private long rowCount;

    @Column(length = 128)
    private String checksum;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @OneToOne(mappedBy = "datasetVersion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private DatasetProfileRef profile;
}
