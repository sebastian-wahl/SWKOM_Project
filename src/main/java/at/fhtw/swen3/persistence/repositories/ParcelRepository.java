package at.fhtw.swen3.persistence.repositories;

import at.fhtw.swen3.persistence.entities.ParcelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParcelRepository extends JpaRepository<ParcelEntity, Long> {
    Optional<ParcelEntity> findFirstByTrackingId(String trackingId);
}