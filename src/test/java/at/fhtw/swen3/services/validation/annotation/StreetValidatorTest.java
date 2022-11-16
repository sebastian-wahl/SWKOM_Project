package at.fhtw.swen3.services.validation.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {StreetValidator.class})
class StreetValidatorTest {
    public static final String VALID_STREET_NAME = "Hauptstraße 12/12/12";
    public static final String VALID_STREET_NAME2 = "Landstraße 27a";
    public static final String INVALID_STREET_NAME = "!Hauptstras12aße ?";
    @Autowired
    private StreetValidator streetValidator;

    @Test
    void GIVEN_valid_code_WHEN_validate_THEN_return_true() {
        assertThat(streetValidator.isValid(VALID_STREET_NAME, null)).isTrue();
    }

    @Test
    void GIVEN_valid_code_WHEN_validate_THEN_return_true_V2() {
        assertThat(streetValidator.isValid(VALID_STREET_NAME2, null)).isTrue();
    }

    @Test
    void GIVEN_valid_code_WHEN_validate_THEN_return_false() {
        assertThat(streetValidator.isValid(INVALID_STREET_NAME, null)).isFalse();
    }
}