package at.fhtw.swen3.services.impl;

import at.fhtw.swen3.gps.service.GeoEncodingService;
import at.fhtw.swen3.gps.service.models.Address;
import at.fhtw.swen3.gps.service.models.GeoEncodingCoordinate;
import at.fhtw.swen3.persistence.entities.HopArrivalEntity;
import at.fhtw.swen3.persistence.entities.HopEntity;
import at.fhtw.swen3.persistence.entities.ParcelEntity;
import at.fhtw.swen3.persistence.entities.TruckEntity;
import at.fhtw.swen3.persistence.entities.enums.TrackingInformationState;
import at.fhtw.swen3.persistence.repositories.ParcelRepository;
import at.fhtw.swen3.persistence.repositories.TruckRepository;
import at.fhtw.swen3.services.ParcelService;
import at.fhtw.swen3.services.exception.blexception.*;
import at.fhtw.swen3.services.validation.EntityValidatorService;
import com.github.curiousoddman.rgxgen.RgxGen;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static at.fhtw.swen3.util.JTSUtil.geoEncodingCoordinateToGeometry;


@Service
@Slf4j
@RequiredArgsConstructor
public class ParcelServiceImpl implements ParcelService {

    private final EntityValidatorService entityValidatorService;

    private final ParcelRepository parcelRepository;

    private final GeoEncodingService geoEncodingService;

    private final TruckRepository truckRepository;

    @Override
    public void reportParcelDelivery(String trackingId) {
        log.debug("Reporting parcel delivery for id {}", trackingId);
        Optional<ParcelEntity> parcelOpt = parcelRepository.findFirstByTrackingId(trackingId);
        if (parcelOpt.isPresent()) {
            ParcelEntity parcel = parcelOpt.get();
            parcel.getVisitedHops().addAll(parcel.getFutureHops());
            parcel.setFutureHops(new ArrayList<>());

            parcel.setState(TrackingInformationState.DELIVERED);
            parcelRepository.save(parcel);
        } else {
            throw new BLParcelNotFound(trackingId);
        }
    }

    @Override
    public Optional<ParcelEntity> reportParcelHop(String trackingId, String code) {
        log.debug("Reporting parcel hop for hop with id {} and parcel with id {}", code, trackingId);
        Optional<ParcelEntity> parcelOpt = parcelRepository.findFirstByTrackingId(trackingId);

        // add hop do parcel
        if (parcelOpt.isPresent()) {
            ParcelEntity parcel = parcelOpt.get();

            validateCodeFirstInFutureHops(parcel, code);
            moveHopArrivalToVisited(parcel);
            setReportHopTrackingState(parcel, code);

            parcelRepository.save(parcel);
            return Optional.of(parcel);
        }
        log.info("Parcel with trackingId={} not found", trackingId);
        return Optional.empty();
    }

    private void setReportHopTrackingState(ParcelEntity parcel, String code) {
        if (isLastTruck(parcel, code)) {
            parcel.setState(TrackingInformationState.INTRUCKDELIVERY);
        } else {
            parcel.setState(TrackingInformationState.INTRANSPORT);
        }
    }

    private boolean isLastTruck(ParcelEntity parcel, String code) {
        return parcel.getFutureHops().isEmpty() && code.equals(getLastElement(parcel.getVisitedHops()).getCode());
    }

    private void validateCodeFirstInFutureHops(ParcelEntity parcel, String code) {
        findFirstFutureHop(parcel)
                .filter(hopArrival -> code.equals(hopArrival.getCode()))
                .orElseThrow(() -> new BLInvalidHopArrivalCodeException(code));
    }

    private Optional<HopArrivalEntity> findFirstFutureHop(ParcelEntity parcel) {
        return parcel.getFutureHops().stream().findFirst();
    }

    private void moveHopArrivalToVisited(ParcelEntity parcel) {
        findFirstFutureHop(parcel).ifPresent(hopArrivalEntity -> {
            parcel.getFutureHops().remove(hopArrivalEntity);
            parcel.getVisitedHops().add(hopArrivalEntity);
        });
    }

    @Override
    public ParcelEntity submitParcel(ParcelEntity parcel) {
        log.debug("Submitting new parcel");

        generateAndSetTrackingId(parcel);
        parcel.setState(TrackingInformationState.PICKUP);

        entityValidatorService.validate(parcel);
        log.debug("Parcel validated");

        predictHops(parcel);
        ParcelEntity parcelOut = parcelRepository.save(parcel);
        log.debug("Parcel saved successfully");
        return parcelOut;
    }

    private void predictHops(ParcelEntity parcel) {
        Address senderAddress = Address.fromRecipient(parcel.getSender());
        Address recipientAddress = Address.fromRecipient(parcel.getRecipient());
        Optional<GeoEncodingCoordinate> recipientCoordinates = geoEncodingService.encodeAddress(recipientAddress);
        Optional<GeoEncodingCoordinate> senderCoordinates = geoEncodingService.encodeAddress(senderAddress);
        if (senderCoordinates.isEmpty()) {
            throw new BLSubmitParcelAddressIncorrect(senderAddress, true);
        }
        if (recipientCoordinates.isEmpty()) {
            throw new BLSubmitParcelAddressIncorrect(recipientAddress, false);

        }
        Optional<TruckEntity> nearestRecipientTruckOpt = this.truckRepository.findFirstNearestTruck((Point) geoEncodingCoordinateToGeometry(recipientCoordinates.get()));
        Optional<TruckEntity> nearestSenderTruckOpt = this.truckRepository.findFirstNearestTruck((Point) geoEncodingCoordinateToGeometry(senderCoordinates.get()));
        if (nearestRecipientTruckOpt.isEmpty()) {
            throw new BLNoTruckFound(recipientCoordinates.get());
        }
        if (nearestSenderTruckOpt.isEmpty()) {
            throw new BLNoTruckFound(senderCoordinates.get());
        }
        log.debug("Address validated");

        predictNextHops(parcel, nearestRecipientTruckOpt.get(), nearestSenderTruckOpt.get());
    }

