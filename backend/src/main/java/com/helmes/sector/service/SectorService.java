package com.helmes.sector.service;

import com.helmes.sector.dto.SectorNode;
import com.helmes.sector.entity.Sector;
import com.helmes.sector.repository.SectorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SectorService {

    private final SectorRepository sectorRepository;
    
    public List<SectorNode> getSectorTree() {
        return sectorRepository.findAllRootsWithChildren().stream()
            .map(this::toNode)
            .toList();
    }

    private SectorNode toNode(final Sector sector) {
        return SectorNode.builder()
            .id(sector.getId())
            .name(sector.getName())
            .children(
                sector.getChildren().stream()
                    .map(this::toNode)
                    .toList()
            )
            .build();
    }
}
