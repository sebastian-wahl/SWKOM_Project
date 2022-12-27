package at.fhtw.swen3.services.impl;

import at.fhtw.swen3.gps.service.GeoEncodingService;
import at.fhtw.swen3.persistence.entities.HopArrivalEntity;
import at.fhtw.swen3.persistence.entities.HopEntity;
import at.fhtw.swen3.persistence.entities.ParcelEntity;
import at.fhtw.swen3.persistence.entities.enums.TrackingInformationState;
import at.fhtw.swen3.persistence.repositories.HopRepository;
import at.fhtw.swen3.persistence.repositories.ParcelRepository;
import at.fhtw.swen3.services.ParcelService;
import at.fhtw.swen3.services.exception.BLException.BLParcelNotFound;
import at.fhtw.swen3.services.validation.EntityValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;


@Service
@Slf4j
public class ParcelServiceImpl implements ParcelService {

    private final EntityValidatorService entityValidatorService;

    private final ParcelRepository parcelRepository;

    private final HopRepository hopRepository;

    private final GeoEncodingService geoEncodingService;

    @Autowired
    public ParcelServiceImpl(EntityValidatorService entityValidatorService, ParcelRepository parcelRepository, HopRepository hopRepository, GeoEncodingService geoEncodingService) {
        this.entityValidatorService = entityValidatorService;
        this.parcelRepository = parcelRepository;
        this.hopRepository = hopRepository;
        this.geoEncodingService = geoEncodingService;
    }

    @Override
    public void reportParcelDelivery(String trackingId) {
        log.debug("Reporting parcel delivery for id {}", trackingId);
        Optional<ParcelEntity> parcelOpt = parcelRepository.findFirstByTrackingId(trackingId);
        if (parcelOpt.isPresent()) {
            // ToDo check if last location is the arrival dest to validate the delivery
            String hopArrivalCode = parcelOpt.get().getVisitedHops().get(parcelOpt.get().getVisitedHops().size() - 1).getCode();
            hopRepository.findFirstByCode(hopArrivalCode).get().getLocationCoordinates();
            parcelOpt.get().getRecipient().getStreet();

            changeTrackingStateToDelivered(parcelOpt.get());
        } else {
            throw new BLParcelNotFound(trackingId);
        }
    }

    @Override
    public Optional<ParcelEntity> reportParcelHop(String trackingId, String code) {
        log.debug("Reporting parcel hop for hop with id {} and parcel with id {}", code, trackingId);
        Optional<ParcelEntity> parcelOpt = parcelRepository.findFirstByTrackingId(trackingId);

        Optional<HopEntity> hopOptional = hopRepository.findFirstByCode(code);
        // add hop do parcel
        if (parcelOpt.isPresent() && hopOptional.isPresent()) {
            ParcelEntity parcel = parcelOpt.get();

            HopArrivalEntity hopArrivalEntity = HopArrivalEntity.builder()
                    .dateTime(OffsetDateTime.now())
                    .code(hopOptional.get().getCode())
                    .description(hopOptional.get().getDescription())
                    .build();


            //parcel.getVisitedHops().add(hopArrivalEntity);
            parcelRepository.save(parcel);
            return Optional.of(parcel);
        }
        log.error("Reporting parcel not possible! Hop found: {}; Parcel found: {}", hopOptional.isPresent(), parcelOpt.isPresent());
        return Optional.empty();
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
