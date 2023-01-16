package at.fhtw.swen3.services.mapper;

import at.fhtw.swen3.persistence.entities.GeoCoordinateEntity;
import at.fhtw.swen3.services.dto.GeoCoordinate;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;

import static at.fhtw.swen3.util.JTSUtil.wktToGeometry;
import static org.assertj.core.api.Assertions.assertThat;

class GeoCoordinateMapperTest {

    @Test
    void GIVEN_dto_WHEN_fromDto_THEN_mapped_to_entity() {
        GeoCoordinate geoCoordinate = new GeoCoordinate().lat(1.0).lon(2.0);

        GeoCoordinateEntity entity = GeoCoordinateMapper.INSTANCE.fromDto(geoCoordinate);

        assertThat(entity).isNotNull();
        assertThat(entity.getLocation()).isNotNull();
        assertThat(entity.getLocation().getX()).isEqualTo(1.0);
        assertThat(entity.getLocation().getY()).isEqualTo(2.0);
    }

    @Test
    void GIVEN_entity_WHEN_toDto_THEN_mapped_to_dto() {
        GeoCoordinateEntity geoCoordinateEntity = GeoCoordinateEntity.builder()
                .location((Point) wktToGeometry(1, 3))
                .build();

        GeoCoordinate geoCoordinateDto = GeoCoordinateMapper.INSTANCE.toDto(geoCoordinateEntity);

        assertThat(geoCoordinateDto).isNotNull();
        assertThat(geoCoordinateDto.getLat()).isEqualTo(1.0);
        assertThat(geoCoordinateDto.getLon()).isEqualTo(3.0);
    }
}