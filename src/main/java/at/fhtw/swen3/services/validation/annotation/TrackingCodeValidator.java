package at.fhtw.swen3.services.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrackingCodeValidator implements ConstraintValidator<PostalCodeValidation, String> {
    @Override
    public boolean isValid(String street, ConstraintValidatorContext constraintValidatorContext) {
        if (street == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[A-Z0-9]{9}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(street);
        return matcher.find();
    }
}
