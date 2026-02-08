package com.helmes.sector.controller;

import com.helmes.sector.dto.UserSelectionRequest;
import com.helmes.sector.dto.UserSelectionResponse;
import com.helmes.sector.service.UserSelectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user-selections")
@RequiredArgsConstructor
@Tag(name = "User Selections", description = "Session-scoped user selection management")
public class UserSelectionController {

    private final UserSelectionService userSelectionService;

    @GetMapping("/me")
    @Operation(summary = "Get the current session's saved selection")
    public ResponseEntity<UserSelectionResponse> getMine(final HttpSession session) {
        final Optional<UserSelectionResponse> result = userSelectionService.findBySessionId(session.getId());
        return result.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping
    @Operation(summary = "Save a new selection for the current session")
    public ResponseEntity<UserSelectionResponse> create(
        @Valid @RequestBody final UserSelectionRequest request,
        final HttpSession session
    ) {
        final UserSelectionResponse response = userSelectionService.create(session.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/me")
    @Operation(summary = "Update the current session's selection")
    public ResponseEntity<UserSelectionResponse> update(
        @Valid @RequestBody final UserSelectionRequest request,
        final HttpSession session
    ) {
        final UserSelectionResponse response = userSelectionService.update(session.getId(), request);
        return ResponseEntity.ok(response);
    }
}
