package at.fhtw.swen3.services.impl;

import at.fhtw.swen3.persistence.entities.WarehouseEntity;
import at.fhtw.swen3.services.WarehouseService;
import at.fhtw.swen3.services.dto.Hop;
import at.fhtw.swen3.services.dto.Warehouse;
import org.springframework.stereotype.Service;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    @Override
    public Warehouse exportWarehouses() {
        return null;
    }

    @Override
    public Hop getWarehouse(String code) {
        return null;
    }

    @Override
    public void importWarehouses(WarehouseEntity warehouse) {

    }
}
