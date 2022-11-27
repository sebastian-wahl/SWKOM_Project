package at.fhtw.swen3.services.mapper;


import at.fhtw.swen3.persistence.entities.GeoCoordinateEntity;
import at.fhtw.swen3.persistence.entities.HopEntity;
import at.fhtw.swen3.persistence.entities.TruckEntity;
import at.fhtw.swen3.services.dto.GeoCoordinate;
import at.fhtw.swen3.services.dto.Hop;
import at.fhtw.swen3.services.dto.Truck;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;

import static at.fhtw.swen3.util.JTSUtil.wktToGeometry;
import static org.assertj.core.api.Assertions.assertThat;

class HopMapperTest {

    public static final int PROCESSING_DELAY_MINS = 4;
    public static final String LOCATION_NAME = "locationName";
    public static final double LAT = 10.0;
    public static final double LON = 20.0;
    public static final String DESCRIPTION = "description";
    public static final String CODE = "code";
    public static final String HOP_TYPE = "hopType";
    public static final String REGION_GEO_JSON = "regionGeoJson";
    public static final String NUMBER_PLATE = "numberPlate";

    @Test
    void GIVEN_truckDto_WHEN_fromDto_THEN_maps_to_truckEntity() {
        // GIVEN
        Truck truck = new Truck()
                .hopType(HOP_TYPE)
                .code(CODE)
                .description(DESCRIPTION)
                .locationCoordinates(new GeoCoordinate().lat(LAT).lon(LON))
                .locationName(LOCATION_NAME)
                .processingDelayMins(PROCESSING_DELAY_MINS)
                .regionGeoJson(REGION_GEO_JSON)
                .numberPlate(NUMBER_PLATE);

        // WHEN
        HopEntity hopEntity = HopMapper.INSTANCE.fromDto(truck);

        // THEN
        assertThat(hopEntity).isNotNull();
        assertThat(hopEntity.getHopType()).isEqualTo(HOP_TYPE);
        assertThat(hopEntity.getCode()).isEqualTo(CODE);
        assertThat(hopEntity.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(hopEntity.getProcessingDelayMins()).isEqualTo(PROCESSING_DELAY_MINS);
        assertThat(hopEntity.getLocationName()).isEqualTo(LOCATION_NAME);
        assertThat(hopEntity.getLocationCoordinates()).isNotNull();
        assertThat(hopEntity.getLocationCoordinates().getLat()).isEqualTo(LAT);
        assertThat(hopEntity.getLocationCoordinates().getLon()).isEqualTo(LON);

        assertThat(hopEntity).isInstanceOf(TruckEntity.class);
        assertThat(((TruckEntity) hopEntity).getNumberPlate()).isEqualTo(NUMBER_PLATE);
        assertThat(((TruckEntity) hopEntity).getRegionGeoJson()).isEqualTo(REGION_GEO_JSON);
    }

    @Test
    void GIVEN_truckEntity_WHEN_toDto_THEN_maps_to_truckDto() {
        // GIVEN
        TruckEntity truckEntity = TruckEntity.builder()
                .hopType(HOP_TYPE)
                .code(CODE)
                .description(DESCRIPTION)
                .processingDelayMins(PROCESSING_DELAY_MINS)
                .locationName(LOCATION_NAME)
                .locationCoordinates(GeoCoordinateEntity.builder().lat(LAT).lon(LON).build())
                .regionGeoJson(REGION_GEO_JSON)
                .numberPlate(NUMBER_PLATE)
                .build();

        // WHEN
        Hop hop = HopMapper.INSTANCE.toDto(truckEntity);

        // THEN
        assertThat(hop).isNotNull();
        assertThat(hop.getHopType()).isEqualTo(HOP_TYPE);
        assertThat(hop.getCode()).isEqualTo(CODE);
        assertThat(hop.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(hop.getProcessingDelayMins()).isEqualTo(PROCESSING_DELAY_MINS);
        assertThat(hop.getLocationName()).isEqualTo(LOCATION_NAME);
        assertThat(hop.getLocationCoordinates()).isNotNull();
        assertThat(hop.getLocationCoordinates().getLat()).isEqualTo(LAT);
        assertThat(hop.getLocationCoordinates().getLon()).isEqualTo(LON);

        assertThat(hop).isInstanceOf(Truck.class);
        assertThat(((Truck) hop).getNumberPlate()).isEqualTo(NUMBER_PLATE);
        assertThat(((Truck) hop).getRegionGeoJson()).isEqualTo(REGION_GEO_JSON);
    }

    @Test
    void GIVEN_hopDto_WHEN_fromDto_THEN_maps_to_hopEntity() {
        // GIVEN
        Hop truck = new Hop()
                .hopType(HOP_TYPE)
                .code(CODE)
                .description(DESCRIPTION)
                .locationCoordinates(new GeoCoordinate().lat(LAT).lon(LON))
                .locationName(LOCATION_NAME)
                .processingDelayMins(PROCESSING_DELAY_MINS);

        // WHEN
        HopEntity hopEntity = HopMapper.INSTANCE.fromDto(truck);

        // THEN
        assertThat(hopEntity).isNotNull();
        assertThat(hopEntity.getHopType()).isEqualTo(HOP_TYPE);
        assertThat(hopEntity.getCode()).isEqualTo(CODE);
        assertThat(hopEntity.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(hopEntity.getProcessingDelayMins()).isEqualTo(PROCESSING_DELAY_MINS);
        assertThat(hopEntity.getLocationName()).isEqualTo(LOCATION_NAME);
        assertThat(hopEntity.getLocationCoordinates()).isNotNull();
        assertThat(hopEntity.getLocationCoordinates().getLat()).isEqualTo(LAT);
        assertThat(hopEntity.getLocationCoordinates().getLon()).isEqualTo(LON);
    }

    @Test
    void GIVEN_hopEntity_WHEN_toDto_THEN_maps_to_hopDto() {
        // GIVEN
        HopEntity truckEntity = HopEntity.builder()
                .hopType(HOP_TYPE)
                .code(CODE)
                .description(DESCRIPTION)
                .processingDelayMins(PROCESSING_DELAY_MINS)
                .locationName(LOCATION_NAME)
                .locationCoordinates(GeoCoordinateEntity.builder().lat(LAT).lon(LON).build())
                .build();

        // WHEN
        Hop hop = HopMapper.INSTANCE.toDto(truckEntity);

        // THEN
        assertThat(hop).isNotNull();
        assertThat(hop.getHopType()).isEqualTo(HOP_TYPE);
        assertThat(hop.getCode()).isEqualTo(CODE);
        assertThat(hop.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(hop.getProcessingDelayMins()).isEqualTo(PROCESSING_DELAY_MINS);
        assertThat(hop.getLocationName()).isEqualTo(LOCATION_NAME);
        assertThat(hop.getLocationCoordinates()).isNotNull();
        assertThat(hop.getLocationCoordinates().getLat()).isEqualTo(LAT);
        assertThat(hop.getLocationCoordinates().getLon()).isEqualTo(LON);
    }
}