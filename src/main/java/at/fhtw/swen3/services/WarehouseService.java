package at.fhtw.swen3.services;

import at.fhtw.swen3.services.dto.Hop;
import at.fhtw.swen3.services.dto.Warehouse;

public interface WarehouseService {
    Warehouse exportWarehouses();
    Hop getWarehouse(String code);
    void importWarehouses(Warehouse warehouseDto);
}
