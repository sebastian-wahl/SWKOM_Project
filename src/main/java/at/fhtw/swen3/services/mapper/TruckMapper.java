package at.fhtw.swen3.services.mapper;

import at.fhtw.swen3.persistence.entities.TruckEntity;
import at.fhtw.swen3.services.dto.Truck;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = GeoCoordinateMapper.class)
public interface TruckMapper {

    TruckMapper INSTANCE = Mappers.getMapper(TruckMapper.class);

    TruckEntity fromDto(Truck truck);
    Truck toDto(TruckEntity truckEntity);
}
