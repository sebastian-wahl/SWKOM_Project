package at.fhtw.swen3.controller.rest;

import at.fhtw.swen3.controller.ParcelApi;
import at.fhtw.swen3.persistence.entities.HopArrivalEntity;
import at.fhtw.swen3.persistence.entities.ParcelEntity;
import at.fhtw.swen3.persistence.entities.RecipientEntity;
import at.fhtw.swen3.services.ParcelService;
import at.fhtw.swen3.services.dto.NewParcelInfo;
import at.fhtw.swen3.services.dto.Parcel;
import at.fhtw.swen3.services.dto.Recipient;
import at.fhtw.swen3.services.dto.TrackingInformation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ValidationException;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class ParcelApiControllerTest {

    public static final String CITY = "City";
    public static final String COUNTRY = "Country";
    public static final String POSTAL_CODE = "1200";
    public static final String STREET = "Street";
    public static final String RECIPIENT_NAME = "recipient";
    public static final String SENDER_NAME = "sender";
    public static final float WEIGHT = 1.0F;
    public static final String VALID_TRACKING_ID = "ABCDE6789";
    public static final String INVALID_TRACKING_ID = "invalidTrackingId";
    public static final String VALID_HOP_CODE = "ABCD1234";
    public static final String INVALID_HOP_CODE = "invalidCode";

    @MockBean(name = "parcelService")
    private ParcelService parcelService;

    @Autowired
    private ParcelApi parcelApiController;

    @Test
    void GIVEN_valid_trackingId_WHEN_reportParcelDelivery_THEN_200_ok() {
        ResponseEntity<Void> response = parcelApiController.reportParcelDelivery(VALID_TRACKING_ID);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void GIVEN_invalid_trackingId_WHEN_reportParcelDelivery_THEN_ValidationException() {
        assertThatCode(() -> parcelApiController.reportParcelDelivery(INVALID_TRACKING_ID))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void GIVEN_valid_input_WHEN_reportParcelHop_THEN_200_ok() {
        ResponseEntity<Void> response = parcelApiController.reportParcelHop(VALID_TRACKING_ID, VALID_HOP_CODE);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void GIVEN_invalid_code_WHEN_reportParcelHop_THEN_ValidationException() {
        assertThatCode(() -> parcelApiController.reportParcelHop(VALID_TRACKING_ID, INVALID_HOP_CODE))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("code");
    }

    @Test
    void GIVEN_valid_parcel_WHEN_submitParcel_THEN_201_created() {
        ParcelEntity parcelEntity = createParcelEntity();
        doReturn(parcelEntity).when(parcelService).submitParcel(any());
        Parcel parcelDto = createParcel();

        ResponseEntity<NewParcelInfo> response = parcelApiController.submitParcel(parcelDto);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void GIVEN_valid_trackingId_WHEN_trackParcel_THEN_200_ok() {
        Optional<ParcelEntity> parcelEntityOptional = Optional.of(createParcelEntity());
        doReturn(parcelEntityOptional).when(parcelService).trackParcel(any());

        ResponseEntity<TrackingInformation> response = parcelApiController.trackParcel(VALID_TRACKING_ID);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    void GIVEN_invalid_trackingId_WHEN_trackParcel_THEN_ValidationException() {
        assertThatCode(() -> parcelApiController.trackParcel(INVALID_TRACKING_ID))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("trackingId");
    }

    @Test
    void GIVEN_valid_trackingId_WHEN_transitionParcel_THEN_200_ok() {
        ParcelEntity parcelEntity = createParcelEntity();
        Parcel parcel = createParcel();
        NewParcelInfo newParcelInfo = mockNewParcelInfo();
        doReturn(parcelEntity).when(parcelService).transitionParcel(any());


        ResponseEntity<NewParcelInfo> response = parcelApiController.transitionParcel(VALID_TRACKING_ID, parcel);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(newParcelInfo);
    }

    @Test
    void GIVEN_invalid_trackingId_WHEN_transitionParcel_THEN_ValidationException() {
        Parcel parcel = createParcel();
        assertThatCode(() -> parcelApiController.transitionParcel(INVALID_TRACKING_ID, parcel))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("trackingId");
    }


    private Recipient createRecipient(String recipientName) {
        return new Recipient()
                .name(recipientName)
                .city(CITY)
                .country(COUNTRY)
                .postalCode(POSTAL_CODE)
                .street(STREET);
    }

    private Parcel createParcel() {
        return new Parcel()
                .recipient(createRecipient(RECIPIENT_NAME))
                .sender(createRecipient(SENDER_NAME))
                .weight(WEIGHT);
    }

    private ParcelEntity createParcelEntity() {
        HopArrivalEntity hopArrival = HopArrivalEntity.builder()
                .code("code")
                .description("description")
                .dateTime(OffsetDateTime.now())
                .build();

        return ParcelEntity.builder()
                .recipient(createRecipientEntity(RECIPIENT_NAME))
                .sender(createRecipientEntity(SENDER_NAME))
                .trackingId(VALID_TRACKING_ID)
                .visitedHop(hopArrival)
                .futureHop(hopArrival)
                .weight(WEIGHT)
                .build();
    }

    private RecipientEntity createRecipientEntity(String recipientName) {
        return RecipientEntity.builder()
                .name(recipientName)
                .city(CITY)
                .country(COUNTRY)
                .postalCode(POSTAL_CODE)
                .street(STREET)
                .build();
    }

    private NewParcelInfo mockNewParcelInfo() {
        return new NewParcelInfo()
                .trackingId(VALID_TRACKING_ID);
    }
}