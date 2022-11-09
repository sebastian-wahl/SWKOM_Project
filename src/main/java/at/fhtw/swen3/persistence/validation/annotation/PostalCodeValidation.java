package at.fhtw.swen3.persistence.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PostalCodeValidator.class)
public @interface PostalCodeValidation {
    //error message
    String message() default "Invalid postal code: Should have the form \"A-\" + 4 digits (0000-9999). E.g. \"A-1120\"";
    //represents group of constraints
    Class<?>[] groups() default {};
    //represents additional information about annotation
    Class<? extends Payload>[] payload() default {};
}
