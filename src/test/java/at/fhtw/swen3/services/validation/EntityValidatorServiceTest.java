package at.fhtw.swen3.services.validation;

import at.fhtw.swen3.persistence.entities.HopArrivalEntity;
import at.fhtw.swen3.persistence.entities.RecipientEntity;
import at.fhtw.swen3.services.exception.BLEntityValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {EntityValidatorService.class})
class EntityValidatorServiceTest {

    private final RecipientEntity validRecipient = RecipientEntity.builder()
            .name("Test Eins")
            .street("Teststreet 1")
            .postalCode("A-1234")
            .city("Test City")
            .country("Austria")
            .build();
    private final RecipientEntity invalidPostalCodeRecipient = RecipientEntity.builder()
            .name("Test Eins")
            .street("Teststreet 1")
            .postalCode("1234")
            .city("Test City")
            .country("Austria")
            .build();
    private final RecipientEntity invalidPostalCodeNotATRecipient = RecipientEntity.builder()
            .name("Test Eins")
            .street("Teststreet 1")
            .postalCode("DE-12344")
            .city("Test City")
            .country("Germany")
            .build();

    private final HopArrivalEntity validHopArrivalEntity = HopArrivalEntity.builder()
            .dateTime(OffsetDateTime.now())
            .code("FHBX799")
            .build();

    private final HopArrivalEntity invalidHopArrivalEntity = HopArrivalEntity.builder()
            .code("!asda§")
            .build();
    @Autowired
    private EntityValidatorService entityValidatorService;

    @Test
    void GIVEN_valid_recipient_WHEN_validating_THEN_no_exception() {
        assertDoesNotThrow(() -> entityValidatorService.validate(validRecipient));
    }

    @Test
    void GIVEN_invalid_recipient_WHEN_validating_THEN_throw_exception() {
        BLEntityValidationException exception = assertThrows(BLEntityValidationException.class, () -> {
            entityValidatorService.validate(invalidPostalCodeRecipient);
        });

        assertThat(exception.getValidationMessages()).contains("Postal code: Should have the form \"A-\" + 4 digits (0000-9999). E.g. \"A-1120\"");
    }

    @Test
    void GIVEN_invalid_recipient_country_germany_WHEN_validating_THEN_no_exception() {
        assertDoesNotThrow(() -> entityValidatorService.validate(invalidPostalCodeNotATRecipient));
    }

    @Test
    void GIVEN_invalid_recipient_no_conditional_WHEN_validating_THEN_no_exception() {
        assertDoesNotThrow(() -> entityValidatorService.validate(validRecipient));
    }


    @Test
    void GIVEN_valid_hoparrival_WHEN_validating_THEN_no_exception() {
        assertDoesNotThrow(() -> entityValidatorService.validate(validHopArrivalEntity));
    }

    @Test
    void GIVEN_invalid_hoparrival_WHEN_validating_THEN_throw_exception() {
        BLEntityValidationException exception = assertThrows(BLEntityValidationException.class, () -> {
            entityValidatorService.validate(invalidHopArrivalEntity);
        });
        assertThat(exception.getValidationMessages()).contains("Hop arrival code: Must match the regex \"^[A-Z]{4}\\d{1,4}$\"");
    }

}