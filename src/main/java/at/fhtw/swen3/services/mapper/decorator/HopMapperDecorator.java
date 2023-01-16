package at.fhtw.swen3.services.mapper.decorator;

import at.fhtw.swen3.persistence.entities.HopEntity;
import at.fhtw.swen3.persistence.entities.TransferwarehouseEntity;
import at.fhtw.swen3.persistence.entities.TruckEntity;
import at.fhtw.swen3.persistence.entities.WarehouseEntity;
import at.fhtw.swen3.services.dto.Hop;
import at.fhtw.swen3.services.dto.Transferwarehouse;
import at.fhtw.swen3.services.dto.Truck;
import at.fhtw.swen3.services.dto.Warehouse;
import at.fhtw.swen3.services.mapper.*;

public abstract class HopMapperDecorator implements HopMapper {

    private final HopMapper delegate;

    protected HopMapperDecorator(HopMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public HopEntity fromDto(Hop hop) {
        if (hop instanceof Transferwarehouse transferwarehouse) {
            return TransferwarehouseMapper.INSTANCE.fromDto(transferwarehouse);
        } else if (hop instanceof Truck truck) {
            return TruckMapper.INSTANCE.fromDto(truck);
        } else if (hop instanceof Warehouse warehouse) {
            return WarehouseMapper.INSTANCE.fromDto(warehouse, new JpaWarehouseContext());
        }
        return delegate.fromDto(hop);
    }

    @Override
    public Hop toDto(HopEntity hop) {
        if (hop instanceof TransferwarehouseEntity transferwarehouse) {
            return TransferwarehouseMapper.INSTANCE.toDto(transferwarehouse);
        } else if (hop instanceof TruckEntity truck) {
            return TruckMapper.INSTANCE.toDto(truck);
        } else if (hop instanceof WarehouseEntity warehouse) {
            return WarehouseMapper.INSTANCE.toDto(warehouse);
        }
        return delegate.toDto(hop);
    }
}
