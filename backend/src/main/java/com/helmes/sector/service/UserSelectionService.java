package com.helmes.sector.service;

import com.helmes.sector.dto.UserSelectionRequest;
import com.helmes.sector.dto.UserSelectionResponse;
import com.helmes.sector.entity.Sector;
import com.helmes.sector.entity.UserSelection;
import com.helmes.sector.repository.SectorRepository;
import com.helmes.sector.repository.UserSelectionRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSelectionService {

    private final UserSelectionRepository userSelectionRepository;
    private final SectorRepository sectorRepository;
    
    @Transactional(readOnly = true)
    public Optional<UserSelectionResponse> findBySessionId(final String sessionId) {
        return userSelectionRepository.findBySessionId(sessionId)
            .map(this::toResponse);
    }
    
    public UserSelectionResponse create(final String sessionId, final UserSelectionRequest request) {
        userSelectionRepository.findBySessionId(sessionId).ifPresent(existing -> {
            throw new IllegalStateException("Selection already exists for this session. Use update instead.");
        });

        final Set<Sector> sectors = resolveSectors(request.sectorIds());

        final UserSelection entity = UserSelection.builder()
            .sessionId(sessionId)
            .name(request.name())
            .agreeToTerms(request.agreeToTerms())
            .sectors(sectors)
            .build();

        return toResponse(userSelectionRepository.save(entity));
    }
    
    public UserSelectionResponse update(final String sessionId, final UserSelectionRequest request) {
        final UserSelection entity = userSelectionRepository.findBySessionId(sessionId)
            .orElseThrow(() -> new IllegalStateException("No selection found for this session. Use create instead."));

        final Set<Sector> sectors = resolveSectors(request.sectorIds());

        entity.setName(request.name());
        entity.setAgreeToTerms(request.agreeToTerms());
        entity.setSectors(sectors);

        return toResponse(userSelectionRepository.save(entity));
    }

    private Set<Sector> resolveSectors(final Set<Long> sectorIds) {
        final Set<Sector> sectors = new HashSet<>(sectorRepository.findAllById(sectorIds));

        if (sectors.size() != sectorIds.size()) {
            throw new IllegalArgumentException("One or more sector IDs are invalid.");
        }

        return sectors;
    }

    private UserSelectionResponse toResponse(final UserSelection entity) {
        return UserSelectionResponse.builder()
            .id(entity.getId())
            .name(entity.getName())
            .sectorIds(
                entity.getSectors().stream()
                    .map(Sector::getId)
                    .collect(Collectors.toSet())
            )
            .agreeToTerms(entity.isAgreeToTerms())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
