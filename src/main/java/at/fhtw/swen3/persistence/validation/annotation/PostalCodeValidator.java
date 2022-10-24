package at.fhtw.swen3.persistence.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostalCodeValidator implements ConstraintValidator<PostalCodeValidation, String> {
    @Override
    public boolean isValid(String postalCode, ConstraintValidatorContext cxt) {
        if (postalCode == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("^A-\\d{4}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(postalCode);
        return matcher.find();
    }
}
