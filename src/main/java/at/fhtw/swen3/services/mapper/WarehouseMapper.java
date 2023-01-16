package at.fhtw.swen3.services.mapper;

import at.fhtw.swen3.persistence.entities.WarehouseEntity;
import at.fhtw.swen3.persistence.entities.WarehouseNextHopsEntity;
import at.fhtw.swen3.services.dto.Warehouse;
import at.fhtw.swen3.services.dto.WarehouseNextHops;
import org.mapstruct.Builder;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {GeoCoordinateMapper.class, HopMapper.class}, builder = @Builder(disableBuilder = true))

public interface WarehouseMapper {

    WarehouseMapper INSTANCE = Mappers.getMapper(WarehouseMapper.class);

    WarehouseEntity fromDto(Warehouse warehouse, @Context JpaWarehouseContext jpaWarehouseContext);

    @Mapping(target = "warehouse", ignore = true)
    WarehouseNextHopsEntity fromDto(WarehouseNextHops warehouseNextHops, @Context JpaWarehouseContext jpaWarehouseContext);

    Warehouse toDto(WarehouseEntity warehouseEntity);

    WarehouseNextHops toDto(WarehouseNextHopsEntity warehouseNextHopsEntity);
}
