package at.fhtw.swen3.services.validation;

import at.fhtw.swen3.persistence.entity.BaseEntity;
import at.fhtw.swen3.services.exception.ValidationException;
import at.fhtw.swen3.services.validation.annotation.ConditionalValidatorService;
import at.fhtw.swen3.services.validation.annotation.ValidateUnderCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;
import java.util.List;
import java.util.Set;

@Service
public class EntityValidatorService {
    @Autowired
    private ConditionalValidatorService conditionalValidatorService;
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public <T extends BaseEntity> void validate(T toValidate) throws ValidationException {
        if (conditionalValidatorService.validateCondition(toValidate)) {
            validateGroups(toValidate, Default.class, ValidateUnderCondition.class);
        }
        validateGroups(toValidate, Default.class);
    }

    private <T extends BaseEntity> void validateGroups(T toValidate, Class<?>... classes) throws ValidationException {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(toValidate, classes);

        if (constraintViolations.isEmpty()) {
            return;
        }

        List<String> validationsMessages = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        throw new ValidationException(validationsMessages);
    }
}
