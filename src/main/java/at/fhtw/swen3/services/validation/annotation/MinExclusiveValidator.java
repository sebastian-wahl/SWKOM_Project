package at.fhtw.swen3.persistence.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinExclusiveValidator implements ConstraintValidator<MinExclusiveValidation, Float> {
    @Override
    public boolean isValid(Float a, ConstraintValidatorContext constraintValidatorContext) {
        return a != null && a > 0.0f;
    }
}
