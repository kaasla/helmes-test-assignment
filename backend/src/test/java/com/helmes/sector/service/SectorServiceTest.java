package com.helmes.sector.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.helmes.sector.dto.SectorNode;
import com.helmes.sector.entity.Sector;
import com.helmes.sector.repository.SectorRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("SectorService unit tests")
class SectorServiceTest {

    @Mock
    private SectorRepository sectorRepository;

    @InjectMocks
    private SectorService sectorService;

    @Test
    @DisplayName("should return empty list when no sectors exist")
    void getSectorTree_empty() {
        when(sectorRepository.findAllRootsWithChildren()).thenReturn(List.of());

        final List<SectorNode> result = sectorService.getSectorTree();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should map flat root sectors correctly")
    void getSectorTree_flatRoots() {
        final Sector root = createSector(1L, "Manufacturing");

        when(sectorRepository.findAllRootsWithChildren()).thenReturn(List.of(root));

        final List<SectorNode> result = sectorService.getSectorTree();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().id()).isEqualTo(1L);
        assertThat(result.getFirst().name()).isEqualTo("Manufacturing");
        assertThat(result.getFirst().children()).isEmpty();
    }

    @Test
    @DisplayName("should map nested children recursively")
    void getSectorTree_withChildren() {
        final Sector child = createSector(6L, "Food and Beverage");
        final Sector root = createSector(1L, "Manufacturing");
        ReflectionTestUtils.setField(root, "children", List.of(child));

        when(sectorRepository.findAllRootsWithChildren()).thenReturn(List.of(root));

        final List<SectorNode> result = sectorService.getSectorTree();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().children()).hasSize(1);
        assertThat(result.getFirst().children().getFirst().name()).isEqualTo("Food and Beverage");
    }

    private Sector createSector(final Long id, final String name) {
        final Sector sector = org.springframework.beans.BeanUtils.instantiateClass(Sector.class);
        ReflectionTestUtils.setField(sector, "id", id);
        ReflectionTestUtils.setField(sector, "name", name);
        return sector;
    }
}

