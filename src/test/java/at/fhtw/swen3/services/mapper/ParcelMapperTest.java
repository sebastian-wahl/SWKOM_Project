package at.fhtw.swen3.services.mapper;

import at.fhtw.swen3.persistence.entity.*;
import at.fhtw.swen3.services.dto.NewParcelInfoDto;
import at.fhtw.swen3.services.dto.ParcelDto;
import at.fhtw.swen3.services.dto.RecipientDto;
import at.fhtw.swen3.services.dto.TrackingInformationDto;
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
        RecipientDto recipientDto = new RecipientDto().name(RECIPIENT_NAME);
        RecipientDto senderDto = new RecipientDto().name(SENDER_NAME);
        ParcelDto parcelDto = new ParcelDto()
                .recipient(recipientDto)
                .sender(senderDto)
                .weight(1.0F);

        NewParcelInfoDto newParcelInfoDto = new NewParcelInfoDto()
                .trackingId(TRACKING_ID);

        TrackingInformationDto trackingInformationDto = new TrackingInformationDto();

        // WHEN
        Parcel parcel = ParcelMapper.INSTANCE.fromDto(parcelDto, newParcelInfoDto, trackingInformationDto);

        // THEN
        assertThat(parcel).isNotNull();
        assertThat(parcel.getRecipient()).isNotNull();
        assertThat(parcel.getRecipient().getName()).isEqualTo(RECIPIENT_NAME);

        assertThat(parcel.getSender()).isNotNull();
        assertThat(parcel.getSender().getName()).isEqualTo(SENDER_NAME);

        assertThat(parcel.getTrackingId()).isEqualTo(TRACKING_ID);
    }

    @Test
    void GIVEN_business_entity_WHEN_toParcelDto_THEN_maps_correctly() {
        // GIVEN
        Parcel parcel = buildParcel();

        // WHEN
        ParcelDto parcelDto = ParcelMapper.INSTANCE.toParcelDto(parcel);

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
        Parcel parcel = buildParcel();

        // WHEN
        NewParcelInfoDto newParcelInfoDto = ParcelMapper.INSTANCE.toNewParcelInfoDto(parcel);


        // THEN
        assertThat(newParcelInfoDto).isNotNull();
        assertThat(newParcelInfoDto.getTrackingId()).isEqualTo(TRACKING_ID);
    }

    @Test
    void GIVEN_business_entity_WHEN_toTrackingInformationDto_THEN_maps_correctly() {
        // GIVEN
        Parcel parcel = buildParcel();

        // WHEN
        TrackingInformationDto newParcelInfoDto = ParcelMapper.INSTANCE.toTrackingInformationDto(parcel);


        // THEN
        assertThat(newParcelInfoDto).isNotNull();
        assertThat(newParcelInfoDto.getState()).hasToString(TRACKING_INFORMATION_STATE.toString());

        assertThat(newParcelInfoDto.getVisitedHops()).isNotEmpty().isNotNull();
        assertThat(newParcelInfoDto.getVisitedHops().get(0).getCode()).isEqualTo(VISITED_HOPS);

        assertThat(newParcelInfoDto.getFutureHops()).isNotEmpty().isNotNull();
        assertThat(newParcelInfoDto.getFutureHops().get(0).getCode()).isEqualTo(FUTURE_HOPS);

    }

    private Parcel buildParcel() {
        return Parcel.builder()
                .weight(WEIGHT)
                .recipient(buildRecipient(RECIPIENT_NAME))
                .sender(buildRecipient(SENDER_NAME))
                .trackingId(TRACKING_ID)
                .state(TRACKING_INFORMATION_STATE)
                .visitedHops(buildHopArrivals(VISITED_HOPS))
                .futureHops(buildHopArrivals(FUTURE_HOPS))
                .build();
    }

    private List<HopArrival> buildHopArrivals(String code) {
        HopArrival hopArrival = HopArrival.builder()
                .code(code)
                .build();

        return List.of(hopArrival);
    }

    private Recipient buildRecipient(String name) {
        return Recipient.builder()
                .name(name)
                .build();
    }
}