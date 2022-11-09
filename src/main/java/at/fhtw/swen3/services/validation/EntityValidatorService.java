package at.fhtw.swen3.services.validation;

import at.fhtw.swen3.persistence.entity.BaseEntity;
import at.fhtw.swen3.services.validation.annotation.ConditionalValidatorService;
import at.fhtw.swen3.services.validation.annotation.ValidateUnderCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;
import java.util.Set;

@Service
public class EntityValidatorService {
    @Autowired
    private ConditionalValidatorService conditionalValidatorService;
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public <T extends BaseEntity> Set<ConstraintViolation<T>> validate(T toValidate) {
        if (conditionalValidatorService.validateCondition(toValidate)) {
            return validator.validate(toValidate, Default.class, ValidateUnderCondition.class);
        }
        return validator.validate(toValidate, Default.class);
    }
}
