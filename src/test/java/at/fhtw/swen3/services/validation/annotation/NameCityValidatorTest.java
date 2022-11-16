package at.fhtw.swen3.services.validation.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {NameCityValidator.class})
class NameCityValidatorTest {

    public static final String VALID_NAME = "St Pölten";
    public static final String INVALID_NAME = ".Wien!2 ";
    @Autowired
    private NameCityValidator nameCityValidator;

    @Test
    void GIVEN_valid_code_WHEN_validate_THEN_return_true() {
        assertThat(nameCityValidator.isValid(VALID_NAME, null)).isTrue();
    }

    @Test
    void GIVEN_valid_code_WHEN_validate_THEN_return_false() {
        assertThat(nameCityValidator.isValid(INVALID_NAME, null)).isFalse();
    }
}