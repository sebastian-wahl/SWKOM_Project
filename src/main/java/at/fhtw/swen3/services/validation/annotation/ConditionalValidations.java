package at.fhtw.swen3.services.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ConditionalValidator.class)
public @interface ConditionalValidations {

    String message() default "Austria-specific validations violated";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String field();

    String[] contains();
}
