package at.fhtw.swen3.persistence.repositories;

import at.fhtw.swen3.persistence.entities.HopArrivalEntity;
import at.fhtw.swen3.persistence.entities.ParcelEntity;
import at.fhtw.swen3.persistence.entities.RecipientEntity;
import at.fhtw.swen3.persistence.entities.enums.TrackingInformationState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ParcelRepositoryTest {

    private static final String TRACKING_ID_1 = "PYJRB4HZ6";
    private static final RecipientEntity RECIPIENT_1 = RecipientEntity.builder().name("Test Name").build();
    private static final RecipientEntity RECIPIENT_2 = RecipientEntity.builder().name("Test Name").build();
    private static final String HOP_CODE = "ABCD12";
    private static final String HOP_DESC = "Test";
    @Autowired
    private ParcelRepository parcelRepository;

    @Test
    void GIVEN_correct_trackingId_WHEN_findFirstByTrackingId_RETURN_parcel_with_hop_arrival() {
        // GIVEN
        OffsetDateTime dateNow = OffsetDateTime.now();

        HopArrivalEntity hopArrival = HopArrivalEntity.builder()
                .dateTime(dateNow)
                .code(HOP_CODE)
                .description(HOP_DESC)
                .build();

        ParcelEntity parcelEntity = ParcelEntity.builder().state(TrackingInformationState.TRANSFERRED).weight(1f).sender(RECIPIENT_1).recipient(RECIPIENT_2).trackingId(TRACKING_ID_1)
                .visitedHop(hopArrival)
                .build();


        parcelRepository.save(parcelEntity);

        // WHEN
        var optionalParcel = parcelRepository.findFirstByTrackingId(TRACKING_ID_1);

        // THEN
        assertThat(optionalParcel).isPresent();
        assertThat(optionalParcel.get().getState()).isEqualTo(TrackingInformationState.TRANSFERRED);
        assertThat(optionalParcel.get().getTrackingId()).isEqualTo(TRACKING_ID_1);
        assertThat(optionalParcel.get().getSender()).isNotNull();
        assertThat(optionalParcel.get().getSender().getName()).isEqualTo(RECIPIENT_1.getName());
        assertThat(optionalParcel.get().getRecipient()).isNotNull();
        assertThat(optionalParcel.get().getRecipient().getName()).isEqualTo(RECIPIENT_2.getName());
        assertThat(optionalParcel.get().getVisitedHops()).hasSize(1);
        assertThat(optionalParcel.get().getVisitedHops().get(0).getCode()).isEqualTo(HOP_CODE);
        assertThat(optionalParcel.get().getVisitedHops().get(0).getDescription()).isEqualTo(HOP_DESC);
    }


}