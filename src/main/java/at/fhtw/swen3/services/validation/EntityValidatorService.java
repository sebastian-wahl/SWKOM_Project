package at.fhtw.swen3.services.validation;

import at.fhtw.swen3.persistence.entities.BaseEntity;
import at.fhtw.swen3.services.exception.EntityValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class EntityValidatorService {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public <T extends BaseEntity> void validate(T toValidate) {
        validateGroups(toValidate, Default.class);
    }

    private <T extends BaseEntity> void validateGroups(T toValidate, Class<?>... classes) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(toValidate, classes);

        if (constraintViolations.isEmpty()) {
            return;
        }

        List<String> validationsMessages = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        throw new EntityValidationException(validationsMessages);
    }
}
