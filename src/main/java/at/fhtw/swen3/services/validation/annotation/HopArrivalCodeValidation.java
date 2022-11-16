package at.fhtw.swen3.services.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = HopArrivalCodeValidator.class)
public @interface HopArrivalCodeValidation {
    //error message
    String message() default "Hop arrival code: Must match the regex \"^[A-Z]{4}\\\\d{1,4}$\"";

    //represents group of constraints
    Class<?>[] groups() default {};

    //represents additional information about annotation
    Class<? extends Payload>[] payload() default {};
}
