package at.fhtw.swen3.persistence.repositories;

import at.fhtw.swen3.persistence.entities.GeoCoordinateEntity;
import at.fhtw.swen3.persistence.entities.HopEntity;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static at.fhtw.swen3.util.JTSUtil.wktToGeometry;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HopRepositoryTest {

    private static final String HOP_CODE = "ABC123";
    private static final String HOP_DESC = "TestDesc";
    private static final String HOP_TYPE = "Test";
    @Autowired
    private HopRepository hopRepository;

    @Test
    void GIVEN_correct_hopecode_WHEN_findFirstByCode_RETURN_hop_with_geocoordinate() {
        // GIVEN
        GeoCoordinateEntity geoCoordinateEntity = GeoCoordinateEntity.builder()
                .location((Point) wktToGeometry(1, 1))
                .build();

        HopEntity hopEntity = HopEntity.builder()
                .hopType(HOP_TYPE)
                .description(HOP_DESC)
                .code(HOP_CODE)
                .locationCoordinates(geoCoordinateEntity)
                .build();


        hopRepository.save(hopEntity);

        // WHEN
        var hopOptional = hopRepository.findFirstByCode(HOP_CODE);

        // THEN
        assertThat(hopOptional).isPresent();
        assertThat(hopOptional.get().getHopType()).isEqualTo(HOP_TYPE);
        assertThat(hopOptional.get().getCode()).isEqualTo(HOP_CODE);
        assertThat(hopOptional.get().getDescription()).isEqualTo(HOP_DESC);
        assertThat(hopOptional.get().getLocationCoordinates()).isNotNull();
    }

}