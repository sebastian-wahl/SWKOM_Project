package at.fhtw.swen3.services.mapper;

import at.fhtw.swen3.persistence.entities.GeoCoordinateEntity;
import at.fhtw.swen3.persistence.entities.TransferwarehouseEntity;
import at.fhtw.swen3.services.dto.GeoCoordinate;
import at.fhtw.swen3.services.dto.Transferwarehouse;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;

import static at.fhtw.swen3.util.JTSUtil.wktToGeometry;
import static org.assertj.core.api.Assertions.assertThat;

class TransferwarehouseMapperTest {

    public static final String REGION_GEO_JSON = "regionGeoJson";
    public static final String LOGISTIC_PARTNER = "logisticPartner";
    public static final String LOGISTIC_PARTNER_URL = "logisticPartnerUrl";
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";
    public static final String HOP_TYPE = "hopType";
    public static final double LON = 2.0;
    public static final double LAT = 1.0;
    public static final int PROCESSING_DELAYS_MINS = 1;
    public static final String LOCATION_NAME = "locationName";

    @Test
    void GIVEN_dto_WHEN_fromDto_THEN_maps_to_entity() {
        // GIVEN
        Transferwarehouse dto = new Transferwarehouse()
                .regionGeoJson(REGION_GEO_JSON)
                .logisticsPartner(LOGISTIC_PARTNER)
                .logisticsPartnerUrl(LOGISTIC_PARTNER_URL)
                .code(CODE)
                .description(DESCRIPTION)
                .hopType(HOP_TYPE)
                .locationCoordinates(new GeoCoordinate().lat(LAT).lon(LON))
                .processingDelayMins(PROCESSING_DELAYS_MINS)
                .locationName(LOCATION_NAME);

        // WHEN
        TransferwarehouseEntity entity = TransferwarehouseMapper.INSTANCE.fromDto(dto);

        // THEN
        assertThat(entity).isNotNull();
        assertThat(entity.getRegionGeoJson()).isEqualTo(REGION_GEO_JSON);
        assertThat(entity.getLogisticsPartner()).isEqualTo(LOGISTIC_PARTNER);
        assertThat(entity.getLogisticsPartnerUrl()).isEqualTo(LOGISTIC_PARTNER_URL);
        assertThat(entity.getHopType()).isEqualTo(HOP_TYPE);
        assertThat(entity.getCode()).isEqualTo(CODE);
        assertThat(entity.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(entity.getProcessingDelayMins()).isEqualTo(PROCESSING_DELAYS_MINS);
        assertThat(entity.getLocationName()).isEqualTo(LOCATION_NAME);
        assertThat(entity.getLocationCoordinates()).isNotNull();
        assertThat(entity.getLocationCoordinates().getLocation().getX()).isEqualTo(LAT);
        assertThat(entity.getLocationCoordinates().getLocation().getY()).isEqualTo(LON);
    }

    @Test
    void GIVEN_entity_WHEN_toDto_THEN_maps_to_dto() {
        // GIVEN
        TransferwarehouseEntity entity = TransferwarehouseEntity.builder()
                .regionGeoJson(REGION_GEO_JSON)
                .logisticsPartner(LOGISTIC_PARTNER)
                .logisticsPartnerUrl(LOGISTIC_PARTNER_URL)
                .code(CODE)
                .description(DESCRIPTION)
                .hopType(HOP_TYPE)
                .locationCoordinates(GeoCoordinateEntity.builder().location((Point) wktToGeometry(LAT, LON)).build())
                .processingDelayMins(PROCESSING_DELAYS_MINS)
                .locationName(LOCATION_NAME)
                .build();

        // WHEN
        Transferwarehouse dto = TransferwarehouseMapper.INSTANCE.toDto(entity);

        // THEN
        assertThat(dto).isNotNull();
        assertThat(dto.getRegionGeoJson()).isEqualTo(REGION_GEO_JSON);
        assertThat(dto.getLogisticsPartner()).isEqualTo(LOGISTIC_PARTNER);
        assertThat(dto.getLogisticsPartnerUrl()).isEqualTo(LOGISTIC_PARTNER_URL);
        assertThat(dto.getHopType()).isEqualTo(HOP_TYPE);
        assertThat(dto.getCode()).isEqualTo(CODE);
        assertThat(dto.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(dto.getProcessingDelayMins()).isEqualTo(PROCESSING_DELAYS_MINS);
        assertThat(dto.getLocationName()).isEqualTo(LOCATION_NAME);
        assertThat(dto.getLocationCoordinates()).isNotNull();
        assertThat(dto.getLocationCoordinates().getLat()).isEqualTo(LAT);
        assertThat(dto.getLocationCoordinates().getLon()).isEqualTo(LON);
    }
}