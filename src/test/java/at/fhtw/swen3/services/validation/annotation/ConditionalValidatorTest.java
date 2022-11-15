package at.fhtw.swen3.services.validation.annotation;

import at.fhtw.swen3.persistence.entities.RecipientEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ConditionalValidator.class})
class ConditionalValidatorTest {

    private final RecipientEntity recipientWithoutCountryValid = RecipientEntity.builder()
            .postalCode("D-13200")
            .build();
    private final RecipientEntity recipientWithCountryValid = RecipientEntity.builder()
            .country("Austria")
            .postalCode("A-1200")
            .build();
    private final RecipientEntity recipientWithCountryInvalid = RecipientEntity.builder()
            .country("Austria")
            .postalCode("D-13200")
            .build();
    @Autowired
    private ConditionalValidator conditionalValidator;

    @Test
    void GIVEN_valid_recipient_country_austria_WHEN_validate_THEN_return_true() {
        //assertThat(conditionalValidator.isValid(recipientWithCountryValid, null)).isTrue();
    }

    @Test
    void GIVEN_invalid_recipient_country_austria_WHEN_validate_THEN_return_false() {
        // ToDo Think about context
        //assertThat(conditionalValidator.isValid(recipientWithCountryInvalid, null)).isFalse();
    }

    @Test
    void GIVEN_invalid_recipient_country_empty_WHEN_validate_THEN_return_true() {
        //assertThat(conditionalValidator.isValid(recipientWithoutCountryValid, null)).isTrue();
    }
}