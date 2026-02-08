package com.helmes.sector.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

import com.helmes.sector.dto.UserSelectionRequest;
import com.helmes.sector.dto.UserSelectionResponse;
import com.helmes.sector.entity.Sector;
import com.helmes.sector.entity.UserSelection;
import com.helmes.sector.repository.SectorRepository;
import com.helmes.sector.repository.UserSelectionRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserSelectionService unit tests")
class UserSelectionServiceTest {

    private static final String SESSION_ID = "session-123";

    @Mock
    private UserSelectionRepository userSelectionRepository;

    @Mock
    private SectorRepository sectorRepository;

    @InjectMocks
    private UserSelectionService userSelectionService;

    @Nested
    @DisplayName("findBySessionId")
    class FindBySessionId {

        @Test
        @DisplayName("should return empty when no selection exists")
        void returnsEmpty() {
            when(userSelectionRepository.findBySessionId(SESSION_ID)).thenReturn(Optional.empty());

            assertThat(userSelectionService.findBySessionId(SESSION_ID)).isEmpty();
        }

        @Test
        @DisplayName("should return mapped response when selection exists")
        void returnsResponse() {
            final UserSelection entity = buildEntity();
            when(userSelectionRepository.findBySessionId(SESSION_ID)).thenReturn(Optional.of(entity));

            final Optional<UserSelectionResponse> result = userSelectionService.findBySessionId(SESSION_ID);

            assertThat(result).isPresent();
            assertThat(result.get().name()).isEqualTo("John");
            assertThat(result.get().sectorIds()).containsExactly(1L);
        }
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("should create and return selection")
        void createsSuccessfully() {
            final UserSelectionRequest request = UserSelectionRequest.builder()
                .name("John")
                .sectorIds(Set.of(1L))
                .agreeToTerms(true)
                .build();

            when(userSelectionRepository.findBySessionId(SESSION_ID)).thenReturn(Optional.empty());
            when(sectorRepository.findAllById(anySet())).thenReturn(List.of(createSector(1L)));
            when(userSelectionRepository.save(any())).thenAnswer(inv -> {
                final UserSelection saved = inv.getArgument(0);
                ReflectionTestUtils.setField(saved, "id", 1L);
                return saved;
            });

            final UserSelectionResponse response = userSelectionService.create(SESSION_ID, request);

            assertThat(response.name()).isEqualTo("John");
            assertThat(response.agreeToTerms()).isTrue();
        }

        @Test
        @DisplayName("should throw when selection already exists")
        void throwsOnDuplicate() {
            when(userSelectionRepository.findBySessionId(SESSION_ID)).thenReturn(Optional.of(buildEntity()));

            final UserSelectionRequest request = UserSelectionRequest.builder()
                .name("John")
                .sectorIds(Set.of(1L))
                .agreeToTerms(true)
                .build();

            assertThatThrownBy(() -> userSelectionService.create(SESSION_ID, request))
                .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("should throw when sector IDs are invalid")
        void throwsOnInvalidSectors() {
            when(userSelectionRepository.findBySessionId(SESSION_ID)).thenReturn(Optional.empty());
            when(sectorRepository.findAllById(anySet())).thenReturn(List.of());

            final UserSelectionRequest request = UserSelectionRequest.builder()
                .name("John")
                .sectorIds(Set.of(999L))
                .agreeToTerms(true)
                .build();

            assertThatThrownBy(() -> userSelectionService.create(SESSION_ID, request))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("should update existing selection")
        void updatesSuccessfully() {
            final UserSelection existing = buildEntity();
            when(userSelectionRepository.findBySessionId(SESSION_ID)).thenReturn(Optional.of(existing));
            when(sectorRepository.findAllById(anySet())).thenReturn(List.of(createSector(2L)));
            when(userSelectionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            final UserSelectionRequest request = UserSelectionRequest.builder()
                .name("Jane")
                .sectorIds(Set.of(2L))
                .agreeToTerms(true)
                .build();

            final UserSelectionResponse response = userSelectionService.update(SESSION_ID, request);

            assertThat(response.name()).isEqualTo("Jane");
        }

        @Test
        @DisplayName("should throw when no selection exists")
        void throwsWhenNotFound() {
            when(userSelectionRepository.findBySessionId(SESSION_ID)).thenReturn(Optional.empty());

            final UserSelectionRequest request = UserSelectionRequest.builder()
                .name("Jane")
                .sectorIds(Set.of(1L))
                .agreeToTerms(true)
                .build();

            assertThatThrownBy(() -> userSelectionService.update(SESSION_ID, request))
                .isInstanceOf(IllegalStateException.class);
        }
    }

    private UserSelection buildEntity() {
        final Sector sector = createSector(1L);
        final UserSelection entity = UserSelection.builder()
            .sessionId(SESSION_ID)
            .name("John")
            .agreeToTerms(true)
            .sectors(Set.of(sector))
            .build();
        ReflectionTestUtils.setField(entity, "id", 1L);
        return entity;
    }

    private Sector createSector(final Long id) {
        final Sector sector = org.springframework.beans.BeanUtils.instantiateClass(Sector.class);
        ReflectionTestUtils.setField(sector, "id", id);
        ReflectionTestUtils.setField(sector, "name", "Test Sector");
        return sector;
    }
}

