package at.fhtw.swen3.services.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameCityValidator implements ConstraintValidator<PostalCodeValidation, String> {
    @Override
    public boolean isValid(String city, ConstraintValidatorContext constraintValidatorContext) {
        if (city == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[A-Z]([a-zA-ZßÖÜÄöüä\\- ]*[a-zA-Zß])*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(city);
        return matcher.find();
    }
}
