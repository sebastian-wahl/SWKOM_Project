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

    @Mapping(target = "location", source = "geoCoordinate", qualifiedByName = "mapLocation")
    GeoCoordinateEntity fromDto(GeoCoordinate geoCoordinate);

    @Mapping(target = "lat", source = "location", qualifiedByName = "mapLat")
    @Mapping(target = "lon", source = "location", qualifiedByName = "mapLon")
    GeoCoordinate toDto(GeoCoordinateEntity geoCoordinateEntity);

    @Named("mapLocation")
    static Point mapLocation(GeoCoordinate dto) {
        String wktPoint = String.format(Locale.US,"POINT(%f %f)", dto.getLat(), dto.getLon());
        return (Point) wktToGeometry(wktPoint);
    }

    @Named("mapLat")
    static Double mapLat(Point location) {
        return location.getX();
    }

    @Named("mapLon")
    static Double mapLon(Point location) {
        return location.getY();
    }
}
