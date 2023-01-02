package at.fhtw.swen3.persistence.repositories;

import at.fhtw.swen3.persistence.entities.HopEntity;
import at.fhtw.swen3.persistence.entities.WarehouseEntity;
import at.fhtw.swen3.persistence.entities.WarehouseNextHopsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseNextHopsRepository extends JpaRepository<WarehouseNextHopsEntity, Long> {
    Optional<WarehouseNextHopsEntity> findByHop(HopEntity hop);

    Optional<WarehouseNextHopsEntity> findByHopAndWarehouse(HopEntity hop, WarehouseEntity warehouse);
}