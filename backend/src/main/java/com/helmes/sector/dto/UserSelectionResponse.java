package com.helmes.sector.dto;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;

@Builder
public record UserSelectionResponse(
    Long id,
    String name,
    Set<Long> sectorIds,
    boolean agreeToTerms,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}

