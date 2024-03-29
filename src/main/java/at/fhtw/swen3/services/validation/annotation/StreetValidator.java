package at.fhtw.swen3.services.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StreetValidator implements ConstraintValidator<StreetValidation, String> {
    @Override
    public boolean isValid(String street, ConstraintValidatorContext constraintValidatorContext) {
        if (street == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[a-zA-ZßäöüÄÖÜ ]+ \\d(((\\/){0,1}\\w)|(\\d\\w))*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(street);
        return matcher.find();
    }
}
