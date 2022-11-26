package at.fhtw.swen3.services.impl;

import at.fhtw.swen3.persistence.entities.HopEntity;
import at.fhtw.swen3.persistence.entities.WarehouseEntity;
import at.fhtw.swen3.persistence.repositories.HopRepository;
import at.fhtw.swen3.persistence.repositories.WarehouseRepository;
import at.fhtw.swen3.services.WarehouseService;
import at.fhtw.swen3.services.validation.EntityValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private EntityValidatorService entityValidatorService;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private HopRepository hopRepository;

    @Override
    public WarehouseEntity exportWarehouses() {
        // ToDo why only one warehouse (plural?)
        return new WarehouseEntity();
    }

    @Override
    public Optional<HopEntity> getWarehouse(String code) {
        return hopRepository.findFirstByCode(code);
    }

    @Override
    public void importWarehouses(WarehouseEntity warehouse) {
        entityValidatorService.validate(warehouse);
        warehouseRepository.save(warehouse);
    }
}
