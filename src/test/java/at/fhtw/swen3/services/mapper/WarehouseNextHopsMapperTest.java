package at.fhtw.swen3.services.mapper;

import at.fhtw.swen3.persistence.entities.HopEntity;
import at.fhtw.swen3.persistence.entities.WarehouseNextHopsEntity;
import at.fhtw.swen3.services.dto.Hop;
import at.fhtw.swen3.services.dto.WarehouseNextHops;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WarehouseNextHopsMapperTest {

    public static final String CODE = "CODE";
    public static final int TRAVELTIME_MINS = 24;

    @Test
    void GIVEN_dto_WHEN_fromDto_THEN_maps_to_entity() {
        // GIVEN
        WarehouseNextHops dto = new WarehouseNextHops()
                .traveltimeMins(TRAVELTIME_MINS)
                .hop(new Hop().code(CODE));

        // WHEN
        WarehouseNextHopsEntity entity = WarehouseNextHopsMapper.INSTANCE.fromDto(dto);

        // THEN
        assertThat(entity).isNotNull();
        assertThat(entity.getTraveltimeMins()).isEqualTo(TRAVELTIME_MINS);
        assertThat(entity.getHop()).isNotNull();
        assertThat(entity.getHop().getCode()).isEqualTo(CODE);
    }

    @Test
    void GIVEN_entity_WHEN_toDto_THEN_maps_to_dto() {
        // GIVEN
        WarehouseNextHopsEntity entity = WarehouseNextHopsEntity.builder()
                .traveltimeMins(TRAVELTIME_MINS)
                .hop(HopEntity.builder().code(CODE).build())
                .build();

        // WHEN
        WarehouseNextHops dto = WarehouseNextHopsMapper.INSTANCE.toDto(entity);

        // THEN
        assertThat(dto).isNotNull();
        assertThat(dto.getTraveltimeMins()).isEqualTo(TRAVELTIME_MINS);
        assertThat(dto.getHop()).isNotNull();
        assertThat(dto.getHop().getCode()).isEqualTo(CODE);
    }
}