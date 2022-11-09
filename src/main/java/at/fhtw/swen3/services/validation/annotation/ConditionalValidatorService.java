package at.fhtw.swen3.services.validation.annotation;

import at.fhtw.swen3.persistence.entity.BaseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;

@Service
public class ConditionalValidatorService {
    public boolean validateCondition(BaseEntity baseEntity) {
        try {
            ConditionalValidation annotation = baseEntity.getClass().getAnnotation(ConditionalValidation.class);
            if (annotation != null) {
                String field = annotation.field();
                String[] matchingValues = annotation.contains();

                Field privateField = baseEntity.getClass().getDeclaredField(field);
                privateField.setAccessible(true);
                String fieldValue = String.valueOf(privateField.get(baseEntity));

                if (Arrays.asList(matchingValues).contains(fieldValue)) {
                    return true;
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
        return false;
    }
}
