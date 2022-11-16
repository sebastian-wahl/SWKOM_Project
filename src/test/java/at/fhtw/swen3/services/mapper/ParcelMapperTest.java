package at.fhtw.swen3.services.mapper;

import at.fhtw.swen3.persistence.entities.*;
import at.fhtw.swen3.services.dto.NewParcelInfo;
import at.fhtw.swen3.services.dto.Parcel;
import at.fhtw.swen3.services.dto.Recipient;
import at.fhtw.swen3.services.dto.TrackingInformation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ParcelMapperTest {

    public static final String RECIPIENT_NAME = "name";
    public static final String SENDER_NAME = "name2";
    public static final String TRACKING_ID = "trackingId";
    public static final float WEIGHT = 1.0F;
    public static final TrackingInformationState TRACKING_INFORMATION_STATE = TrackingInformationState.DELIVERED;
    public static final String VISITED_HOPS = "visitedHops";
    public static final String FUTURE_HOPS = "futureHops";

    @Test
    void GIVEN_dto_WHEN_map_fromDto_THEN_maps_correctly() {
        // GIVEN
        Recipient recipient = new Recipient().name(RECIPIENT_NAME);
        Recipient senderDto = new Recipient().name(SENDER_NAME);
        Parcel parcelDto = new Parcel()
                .recipient(recipient)
                .sender(senderDto)
                .weight(1.0F);

        // WHEN
        ParcelEntity parcel = ParcelMapper.INSTANCE.fromDto(parcelDto);

        // THEN
        assertThat(parcel).isNotNull();
        assertThat(parcel.getRecipient()).isNotNull();
        assertThat(parcel.getRecipient().getName()).isEqualTo(RECIPIENT_NAME);

        assertThat(parcel.getSender()).isNotNull();
        assertThat(parcel.getSender().getName()).isEqualTo(SENDER_NAME);
    }

    @Test
    void GIVEN_business_entity_WHEN_toParcelDto_THEN_maps_correctly() {
        // GIVEN
        ParcelEntity parcel = buildParcel();

        // WHEN
        Parcel parcelDto = ParcelMapper.INSTANCE.toParcelDto(parcel);

        // THEN
        assertThat(parcelDto).isNotNull();

        assertThat(parcelDto.getWeight()).isEqualTo(WEIGHT);

        assertThat(parcelDto.getRecipient()).isNotNull();
        assertThat(parcelDto.getRecipient().getName()).isEqualTo(RECIPIENT_NAME);

        assertThat(parcelDto.getSender()).isNotNull();
        assertThat(parcelDto.getSender().getName()).isEqualTo(SENDER_NAME);
    }

    @Test
    void GIVEN_business_entity_WHEN_toNewParcelInfoDto_THEN_maps_correctly() {
        // GIVEN
        ParcelEntity parcel = buildParcel();

        // WHEN
        NewParcelInfo newParcelInfo = ParcelMapper.INSTANCE.toNewParcelInfoDto(parcel);


        // THEN
        assertThat(newParcelInfo).isNotNull();
        assertThat(newParcelInfo.getTrackingId()).isEqualTo(TRACKING_ID);
    }

    @Test
    void GIVEN_business_entity_WHEN_toTrackingInformationDto_THEN_maps_correctly() {
        // GIVEN
        ParcelEntity parcel = buildParcel();

        // WHEN
        TrackingInformation newParcelInfoDto = ParcelMapper.INSTANCE.toTrackingInformationDto(parcel);


        // THEN
        assertThat(newParcelInfoDto).isNotNull();
        assertThat(newParcelInfoDto.getState()).hasToString(TRACKING_INFORMATION_STATE.toString());

        assertThat(newParcelInfoDto.getVisitedHops()).isNotEmpty().isNotNull();
        assertThat(newParcelInfoDto.getVisitedHops().get(0).getCode()).isEqualTo(VISITED_HOPS);

        assertThat(newParcelInfoDto.getFutureHops()).isNotEmpty().isNotNull();
        assertThat(newParcelInfoDto.getFutureHops().get(0).getCode()).isEqualTo(FUTURE_HOPS);

    }

    private ParcelEntity buildParcel() {
        return ParcelEntity.builder()
                .weight(WEIGHT)
                .recipient(buildRecipient(RECIPIENT_NAME))
                .sender(buildRecipient(SENDER_NAME))
                .trackingId(TRACKING_ID)
                .state(TRACKING_INFORMATION_STATE)
                .visitedHops(buildHopArrivals(VISITED_HOPS))
                .futureHops(buildHopArrivals(FUTURE_HOPS))
                .build();
    }

    private List<HopArrivalEntity> buildHopArrivals(String code) {
        HopArrivalEntity hopArrival = HopArrivalEntity.builder()
                .code(code)
                .build();

        return List.of(hopArrival);
    }

    private RecipientEntity buildRecipient(String name) {
        return RecipientEntity.builder()
                .name(name)
                .build();
    }
}