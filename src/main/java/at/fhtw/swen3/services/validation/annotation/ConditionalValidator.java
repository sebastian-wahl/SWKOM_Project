package at.fhtw.swen3.services.validation.annotation;

import at.fhtw.swen3.persistence.entities.BaseEntity;

import javax.validation.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;


public class ConditionalValidator implements ConstraintValidator<ConditionalValidations, BaseEntity> {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    /**
     * Returns true if no conditional validation is necessary or the conditional validation did not fail.
     * Returns false if the conditional validation failed.
     *
     * @param baseEntity
     * @param context
     * @return
     */
    @Override
    public boolean isValid(BaseEntity baseEntity, ConstraintValidatorContext context) {
        try {
            ConditionalValidations annotation = baseEntity.getClass().getAnnotation(ConditionalValidations.class);
            if (annotation != null) {
                String field = annotation.field();
                String[] matchingValues = annotation.contains();

                Field privateField = baseEntity.getClass().getDeclaredField(field);
                privateField.setAccessible(true);
                String fieldValue = String.valueOf(privateField.get(baseEntity));

                if (Arrays.stream(matchingValues).map(String::toLowerCase).toList().contains(fieldValue.toLowerCase())) {
                    Set<ConstraintViolation<BaseEntity>> violations = validator.validate(baseEntity, ValidateUnderCondition.class);

                    violations.forEach(violation -> context.buildConstraintViolationWithTemplate(violation.getMessage())
                            .addPropertyNode(violation.getPropertyPath().toString())
                            .addConstraintViolation());
                    return violations.isEmpty();
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return true;
        }
        return true;
    }
}
