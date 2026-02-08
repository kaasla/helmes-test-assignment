package com.helmes.sector.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.helmes.sector.dto.SectorNode;
import com.helmes.sector.service.SectorService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SectorController.class)
@DisplayName("SectorController tests")
class SectorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SectorService sectorService;

    @Test
    @DisplayName("GET /api/v1/sectors should return the sector tree")
    void getSectors() throws Exception {
        final List<SectorNode> tree = List.of(
            SectorNode.builder()
                .id(1L)
                .name("Manufacturing")
                .children(List.of(
                    SectorNode.builder()
                        .id(19L)
                        .name("Construction materials")
                        .children(List.of())
                        .build()
                ))
                .build(),
            SectorNode.builder()
                .id(2L)
                .name("Service")
                .children(List.of())
                .build()
        );

        when(sectorService.getSectorTree()).thenReturn(tree);

        mockMvc.perform(get("/api/v1/sectors"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("Manufacturing"))
            .andExpect(jsonPath("$[0].children.length()").value(1))
            .andExpect(jsonPath("$[0].children[0].name").value("Construction materials"))
            .andExpect(jsonPath("$[1].name").value("Service"));
    }

    @Test
    @DisplayName("GET /api/v1/sectors should return empty list when no sectors")
    void getSectors_empty() throws Exception {
        when(sectorService.getSectorTree()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/sectors"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }
}
