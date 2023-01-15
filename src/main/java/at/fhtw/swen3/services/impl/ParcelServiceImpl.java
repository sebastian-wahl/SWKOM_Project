package at.fhtw.swen3.services.impl;

import at.fhtw.swen3.persistence.entities.HopArrivalEntity;
import at.fhtw.swen3.persistence.entities.ParcelEntity;
import at.fhtw.swen3.persistence.entities.enums.TrackingInformationState;
import at.fhtw.swen3.persistence.repositories.HopRepository;
import at.fhtw.swen3.persistence.repositories.ParcelRepository;
import at.fhtw.swen3.services.ParcelService;
import at.fhtw.swen3.services.exception.BLException.BLInvalidHopArrivalCodeException;
import at.fhtw.swen3.services.validation.EntityValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
public class ParcelServiceImpl implements ParcelService {

    private final EntityValidatorService entityValidatorService;

    private final ParcelRepository parcelRepository;

    private final HopRepository hopRepository;

    @Autowired
    public ParcelServiceImpl(EntityValidatorService entityValidatorService, ParcelRepository parcelRepository, HopRepository hopRepository) {
        this.entityValidatorService = entityValidatorService;
        this.parcelRepository = parcelRepository;
        this.hopRepository = hopRepository;
    }

    @Override
    public Optional<ParcelEntity> reportParcelDelivery(String trackingId) {
        log.debug("Reporting parcel delivery for id {}", trackingId);
        Optional<ParcelEntity> parcelOpt = parcelRepository.findFirstByTrackingId(trackingId);
        parcelOpt.ifPresent(this::changeTrackingStateToDelivered);
        return parcelOpt;

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

            parcelRepository.save(parcel);
            return Optional.of(parcel);
        }
        log.info("Parcel with trackingId={} not found", trackingId);
        return Optional.empty();
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
        entityValidatorService.validate(parcel);
        log.debug("Parcel validated");
        generateAndSetTrackingId(parcel);
        ParcelEntity out = parcelRepository.save(parcel);
        log.debug("Parcel saved successfully");
        return out;
    }

    private void generateAndSetTrackingId(ParcelEntity parcel) {
        parcel.setTrackingId("ABCD1234");
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
        return parcelRepository.save(parcel);
    }

    private void changeTrackingStateToDelivered(ParcelEntity parcelEntity) {
        parcelEntity.setState(TrackingInformationState.DELIVERED);
        parcelRepository.save(parcelEntity);
    }
}
