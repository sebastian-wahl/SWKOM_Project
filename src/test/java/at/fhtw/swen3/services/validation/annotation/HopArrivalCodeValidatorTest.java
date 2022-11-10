package at.fhtw.swen3.services.validation.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {HopArrivalCodeValidator.class})
class HopArrivalCodeValidatorTest {
    public static final String VALID_HOP_ARRIVAL_CODE = "SKGS2780";
    public static final String INVALID_HOP_ARRIVAL_CODE = "Test";
    @Autowired
    private HopArrivalCodeValidator hopArrivalCodeValidator;

    @Test
    void GIVEN_valid_code_WHEN_validate_THEN_return_true() {
        assertThat(hopArrivalCodeValidator.isValid(VALID_HOP_ARRIVAL_CODE, null)).isTrue();
    }

    @Test
    void GIVEN_valid_code_WHEN_validate_THEN_return_false() {
        assertThat(hopArrivalCodeValidator.isValid(INVALID_HOP_ARRIVAL_CODE, null)).isFalse();
    }
}