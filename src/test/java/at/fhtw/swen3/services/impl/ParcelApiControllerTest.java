package at.fhtw.swen3.services.impl;


import at.fhtw.swen3.services.dto.NewParcelInfo;
import at.fhtw.swen3.services.dto.Parcel;
import at.fhtw.swen3.services.dto.Recipient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ParcelApiControllerTest {

    public static final String CITY = "City";
    public static final String COUNTRY = "Country";
    public static final String POSTAL_CODE = "1200";
    public static final String STREET = "Street";
    public static final String RECIPIENT_NAME = "recipient";
    public static final String SENDER_NAME = "sender";
    public static final float WEIGHT = 1.0F;

    @Autowired
    private ParcelApiController parcelApiController;

    @Test
    void GIVEN_validParcel_WHEN_submitParcel_THEN_200_ok() {
        // GIVEN
        Recipient recipient = createRecipient(RECIPIENT_NAME);
        Recipient senderDto = createRecipient(SENDER_NAME);

        Parcel parcelDto = new Parcel()
                .recipient(recipient)
                .sender(senderDto)
                .weight(WEIGHT);

        // WHEN
        ResponseEntity<NewParcelInfo> response = parcelApiController.submitParcel(parcelDto);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
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