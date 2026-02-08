package com.helmes.sector.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record SectorNode(
    Long id,
    String name,
    List<SectorNode> children
) {
}

