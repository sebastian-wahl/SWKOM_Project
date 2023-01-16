package at.fhtw.swen3.services.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WarehouseDescriptionValidator implements ConstraintValidator<WarehouseDescriptionValidation, String> {
    @Override
    public boolean isValid(String description, ConstraintValidatorContext constraintValidatorContext) {
        if (description == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[A-Za-zßäöüÄÖÜ \\-0-9]+$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(description);
        return matcher.find();
    }
}
