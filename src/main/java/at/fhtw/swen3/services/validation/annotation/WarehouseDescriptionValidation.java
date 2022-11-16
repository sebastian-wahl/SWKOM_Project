package at.fhtw.swen3.services.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = WarehouseDescriptionValidator.class)
public @interface WarehouseDescriptionValidation {
    //error message
    String message() default "Description: Only upper and lowercase letters, \"-\" and blanks allowed.";

    //represents group of constraints
    Class<?>[] groups() default {};

    //represents additional information about annotation
    Class<? extends Payload>[] payload() default {};
}
