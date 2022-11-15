package at.fhtw.swen3.services.validation.annotation;

import at.fhtw.swen3.persistence.entities.BaseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;

@Service
public class ConditionalValidatorService {
    public boolean validateCondition(BaseEntity baseEntity) {
        try {
            ConditionalValidations annotation = baseEntity.getClass().getAnnotation(ConditionalValidations.class);
            if (annotation != null) {
                String field = annotation.field();
                String[] matchingValues = annotation.contains();

                Field privateField = baseEntity.getClass().getDeclaredField(field);
                privateField.setAccessible(true);
                String fieldValue = String.valueOf(privateField.get(baseEntity));

                if (Arrays.stream(matchingValues).map(String::toLowerCase).toList().contains(fieldValue.toLowerCase())) {
                    return true;
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
        return false;
    }
}
