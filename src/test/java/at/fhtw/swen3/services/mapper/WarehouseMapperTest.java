package at.fhtw.swen3.services.mapper;

import at.fhtw.swen3.persistence.entities.HopEntity;
import at.fhtw.swen3.persistence.entities.TruckEntity;
import at.fhtw.swen3.persistence.entities.WarehouseEntity;
import at.fhtw.swen3.persistence.entities.WarehouseNextHopsEntity;
import at.fhtw.swen3.services.dto.*;
import org.junit.jupiter.api.Test;

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
    public static final String CODE = "CODE";

    @Test
    void GIVEN_dto_WHEN_fromDto_THEN_maps_to_entity() {
        // GIVEN
        WarehouseNextHops dto = new WarehouseNextHops()
                .traveltimeMins(TRAVELTIME_MINS)
                .hop(new Truck().code(CODE));

        // WHEN
        WarehouseNextHopsEntity entity = WarehouseMapper.INSTANCE.fromDto(dto, new JpaWarehouseContext());

        // THEN
        assertThat(entity).isNotNull();
        assertThat(entity.getWarehouse()).isNull();
        assertThat(entity.getTraveltimeMins()).isEqualTo(TRAVELTIME_MINS);
        assertThat(entity.getHop()).isNotNull();
        assertThat(entity.getHop().getCode()).isEqualTo(CODE);
    }

    @Test
    void GIVEN_entity_WHEN_toDto_THEN_maps_to_dto() {
        // GIVEN
        WarehouseNextHopsEntity entity = WarehouseNextHopsEntity.builder()
                .traveltimeMins(TRAVELTIME_MINS)
                .hop(TruckEntity.builder().code(CODE).build())
                .build();

        // WHEN
        WarehouseNextHops dto = WarehouseMapper.INSTANCE.toDto(entity);

        // THEN
        assertThat(dto).isNotNull();
        assertThat(dto.getTraveltimeMins()).isEqualTo(TRAVELTIME_MINS);
        assertThat(dto.getHop()).isNotNull();
        assertThat(dto.getHop().getCode()).isEqualTo(CODE);
    }

    @Test
    void GIVEN_dto_WHEN_map_fromDto_THEN_maps_correctly_to_entity() {
        // GIVEN
        WarehouseNextHops warehouseNextHop = new WarehouseNextHops()
                .hop(buildHop())
                .traveltimeMins(TRAVELTIME_MINS);

        Warehouse warehouseDto = buildWarehouse()
                .addNextHopsItem(warehouseNextHop);

        // WHEN
        WarehouseEntity warehouseEntity = WarehouseMapper.INSTANCE.fromDto(warehouseDto, new JpaWarehouseContext());

        // THEN
        assertThat(warehouseEntity).isNotNull();
        assertThat(warehouseEntity.getLevel()).isEqualTo(LEVEL);
        assertThat(warehouseEntity.getHopType()).isEqualTo(HOP_TYPE);
        assertThat(warehouseEntity.getCode()).isEqualTo(WAREHOUSE_CODE);
        assertThat(warehouseEntity.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(warehouseEntity.getProcessingDelayMins()).isEqualTo(PROCESSING_DELAY_MINS);
        assertThat(warehouseEntity.getLocationName()).isEqualTo(LOCATION_NAME);

        assertThat(warehouseEntity.getLocationCoordinates()).isNotNull();
        assertThat(warehouseEntity.getLocationCoordinates().getLocation().getX()).isEqualTo(LAT);
        assertThat(warehouseEntity.getLocationCoordinates().getLocation().getY()).isEqualTo(LON);

        assertThat(warehouseEntity.getNextHops()).isNotNull().isNotEmpty();
        WarehouseNextHopsEntity warehouseNextHopsEntity = warehouseEntity.getNextHops().get(0);
        assertThat(warehouseNextHopsEntity).isNotNull();
        assertThat(warehouseNextHopsEntity.getTraveltimeMins()).isEqualTo(TRAVELTIME_MINS);
        assertThat(warehouseNextHopsEntity.getWarehouse().getCode()).isEqualTo(WAREHOUSE_CODE);
        HopEntity hopEntity = warehouseNextHopsEntity.getHop();
        assertThat(hopEntity).isNotNull();
        assertThat(hopEntity.getCode()).isEqualTo(NEXT_HOP_CODE);
        assertThat(hopEntity.getLocationCoordinates()).isNotNull();
        assertThat(hopEntity.getLocationCoordinates().getLocation().getX()).isEqualTo(LAT);
        assertThat(hopEntity.getLocationCoordinates().getLocation().getY()).isEqualTo(LON);
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