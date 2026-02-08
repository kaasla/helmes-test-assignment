package com.helmes.sector.controller;

import com.helmes.sector.dto.SectorNode;
import com.helmes.sector.service.SectorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sectors")
@RequiredArgsConstructor
@Tag(name = "Sectors", description = "Hierarchical sector data")
public class SectorController {

    private final SectorService sectorService;

    @GetMapping
    @Operation(summary = "Get all sectors as a hierarchical tree")
    public List<SectorNode> getAll() {
        return sectorService.getSectorTree();
    }
}
