package at.fhtw.swen3.services.impl;

import at.fhtw.swen3.persistence.entities.HopArrivalEntity;
import at.fhtw.swen3.persistence.entities.ParcelEntity;
import at.fhtw.swen3.persistence.entities.RecipientEntity;
import at.fhtw.swen3.persistence.entities.enums.TrackingInformationState;
import at.fhtw.swen3.persistence.repositories.ParcelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ParcelServiceImplTest {

    public static final String RECIPIENT_NAME = "Recipient";
    public static final String RECIPIENT_CITY = "Recipient City";
    public static final String RECIPIENT_STREET = "Recipient Street 12";
    public static final String PLZ = "A-1010";
    public static final String COUNTRY = "Austria";
    public static final String SENDER_NAME = "Sender";
    public static final String SENDER_CITY = "Sender City";
    public static final String SENDER_STREET = "Sender Street 12";

    @InjectMocks
    private ParcelServiceImpl parcelService;

    @Mock
    private ParcelRepository parcelRepository;

    @Test
    void GIVEN_valid_hopCode_WHEN_reportParcelHop_THEN_move_hopArrival_to_visited() {
        // GIVEN
        final String trackingId = "trackingId";
        final String firstVisitedHopCode = "firstVisitedHop";
        final String firstFutureHopCode = "firstFutureHop";
        final String secondFutureHopCode = "secondFutureHop";

        ParcelEntity parcel = buildParcel();
        parcel.setVisitedHops(buildHopArrivals(firstVisitedHopCode));
        parcel.setFutureHops(buildHopArrivals(firstFutureHopCode, secondFutureHopCode));

        doReturn(Optional.of(parcel)).when(parcelRepository).findFirstByTrackingId(anyString());

        // WHEN
        Optional<ParcelEntity> processedParcel = parcelService.reportParcelHop(trackingId, firstFutureHopCode);

        // THEN
        assertThat(processedParcel).isPresent();
        assertThat(processedParcel.get().getVisitedHops())
                .usingRecursiveComparison()
                .isEqualTo(buildHopArrivals(firstVisitedHopCode,firstFutureHopCode));
        assertThat(processedParcel.get().getFutureHops())
                .usingRecursiveComparison()
                .isEqualTo(buildHopArrivals(secondFutureHopCode));
    }

    private List<HopArrivalEntity> buildHopArrivals(String... codes) {
        return Stream.of(codes)
                .map(code -> HopArrivalEntity.builder().code(code).build())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private ParcelEntity buildParcel() {
        return ParcelEntity.builder()
                .state(TrackingInformationState.PICKUP)
                .weight(1.2f)
                .recipient(buildRecipient())
                .sender(buildSender())
                .build();
    }
    private RecipientEntity buildRecipient() {
        return RecipientEntity.builder()
                .name(RECIPIENT_NAME)
                .city(RECIPIENT_CITY)
                .street(RECIPIENT_STREET)
                .postalCode(PLZ)
                .country(COUNTRY)
                .build();
    }

    private RecipientEntity buildSender() {
        return RecipientEntity.builder()
                .name(SENDER_NAME)
                .city(SENDER_CITY)
                .street(SENDER_STREET)
                .postalCode(PLZ)
                .country(COUNTRY)
                .build();
    }
}