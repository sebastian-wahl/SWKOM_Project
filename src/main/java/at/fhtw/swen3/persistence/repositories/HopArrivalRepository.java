package at.fhtw.swen3.persistence.repositories;

import at.fhtw.swen3.persistence.entities.HopArrivalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HopArrivalRepository extends JpaRepository<HopArrivalEntity, Long> {
    Optional<HopArrivalEntity> findFirstByCode(String code);
}