package com.helmes.sector.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import lombok.Builder;

@Builder
public record UserSelectionRequest(

    @NotBlank(message = "Name is required")
    String name,

    @NotEmpty(message = "At least one sector must be selected")
    Set<Long> sectorIds,

    @AssertTrue(message = "You must agree to the terms")
    boolean agreeToTerms
) {
}
