package com.helmes.sector.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_selections")
@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Builder
public class UserSelection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false, unique = true)
    private String sessionId;

    @Column(nullable = false)
    private String name;

    @Column(name = "agree_to_terms", nullable = false)
    private boolean agreeToTerms;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_selection_sectors",
        joinColumns = @JoinColumn(name = "user_selection_id"),
        inverseJoinColumns = @JoinColumn(name = "sector_id")
    )
    @Builder.Default
    private Set<Sector> sectors = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
