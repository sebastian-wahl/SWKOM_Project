package at.fhtw.swen3.services.mapper;

import at.fhtw.swen3.persistence.entities.RecipientEntity;
import at.fhtw.swen3.services.dto.Recipient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecipientMapperTest {

    public static final String STREET = "street";
    public static final String POSTAL_CODE = "A-1234";
    public static final String COUNTRY = "country";
    public static final String CITY = "city";
    public static final String NAME = "name";

    @Test
    void GIVEN_dto_WHEN_fromDto_THEN_maps_to_entity() {
        // GIVEN
        Recipient dto = new Recipient()
                .name(NAME)
                .city(CITY)
                .country(COUNTRY)
                .postalCode(POSTAL_CODE)
                .street(STREET);

        // WHEN
        RecipientEntity entity = RecipientMapper.INSTANCE.fromDto(dto);

        // THEN
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo(NAME);
        assertThat(entity.getStreet()).isEqualTo(STREET);
        assertThat(entity.getPostalCode()).isEqualTo(POSTAL_CODE);
        assertThat(entity.getCity()).isEqualTo(CITY);
        assertThat(entity.getCountry()).isEqualTo(COUNTRY);
    }

    @Test
    void GIVEN_entity_WHEN_toDto_THEN_maps_to_dto() {
        // GIVEN
        RecipientEntity entity = RecipientEntity.builder()
                .name(NAME)
                .city(CITY)
                .country(COUNTRY)
                .postalCode(POSTAL_CODE)
                .street(STREET)
                .build();

        // WHEN
        Recipient dto = RecipientMapper.INSTANCE.toDto(entity);

        // THEN
        assertThat(dto).isNotNull();
        assertThat(dto.getName()).isEqualTo(NAME);
        assertThat(dto.getStreet()).isEqualTo(STREET);
        assertThat(dto.getPostalCode()).isEqualTo(POSTAL_CODE);
        assertThat(dto.getCity()).isEqualTo(CITY);
        assertThat(dto.getCountry()).isEqualTo(COUNTRY);
    }
}