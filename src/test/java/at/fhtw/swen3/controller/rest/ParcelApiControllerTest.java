package at.fhtw.swen3.controller.rest;

import at.fhtw.swen3.controller.ParcelApi;
import at.fhtw.swen3.services.dto.NewParcelInfo;
import at.fhtw.swen3.services.dto.Parcel;
import at.fhtw.swen3.services.dto.Recipient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ParcelApiControllerTest {

    public static final String CITY = "City";
    public static final String COUNTRY = "Country";
    public static final String POSTAL_CODE = "1200";
    public static final String STREET = "Street";
    public static final String RECIPIENT_NAME = "recipient";
    public static final String SENDER_NAME = "sender";
    public static final float POSITIVE_WEIGHT = 1.0F;
    public static final float NEGATIVE_WEIGHT = -1.0F;

    @Autowired
    private ParcelApi parcelApiController;

    @Test
    void GIVEN_valid_parcel_WHEN_submitParcel_THEN_200_ok() {
        // GIVEN
        Parcel parcelDto = new Parcel()
                .recipient(createRecipient(RECIPIENT_NAME))
                .sender(createRecipient(SENDER_NAME))
                .weight(POSITIVE_WEIGHT);

        // WHEN
        ResponseEntity<NewParcelInfo> response = parcelApiController.submitParcel(parcelDto);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void GIVEN_invalid_parcel_WHEN_submitParcel_THEN_400_bad_request() {
        // GIVEN
        Parcel parcelDto = new Parcel()
                .recipient(createRecipient(RECIPIENT_NAME))
                .sender(createRecipient(SENDER_NAME))
                .weight(NEGATIVE_WEIGHT);

        // WHEN
        Throwable exception = catchThrowable(() -> {
            parcelApiController.submitParcel(parcelDto);
        });

        // THEN
        assertThat(exception)
                .isNotNull()
                .isInstanceOf(ResponseStatusException.class);
        assertThat(((ResponseStatusException)exception).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private Recipient createRecipient(String recipientName) {
        return new Recipient()
                .name(recipientName)
                .city(CITY)
                .country(COUNTRY)
                .postalCode(POSTAL_CODE)
                .street(STREET);
    }
}