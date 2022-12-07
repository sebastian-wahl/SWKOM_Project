package at.fhtw.swen3.services.impl;

import at.fhtw.swen3.persistence.entities.HopArrivalEntity;
import at.fhtw.swen3.persistence.entities.HopEntity;
import at.fhtw.swen3.persistence.entities.ParcelEntity;
import at.fhtw.swen3.persistence.entities.enums.TrackingInformationState;
import at.fhtw.swen3.persistence.repositories.HopRepository;
import at.fhtw.swen3.persistence.repositories.ParcelRepository;
import at.fhtw.swen3.services.ParcelService;
import at.fhtw.swen3.services.validation.EntityValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;


@Service
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
        Optional<ParcelEntity> parcelOpt = parcelRepository.findFirstByTrackingId(trackingId);
        parcelOpt.ifPresent(this::changeTrackingStateToDelivered);
        return parcelOpt;

    }

    @Override
    public Optional<ParcelEntity> reportParcelHop(String trackingId, String code) {
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
        return Optional.empty();
    }

    @Override
    public ParcelEntity submitParcel(ParcelEntity parcel) {
        entityValidatorService.validate(parcel);
        parcel.setTrackingId("");
        return parcelRepository.save(parcel);
    }

    @Override
    public Optional<ParcelEntity> trackParcel(String trackingId) {
        return parcelRepository.findFirstByTrackingId(trackingId);
    }

    @Override
    public ParcelEntity transitionParcel(ParcelEntity parcel) {
        entityValidatorService.validate(parcel);
        return parcelRepository.save(parcel);
    }

    private void changeTrackingStateToDelivered(ParcelEntity parcelEntity) {
        parcelEntity.setState(TrackingInformationState.DELIVERED);
        parcelRepository.save(parcelEntity);
    }
}
