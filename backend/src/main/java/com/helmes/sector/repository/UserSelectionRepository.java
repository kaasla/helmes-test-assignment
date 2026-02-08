package com.helmes.sector.repository;

import com.helmes.sector.entity.UserSelection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSelectionRepository extends JpaRepository<UserSelection, Long> {

    Optional<UserSelection> findBySessionId(String sessionId);
}

