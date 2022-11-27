package at.fhtw.swen3.services.mapper;

import at.fhtw.swen3.persistence.entities.GeoCoordinateEntity;
import at.fhtw.swen3.services.dto.GeoCoordinate;
import org.locationtech.jts.geom.Point;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Locale;

import static at.fhtw.swen3.util.JTSUtil.wktToGeometry;

@Mapper
public interface GeoCoordinateMapper {

    GeoCoordinateMapper INSTANCE = Mappers.getMapper(GeoCoordinateMapper.class);

    GeoCoordinateEntity fromDto(GeoCoordinate geoCoordinate);
    GeoCoordinate toDto(GeoCoordinateEntity geoCoordinateEntity);
}