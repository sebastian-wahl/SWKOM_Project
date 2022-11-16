package at.fhtw.swen3.services.validation.annotation;

import at.fhtw.swen3.persistence.entities.RecipientEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

//@SpringBootTest(classes = {ConditionalValidator.class})
@ExtendWith(MockitoExtension.class)
class ConditionalValidatorTest {

    private final RecipientEntity recipientWithoutCountryValid = RecipientEntity.builder()
            .postalCode("D-13200")
            .build();
    private final RecipientEntity recipientWithCountryValid = RecipientEntity.builder()
            .name("Name")
            .city("Test City")
            .street("Test Street 12")
            .country("Austria")
            .postalCode("A-1200")
            .build();
    private final RecipientEntity recipientWithCountryInvalid = RecipientEntity.builder()
            .country("Austria")
            .postalCode("D-13200")
            .build();
    //@Autowired
    private final ConditionalValidator conditionalValidator = new ConditionalValidator();
    @Mock
    private ConstraintValidatorContext context;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node;

    @BeforeEach
    void setUp() {
        conditionalValidator.setField("country");
        conditionalValidator.setContains(new String[]{"Austria", "Österreich"});
        lenient().doReturn(builder).when(context).buildConstraintViolationWithTemplate(any());
        lenient().doReturn(node).when(builder).addPropertyNode(any());
    }

    @Test
    void GIVEN_valid_recipient_country_austria_WHEN_validate_THEN_return_true() {
        assertThat(conditionalValidator.isValid(recipientWithCountryValid, context)).isTrue();
    }

    @Test
    void GIVEN_invalid_recipient_country_austria_WHEN_validate_THEN_return_false() {
        assertThat(conditionalValidator.isValid(recipientWithCountryInvalid, context)).isFalse();
    }

    @Test
    void GIVEN_invalid_recipient_country_empty_WHEN_validate_THEN_return_true() {
        assertThat(conditionalValidator.isValid(recipientWithoutCountryValid, context)).isTrue();
    }
}