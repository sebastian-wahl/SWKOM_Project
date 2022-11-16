package at.fhtw.swen3.services.impl;

import at.fhtw.swen3.persistence.entities.WarehouseEntity;
import at.fhtw.swen3.services.WarehouseService;
import at.fhtw.swen3.services.dto.GeoCoordinate;
import at.fhtw.swen3.services.dto.Hop;
import at.fhtw.swen3.services.dto.Warehouse;
import at.fhtw.swen3.services.validation.EntityValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private EntityValidatorService entityValidatorService;

    @Override
    public Warehouse exportWarehouses() {
        return new Warehouse()
                .level(24)
                .code("ABCD1")
                .description("description")
                .hopType("hopType")
                .locationName("locationName")
                .processingDelayMins(1)
                .locationCoordinates(new GeoCoordinate().lat(0.0).lon(2.0));
    }

    @Override
    public Hop getWarehouse(String code) {
        return new Hop()
                .code("ABCD2")
                .description("description")
                .hopType("hopType")
                .locationName("locationName")
                .processingDelayMins(1)
                .locationCoordinates(new GeoCoordinate().lat(1.0).lon(2.0));
    }

    @Override
    public void importWarehouses(WarehouseEntity warehouse) {
        entityValidatorService.validate(warehouse);
    }
}
