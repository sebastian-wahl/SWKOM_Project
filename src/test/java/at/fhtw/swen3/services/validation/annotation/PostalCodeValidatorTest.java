package at.fhtw.swen3.services.validation.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {PostalCodeValidator.class})
class PostalCodeValidatorTest {

    public static final String VALID_POSTAL_CODE = "A-1234";
    public static final String INVALID_POSTAL_CODE = "B-9999";
    @Autowired
    private PostalCodeValidator postalCodeValidator;

    @Test
    void GIVEN_valid_code_WHEN_validate_THEN_return_true() {
        assertThat(postalCodeValidator.isValid(VALID_POSTAL_CODE, null)).isTrue();
    }

    @Test
    void GIVEN_valid_code_WHEN_validate_THEN_return_false() {
        assertThat(postalCodeValidator.isValid(INVALID_POSTAL_CODE, null)).isFalse();
    }
}