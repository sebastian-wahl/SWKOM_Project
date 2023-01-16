package at.fhtw.swen3.services.validation.annotation;

import at.fhtw.swen3.persistence.entities.ParcelEntity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrackingCodeValidator implements ConstraintValidator<TrackingCodeValidation, String> {
    @Override
    public boolean isValid(String street, ConstraintValidatorContext constraintValidatorContext) {
        if (street == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(ParcelEntity.TRACKING_ID_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(street);
        return matcher.find();
    }
}
