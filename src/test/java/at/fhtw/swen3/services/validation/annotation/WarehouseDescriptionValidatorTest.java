package at.fhtw.swen3.services.validation.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {WarehouseDescriptionValidator.class})
class WarehouseDescriptionValidatorTest {
    public static final String VALID_WAREHOUSE_DESCR = "4s7PEOjizoGWCuFrkMBGcz hJkVTJ6rß4r";
    public static final String INVALID_WAREHOUSE_DESCR = "!ASkjdnkjsnd";
    @Autowired
    private WarehouseDescriptionValidator warehouseDescriptionValidator;

    @Test
    void GIVEN_valid_code_WHEN_validate_THEN_return_true() {
        assertThat(warehouseDescriptionValidator.isValid(VALID_WAREHOUSE_DESCR, null)).isTrue();
    }

    @Test
    void GIVEN_valid_code_WHEN_validate_THEN_return_false() {
        assertThat(warehouseDescriptionValidator.isValid(INVALID_WAREHOUSE_DESCR, null)).isFalse();
    }
}