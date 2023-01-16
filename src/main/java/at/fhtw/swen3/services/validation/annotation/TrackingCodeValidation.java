package at.fhtw.swen3.services.validation.annotation;

import at.fhtw.swen3.persistence.entities.ParcelEntity;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = TrackingCodeValidator.class)
public @interface TrackingCodeValidation {
    //error message
    String message() default "Tracking code: Should fit the regex: \"" + ParcelEntity.TRACKING_ID_PATTERN + "\"";

    //represents group of constraints
    Class<?>[] groups() default {};

    //represents additional information about annotation
    Class<? extends Payload>[] payload() default {};
}
