package at.fhtw.swen3.services.impl;

import at.fhtw.swen3.gps.service.GeoEncodingService;
import at.fhtw.swen3.gps.service.models.GeoEncodingCoordinate;
import at.fhtw.swen3.persistence.entities.*;
import at.fhtw.swen3.persistence.repositories.HopRepository;
import at.fhtw.swen3.persistence.repositories.ParcelRepository;
import at.fhtw.swen3.persistence.repositories.TruckRepository;
import at.fhtw.swen3.persistence.repositories.WarehouseNextHopsRepository;
import at.fhtw.swen3.services.exception.blexception.BLSubmitParcelAddressIncorrect;
import at.fhtw.swen3.services.validation.EntityValidatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static at.fhtw.swen3.persistence.entities.enums.TrackingInformationState.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    public static final String SENDER_NUMBER_PLATE = "5678";
    public static final String RECIPIENT_NUMBER_PLATE = "1234";

    @InjectMocks
    private ParcelServiceImpl parcelService;

    @Mock
    private EntityValidatorService entityValidatorService;

    @Mock
    private ParcelRepository parcelRepository;

    @Mock
    private HopRepository hopRepository;

    @Mock
    private WarehouseNextHopsRepository warehouseNextHopsRepository;

    @Mock
    private GeoEncodingService geoEncodingService;

    @Mock
    private TruckRepository truckRepository;

    @Test
    void GIVEN_valid_hopCode_WHEN_reportParcelHop_THEN_move_hopArrival_to_visited() {
        // GIVEN
        final String trackingId = "trackingId";
        final String firstVisitedHopCode = "firstVisitedHop";
        final String firstFutureHopCode = "firstFutureHop";
        final String secondFutureHopCode = "secondFutureHop";

        ParcelEntity parcel = buildParcel(trackingId);
        parcel.setVisitedHops(buildHopArrivals(firstVisitedHopCode));
        parcel.setFutureHops(buildHopArrivals(firstFutureHopCode, secondFutureHopCode));

        doReturn(Optional.of(parcel)).when(parcelRepository).findFirstByTrackingId(trackingId);

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
        assertThat(processedParcel.get().getState()).isEqualTo(INTRANSPORT);
    }

    @Test
    void GIVEN_last_truck_WHEN_reportParcelHop_THEN_parcel_in_state_inTruckDelivery() {
        // GIVEN
        final String trackingId = "trackingId";
        final String truckHopCode = "lastTruck";
        ParcelEntity parcel = buildParcel(trackingId);
        parcel.setVisitedHops(new ArrayList<>());
        parcel.setFutureHops(buildHopArrivals(truckHopCode));

        doReturn(Optional.of(parcel)).when(parcelRepository).findFirstByTrackingId(trackingId);

        // WHEN
        Optional<ParcelEntity> processedParcel = parcelService.reportParcelHop(trackingId, truckHopCode);

        // THEN
        assertThat(processedParcel).isPresent();
        assertThat(processedParcel.get().getState()).isEqualTo(INTRUCKDELIVERY);
    }

    @Test
    void GIVEN_validAddress_WHEN_submitting_parcel_THEN_return_correct_parcel() {
        // GIVEN
        TruckEntity truckRecipient = buildTruckRecipient();
        TruckEntity truckSender = buildTruckSender();

        when(geoEncodingService.encodeAddress(any())).thenReturn(Optional.of(buildRecipientGeo())).thenReturn(Optional.of(buildSenderGeo()));
        when(truckRepository.findFirstNearestTruck(any())).thenReturn(Optional.of(truckRecipient)).thenReturn(Optional.of(truckSender));

        // two layer of warehouses .thenReturn(WarehouseA).thenReturn(WarehouseB).thenReturn(WarehouseC).ThenReturn(WarehouseC)
        // warehouseA (Recipient) -> warehouseC
        // warehouseB (Sender) -> warehouseC
        // warehouseC (C1 -> Sender)
        WarehouseNextHopsEntity warehouseNextHopsA = buildWarehouseNextHopsEntity(truckRecipient, 10);
        WarehouseNextHopsEntity warehouseNextHopsB = buildWarehouseNextHopsEntity(truckSender, 10);

        WarehouseEntity warehouseA = buildWarehouse("WarehouseA", warehouseNextHopsA);
        WarehouseEntity warehouseB = buildWarehouse("WarehouseB", warehouseNextHopsB);

        WarehouseNextHopsEntity warehouseNextHopsC1 = buildWarehouseNextHopsEntity(warehouseA, 15);
        WarehouseNextHopsEntity warehouseNextHopsC2 = buildWarehouseNextHopsEntity(warehouseB, 10);

        WarehouseEntity warehouseC = buildWarehouse("WarehouseC", warehouseNextHopsC1, warehouseNextHopsC2);

        lenient().when(warehouseNextHopsRepository.findByHop(any()))
                .thenReturn(Optional.of(warehouseNextHopsA))
                .thenReturn(Optional.of(warehouseNextHopsB))
                .thenReturn(Optional.of(warehouseNextHopsC1))
                .thenReturn(Optional.of(warehouseNextHopsC2));
        // return parameter when calling save methode
        when(parcelRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ParcelEntity parcel = buildParcel("trackingId");

        // settings date for assertion and
        OffsetDateTime timeNow = OffsetDateTime.now();
        // WHEN
        ParcelEntity parcelFinished = parcelService.submitParcel(parcel);

        // THEN
        assertThat(parcelFinished.getFutureHops().get(0).getDateTime()).isCloseTo(timeNow, within(1, ChronoUnit.MINUTES));
        timeNow = timeNow.plus(30, ChronoUnit.MINUTES);
        assertThat(parcelFinished.getFutureHops().get(1).getDateTime()).isCloseTo(timeNow, within(1, ChronoUnit.MINUTES));
        timeNow = timeNow.plus(15, ChronoUnit.MINUTES);
        assertThat(parcelFinished.getFutureHops().get(2).getDateTime()).isCloseTo(timeNow, within(1, ChronoUnit.MINUTES));
        timeNow = timeNow.plus(20, ChronoUnit.MINUTES);
        assertThat(parcelFinished.getFutureHops().get(3).getDateTime()).isCloseTo(timeNow, within(1, ChronoUnit.MINUTES));
        timeNow = timeNow.plus(15, ChronoUnit.MINUTES);
        assertThat(parcelFinished.getFutureHops().get(4).getDateTime()).isCloseTo(timeNow, within(1, ChronoUnit.MINUTES));
    }

    private WarehouseEntity buildWarehouse(String code, WarehouseNextHopsEntity... warehouseNextHops) {
        List<WarehouseNextHopsEntity> nextHopsList = List.of(warehouseNextHops);
        WarehouseEntity warehouse = WarehouseEntity.builder()
                .processingDelayMins(5)
                .code(code)
                .nextHops(nextHopsList)
                .build();
        nextHopsList.forEach(nextHop -> nextHop.setWarehouse(warehouse));
        return warehouse;
    }

    private WarehouseNextHopsEntity buildWarehouseNextHopsEntity(HopEntity hop, int travelTimeMins) {
        WarehouseNextHopsEntity warehouseNextHopsEntity = WarehouseNextHopsEntity.builder()
                .traveltimeMins(travelTimeMins)
                .build();
        warehouseNextHopsEntity.setHop(hop);
        return warehouseNextHopsEntity;
    }

    @Test
    void GIVEN_invalidAddress_WHEN_submitting_parcel_THEN_throw_exception() {
        when(geoEncodingService.encodeAddress(any())).thenReturn(Optional.empty()).thenReturn(Optional.of(buildSenderGeo()));

        ParcelEntity parcel = buildParcel("trackingId");
        assertThrows(BLSubmitParcelAddressIncorrect.class, () -> parcelService.submitParcel(parcel));
    }

    private TruckEntity buildTruckRecipient() {
        return TruckEntity.builder()
                .regionGeoJson("")
                .locationCoordinates(GeoCoordinateEntity.builder().build())
                .code("Truck1")
                .description("Truck1")
                .hopType("Truck")
                .processingDelayMins(10)
                .numberPlate(RECIPIENT_NUMBER_PLATE)
                .build();
    }

    private TruckEntity buildTruckSender() {
        return TruckEntity.builder()
                .regionGeoJson("")
                .locationCoordinates(GeoCoordinateEntity.builder().build())
                .hopType("Truck")
                .code("Truck2")
                .description("Truck2")
                .processingDelayMins(20)
                .numberPlate(SENDER_NUMBER_PLATE)
                .build();
    }

    private GeoEncodingCoordinate buildRecipientGeo() {
        return GeoEncodingCoordinate.builder().lat("1").lon("2").build();
    }

    private GeoEncodingCoordinate buildSenderGeo() {
        return GeoEncodingCoordinate.builder().lat("2").lon("2").build();
    }

    private List<HopArrivalEntity> buildHopArrivals(String... codes) {
        return Stream.of(codes)
                .map(code -> HopArrivalEntity.builder().code(code).build())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private ParcelEntity buildParcel(String trakcingId) {
        return ParcelEntity.builder()
                .state(PICKUP)
                .trackingId(trakcingId)
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