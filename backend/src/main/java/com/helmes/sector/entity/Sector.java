package com.helmes.sector.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sectors")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Sector {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Sector parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("name ASC")
    private List<Sector> children = new ArrayList<>();
}
