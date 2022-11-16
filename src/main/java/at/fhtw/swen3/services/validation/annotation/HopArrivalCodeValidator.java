package at.fhtw.swen3.services.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HopArrivalCodeValidator implements ConstraintValidator<HopArrivalCodeValidation, String> {
    @Override
    public boolean isValid(String code, ConstraintValidatorContext constraintValidatorContext) {
        if (code == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[A-Z]{4}\\d{1,4}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(code);
        return matcher.find();
    }
}
