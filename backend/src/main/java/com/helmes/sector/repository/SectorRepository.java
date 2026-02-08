package com.helmes.sector.repository;

import com.helmes.sector.entity.Sector;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SectorRepository extends JpaRepository<Sector, Long> {

    @Query("SELECT s FROM Sector s LEFT JOIN FETCH s.children WHERE s.parent IS NULL ORDER BY s.name")
    List<Sector> findAllRootsWithChildren();
}