    private void predictNextHops(ParcelEntity parcel, TruckEntity nearestRecipientTruck, TruckEntity nearestSenderTruck) {
        List<HopEntity> nextSenderHops = retrieveAllHopParents(nearestSenderTruck);
        List<HopEntity> nextRecipientHops = retrieveAllHopParents(nearestRecipientTruck);

        HopEntity commonParent = null;

        while (!nextRecipientHops.isEmpty() && !nextSenderHops.isEmpty()) {
            HopEntity lastSenderHop = getLastElement(nextSenderHops);
            HopEntity lastRecipientHop = getLastElement(nextRecipientHops);
            if (!lastSenderHop.getCode().equals(lastRecipientHop.getCode())) {
                log.debug("Found common parent");
                break;
            }
            removeLastElement(nextSenderHops);
            removeLastElement(nextRecipientHops);
            commonParent = lastSenderHop;
        }
        Collections.reverse(nextRecipientHops);
        List<HopEntity> finalFutureHopsSender = new ArrayList<>();
        finalFutureHopsSender.add(nearestSenderTruck);
        finalFutureHopsSender.addAll(nextSenderHops);
        finalFutureHopsSender.add(commonParent);

        List<HopEntity> finalFutureHopsRecipient = new ArrayList<>(nextRecipientHops);
        finalFutureHopsRecipient.add(nearestRecipientTruck);

        List<HopArrivalEntity> hopArrivalEntityList = new ArrayList<>();

        OffsetDateTime arrivalTime = OffsetDateTime.now();
        for (int i = 0; i < finalFutureHopsSender.size(); i++) {
            HopEntity hop = finalFutureHopsSender.get(i);
            hopArrivalEntityList.add(HopArrivalEntity.fromHop(hop, arrivalTime));
            if (i < finalFutureHopsSender.size() - 1) {
                arrivalTime = arrivalTime.plus(calculateDelaySender(hop), ChronoUnit.MINUTES);
            }
        }

        for (HopEntity hop : finalFutureHopsRecipient) {
            arrivalTime = arrivalTime.plus(calculateDelayRecipient(hop), ChronoUnit.MINUTES);
            hopArrivalEntityList.add(HopArrivalEntity.fromHop(hop, arrivalTime));
        }

        parcel.setFutureHops(hopArrivalEntityList);
        log.debug("Future hops set");
    }

    private <T> T getLastElement(List<T> list) {
        return list.get(list.size() - 1);
    }

    private <T> void removeLastElement(List<T> list) {
        list.remove(list.size() - 1);
    }

    private int calculateDelaySender(HopEntity hopEntity) {
        return hopEntity.getProcessingDelayMins() + hopEntity.getReferencedNextHop().getTraveltimeMins();
    }

    private int calculateDelayRecipient(HopEntity hopEntity) {
        return hopEntity.getReferencedNextHop().getWarehouse().getProcessingDelayMins() + hopEntity.getReferencedNextHop().getTraveltimeMins();
    }

    private List<HopEntity> retrieveAllHopParents(TruckEntity truckEntity) {
        List<HopEntity> nextHops = new ArrayList<>();
        HopEntity nextHop = truckEntity.getReferencedNextHop().getWarehouse();
        while (nextHop != null) {
            nextHops.add(nextHop);

            // root warehouse
            if (nextHop.getReferencedNextHop() == null) {
                break;
            }
            nextHop = nextHop.getReferencedNextHop().getWarehouse();
        }
        return nextHops;
    }

    /**
     * Recursively generates a unique tracking id for the given parcel
     */
    private void generateAndSetTrackingId(ParcelEntity parcel) {
        RgxGen rgxGen = new RgxGen(ParcelEntity.TRACKING_ID_PATTERN);
        String generatedTrackingId = rgxGen.generate();
        if (parcelRepository.findFirstByTrackingId(generatedTrackingId).isPresent()) {
            generateAndSetTrackingId(parcel);
        } else {
            parcel.setTrackingId(rgxGen.generate());
        }
    }

    @Override
    public Optional<ParcelEntity> trackParcel(String trackingId) {
        log.debug("Search parcel with tracking id {}", trackingId);
        return parcelRepository.findFirstByTrackingId(trackingId);
    }

    @Override
    public ParcelEntity transitionParcel(ParcelEntity parcel) {
        log.debug("Transition parcel with tracking id {}", parcel.getId());
        entityValidatorService.validate(parcel);
        if (parcelRepository.findFirstByTrackingId(parcel.getTrackingId()).isPresent()) {
            throw new BLTrackingNumberExistException(parcel.getTrackingId());
        }
        predictHops(parcel);
        parcel.setState(TrackingInformationState.PICKUP);
        ParcelEntity out = parcelRepository.save(parcel);
        log.debug("Parcel saved successfully");
        return out;
    }
}
