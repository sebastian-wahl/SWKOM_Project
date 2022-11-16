package at.fhtw.swen3.services.validation.annotation;

import at.fhtw.swen3.persistence.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;


public class ConditionalValidator implements ConstraintValidator<ConditionalValidations, BaseEntity> {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Getter
    @Setter
    private String field;
    @Getter
    @Setter
    private String[] contains;

    @Override
    public void initialize(ConditionalValidations constraintAnnotation) {
        field = constraintAnnotation.field();
        contains = constraintAnnotation.contains();
    }

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
            Field privateField = baseEntity.getClass().getDeclaredField(field);
            privateField.setAccessible(true);
            String fieldValue = String.valueOf(privateField.get(baseEntity));

            if (Arrays.stream(contains).map(String::toLowerCase).toList().contains(fieldValue.toLowerCase())) {
                Set<ConstraintViolation<BaseEntity>> violations = validator.validate(baseEntity, ValidateUnderCondition.class);

                violations.forEach(violation -> context.buildConstraintViolationWithTemplate(violation.getMessage())
                        .addPropertyNode(violation.getPropertyPath().toString())
                        .addConstraintViolation());
                return violations.isEmpty();
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
        return true;
    }
}
