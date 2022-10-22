package at.fhtw.swen3.services.mapper;

import at.fhtw.swen3.persistence.entity.HopEntity;
import at.fhtw.swen3.persistence.entity.WarehouseEntity;
import at.fhtw.swen3.persistence.entity.WarehouseNextHopsEntity;
import at.fhtw.swen3.services.dto.GeoCoordinate;
import at.fhtw.swen3.services.dto.Hop;
import at.fhtw.swen3.services.dto.Warehouse;
import at.fhtw.swen3.services.dto.WarehouseNextHops;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WarehouseMapperTest {

    public static final String NEXT_HOP_CODE = "nextHop";
    public static final int TRAVELTIME_MINS = 2;
    public static final int LEVEL = 1;
    public static final String WAREHOUSE_CODE = "warehouse";
    public static final String DESCRIPTION = "description";
    public static final String HOP_TYPE = "hopType";
    public static final String LOCATION_NAME = "locationName";
    public static final int PROCESSING_DELAY_MINS = 2;
    public static final double LAT = 1.0;
    public static final double LON = 2.0;

    @Test
    void GIVEN_dto_WHEN_map_fromDto_THEN_maps_correctly_to_entity() {
        // GIVEN
        WarehouseNextHops warehouseNextHop = new WarehouseNextHops()
                .hop(buildHop())
                .traveltimeMins(TRAVELTIME_MINS);

        Warehouse warehouseDto = buildWarehouse()
                .addNextHopsItem(warehouseNextHop);

        // WHEN
        WarehouseEntity warehouseEntity = WarehouseMapper.INSTANCE.fromDto(warehouseDto);

        // THEN
        assertThat(warehouseEntity).isNotNull();
        assertThat(warehouseEntity.getLevel()).isEqualTo(LEVEL);
        assertThat(warehouseEntity.getHopType()).isEqualTo(HOP_TYPE);
        assertThat(warehouseEntity.getCode()).isEqualTo(WAREHOUSE_CODE);
        assertThat(warehouseEntity.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(warehouseEntity.getProcessingDelayMins()).isEqualTo(PROCESSING_DELAY_MINS);
        assertThat(warehouseEntity.getLocationName()).isEqualTo(LOCATION_NAME);

        assertThat(warehouseEntity.getLocationCoordinates()).isNotNull();
        assertThat(warehouseEntity.getLocationCoordinates().getLat()).isEqualTo(LAT);
        assertThat(warehouseEntity.getLocationCoordinates().getLon()).isEqualTo(LON);

        assertThat(warehouseEntity.getNextHops()).isNotNull().isNotEmpty();
        WarehouseNextHopsEntity warehouseNextHopsEntity = warehouseEntity.getNextHops().get(0);
        assertThat(warehouseNextHopsEntity).isNotNull();
        assertThat(warehouseNextHopsEntity.getTraveltimeMins()).isEqualTo(TRAVELTIME_MINS);
        HopEntity hopEntity = warehouseNextHopsEntity.getHop();
        assertThat(hopEntity).isNotNull();
        assertThat(hopEntity.getCode()).isEqualTo(NEXT_HOP_CODE);
    }

    private Warehouse buildWarehouse() {
        return new Warehouse()
                .code(WAREHOUSE_CODE)
                .description(DESCRIPTION)
                .hopType(HOP_TYPE)
                .locationCoordinates(new GeoCoordinate().lat(LAT).lon(LON))
                .locationName(LOCATION_NAME)
                .processingDelayMins(PROCESSING_DELAY_MINS)
                .level(LEVEL);
    }

    private Hop buildHop() {
        return new Hop()
                .code(NEXT_HOP_CODE)
                .description(DESCRIPTION)
                .hopType(HOP_TYPE)
                .locationCoordinates(new GeoCoordinate().lat(LAT).lon(LON))
                .locationName(LOCATION_NAME)
                .processingDelayMins(PROCESSING_DELAY_MINS);
    }
}