package at.fhtw.swen3.services.validation.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {TrackingCodeValidator.class})
class TrackingCodeValidatorTest {
    public static final String VALID_TRACKING_CODE = "MZJEV0WGD";
    public static final String INVALID_TRACKING_CODE = "!ASkjdnkjsnd";
    @Autowired
    private TrackingCodeValidator trackingCodeValidator;

    @Test
    void GIVEN_valid_code_WHEN_validate_THEN_return_true() {
        assertThat(trackingCodeValidator.isValid(VALID_TRACKING_CODE, null)).isTrue();
    }

    @Test
    void GIVEN_valid_code_WHEN_validate_THEN_return_false() {
        assertThat(trackingCodeValidator.isValid(INVALID_TRACKING_CODE, null)).isFalse();
    }
}