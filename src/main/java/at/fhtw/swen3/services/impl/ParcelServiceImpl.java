package at.fhtw.swen3.services.impl;

import at.fhtw.swen3.persistence.entities.HopArrivalEntity;
import at.fhtw.swen3.persistence.entities.HopEntity;
import at.fhtw.swen3.persistence.entities.ParcelEntity;
import at.fhtw.swen3.persistence.entities.enums.TrackingInformationState;
import at.fhtw.swen3.persistence.repositories.HopRepository;
import at.fhtw.swen3.persistence.repositories.ParcelRepository;
import at.fhtw.swen3.services.ParcelService;
import at.fhtw.swen3.services.exception.BLException.BLTrackingNumberExistException;
import at.fhtw.swen3.services.validation.EntityValidatorService;
import com.github.curiousoddman.rgxgen.RgxGen;
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
        ParcelEntity out = setFieldsAndSaveParcel(parcel);
        log.debug("Parcel saved successfully");
        return out;
    }

    private ParcelEntity setFieldsAndSaveParcel(ParcelEntity parcel) {
        generateAndSetTrackingId(parcel);
        parcel.setState(TrackingInformationState.PICKUP);
        // ToDo predict future hops (GeoSpatial)
        return parcelRepository.save(parcel);
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
        return parcelRepository.save(parcel);
    }

    private void changeTrackingStateToDelivered(ParcelEntity parcelEntity) {
        parcelEntity.setState(TrackingInformationState.DELIVERED);
        parcelRepository.save(parcelEntity);
    }
}
