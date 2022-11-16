package at.fhtw.swen3.services.validation.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {MinExclusiveValidator.class})
class MinExclusiveValidatorTest {
    public static final Float VALID_VALUE = 0.1f;
    public static final Float INVALID_VALUE = 0.0f;
    @Autowired
    private MinExclusiveValidator minExclusiveValidator;

    @Test
    void GIVEN_valid_code_WHEN_validate_THEN_return_true() {
        assertThat(minExclusiveValidator.isValid(VALID_VALUE, null)).isTrue();
    }

    @Test
    void GIVEN_valid_code_WHEN_validate_THEN_return_false() {
        assertThat(minExclusiveValidator.isValid(INVALID_VALUE, null)).isFalse();
    }


}