package at.fhtw.swen3.persistence.repositories;

import at.fhtw.swen3.persistence.entities.HopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HopRepository extends JpaRepository<HopEntity, Long> {
    HopEntity findFirstByCode(String code);
}