package com.benjaminbatte.platform.features.dataset.domain;

import com.benjaminbatte.platform.features.org.domain.Org;
import com.benjaminbatte.platform.features.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "datasets",
        uniqueConstraints = @UniqueConstraint(name = "uk_org_name", columnNames = {"org_id","name"}),
        indexes = {
                @Index(name = "ix_dataset_org", columnList = "org_id"),
                @Index(name = "ix_dataset_owner", columnList = "owner_id")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Dataset {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    private Org org;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ElementCollection
    @CollectionTable(name = "dataset_tags", joinColumns = @JoinColumn(name = "dataset_id"))
    @Column(name = "tag", nullable = false)
    @Builder.Default
    private Set<String> tags = new HashSet<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DatasetVersion> versions = new ArrayList<>();
}
