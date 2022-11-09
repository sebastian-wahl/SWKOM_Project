package at.fhtw.swen3.services.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = TrackingCodeValidator.class)
public @interface TrackingCodeValidation {
    //error message
    String message() default "Invalid tracking code: Should fit the regex: \"^[A-Z0-9]{9}$\"";
    //represents group of constraints
    Class<?>[] groups() default {};
    //represents additional information about annotation
    Class<? extends Payload>[] payload() default {};
}
