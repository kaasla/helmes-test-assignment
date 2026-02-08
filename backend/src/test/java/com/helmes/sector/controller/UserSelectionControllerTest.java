package com.helmes.sector.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.helmes.sector.dto.UserSelectionResponse;
import com.helmes.sector.service.UserSelectionService;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserSelectionController.class)
@DisplayName("UserSelectionController tests")
class UserSelectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserSelectionService userSelectionService;

    private final MockHttpSession session = new MockHttpSession();

    @Test
    @DisplayName("GET /me should return 204 when no selection exists")
    void getMe_noContent() throws Exception {
        when(userSelectionService.findBySessionId(session.getId())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/user-selections/me").session(session))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /me should return saved selection")
    void getMe_returnsData() throws Exception {
        when(userSelectionService.findBySessionId(session.getId()))
            .thenReturn(Optional.of(buildResponse()));

        mockMvc.perform(get("/api/v1/user-selections/me").session(session))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"))
            .andExpect(jsonPath("$.sectorIds").isArray())
            .andExpect(jsonPath("$.agreeToTerms").value(true));
    }

    @Test
    @DisplayName("POST should create selection and return 201")
    void create_success() throws Exception {
        when(userSelectionService.create(eq(session.getId()), any())).thenReturn(buildResponse());

        mockMvc.perform(post("/api/v1/user-selections")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name":"John","sectorIds":[1,28],"agreeToTerms":true}"""))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    @DisplayName("POST should return 400 on validation failure")
    void create_validationFailure() throws Exception {
        mockMvc.perform(post("/api/v1/user-selections")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name":"","sectorIds":[],"agreeToTerms":false}"""))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors.name").value("Name is required"))
            .andExpect(jsonPath("$.errors.sectorIds").value("At least one sector must be selected"))
            .andExpect(jsonPath("$.errors.agreeToTerms").value("You must agree to the terms"));
    }

    @Test
    @DisplayName("POST should return 409 when selection already exists")
    void create_conflict() throws Exception {
        when(userSelectionService.create(eq(session.getId()), any()))
            .thenThrow(new IllegalStateException("Selection already exists"));

        mockMvc.perform(post("/api/v1/user-selections")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name":"John","sectorIds":[1],"agreeToTerms":true}"""))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("PUT /me should update selection")
    void update_success() throws Exception {
        final UserSelectionResponse updated = UserSelectionResponse.builder()
            .id(1L)
            .name("Jane")
            .sectorIds(Set.of(2L))
            .agreeToTerms(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        when(userSelectionService.update(eq(session.getId()), any())).thenReturn(updated);

        mockMvc.perform(put("/api/v1/user-selections/me")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name":"Jane","sectorIds":[2],"agreeToTerms":true}"""))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Jane"));
    }

    @Test
    @DisplayName("PUT /me should return 409 when no selection exists")
    void update_notFound() throws Exception {
        when(userSelectionService.update(eq(session.getId()), any()))
            .thenThrow(new IllegalStateException("No selection found"));

        mockMvc.perform(put("/api/v1/user-selections/me")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name":"Jane","sectorIds":[1],"agreeToTerms":true}"""))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("POST should return 400 for invalid sector IDs")
    void create_invalidSectors() throws Exception {
        when(userSelectionService.create(eq(session.getId()), any()))
            .thenThrow(new IllegalArgumentException("One or more sector IDs are invalid."));

        mockMvc.perform(post("/api/v1/user-selections")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name":"Test","sectorIds":[999],"agreeToTerms":true}"""))
            .andExpect(status().isBadRequest());
    }

    private UserSelectionResponse buildResponse() {
        return UserSelectionResponse.builder()
            .id(1L)
            .name("John")
            .sectorIds(Set.of(1L, 28L))
            .agreeToTerms(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }
}
