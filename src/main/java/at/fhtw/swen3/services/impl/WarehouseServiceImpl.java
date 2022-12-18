package at.fhtw.swen3.services.impl;

import at.fhtw.swen3.persistence.entities.HopEntity;
import at.fhtw.swen3.persistence.entities.WarehouseEntity;
import at.fhtw.swen3.persistence.repositories.HopRepository;
import at.fhtw.swen3.persistence.repositories.WarehouseRepository;
import at.fhtw.swen3.services.WarehouseService;
import at.fhtw.swen3.services.validation.EntityValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {

    private final EntityValidatorService entityValidatorService;

    private final WarehouseRepository warehouseRepository;
    private final HopRepository hopRepository;

    public WarehouseServiceImpl(EntityValidatorService entityValidatorService, WarehouseRepository warehouseRepository, HopRepository hopRepository) {
        this.entityValidatorService = entityValidatorService;
        this.warehouseRepository = warehouseRepository;
        this.hopRepository = hopRepository;
    }

    @Override
    public Optional<WarehouseEntity> exportWarehouses() {
        log.debug("Exporting warehouses");
        return warehouseRepository.getFirstByLevel(0);
    }

    @Override
    public Optional<HopEntity> getWarehouse(String code) {
        log.debug("Exporting warehouse with code {}", code);
        return hopRepository.findFirstByCode(code);
    }

    @Override
    public void importWarehouses(WarehouseEntity warehouse) {
        warehouse.getNextHops().forEach(warehouseNextHopsEntity -> warehouseNextHopsEntity.setWarehouse(warehouse));
        log.debug("Importing warehouse");
        entityValidatorService.validate(warehouse);
        log.debug("Given warehouse is valid");
        warehouseRepository.save(warehouse);
        log.debug("Warehouse imported successfully");
    }
}
