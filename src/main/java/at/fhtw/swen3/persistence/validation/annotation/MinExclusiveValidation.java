package at.fhtw.swen3.persistence.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = HopArrivalCodeValidator.class)
public @interface MinExclusiveValidation {
    //error message
    String message() default "Parcel weight: Must be greater then 0.0";

    //represents group of constraints
    Class<?>[] groups() default {};

    //represents additional information about annotation
    Class<? extends Payload>[] payload() default {};
}
