package at.fhtw.swen3.services;

import at.fhtw.swen3.persistence.entities.HopEntity;
import at.fhtw.swen3.persistence.entities.WarehouseEntity;

import java.util.Optional;

public interface WarehouseService {
    Optional<WarehouseEntity> exportWarehouses();

    Optional<HopEntity> getWarehouse(String code);

    void importWarehouses(WarehouseEntity warehouse);
}
