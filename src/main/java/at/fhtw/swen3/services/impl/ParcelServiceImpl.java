package at.fhtw.swen3.services.impl;

import at.fhtw.swen3.persistence.entities.HopEntity;
import at.fhtw.swen3.persistence.entities.ParcelEntity;
import at.fhtw.swen3.persistence.entities.TrackingInformationState;
import at.fhtw.swen3.persistence.repositories.HopRepository;
import at.fhtw.swen3.persistence.repositories.ParcelRepository;
import at.fhtw.swen3.services.ParcelService;
import at.fhtw.swen3.services.dto.HopArrival;
import at.fhtw.swen3.services.dto.NewParcelInfo;
import at.fhtw.swen3.services.dto.TrackingInformation;
import at.fhtw.swen3.services.mapper.HopMapper;
import at.fhtw.swen3.services.mapper.ParcelMapper;
import at.fhtw.swen3.services.validation.EntityValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;


@Service
public class ParcelServiceImpl implements ParcelService {

    @Autowired
    private EntityValidatorService entityValidatorService;

    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private HopRepository hopRepository;

    @Override
    public void reportParcelDelivery(String trackingId) {
        ParcelEntity parcel = parcelRepository.findFirstByTrackingId(trackingId);
        parcel.setState(TrackingInformationState.DELIVERED);
        parcelRepository.save(parcel);
    }

    @Override
    public void reportParcelHop(String trackingId, String code) {
        ParcelEntity parcel = parcelRepository.findFirstByTrackingId(trackingId);

        HopEntity hopEntity = hopRepository.findFirstByCode(code);
        // add hop do parcel
        // ToDo think about mapping
        parcel.getVisitedHops().add(HopMapper.INSTANCE.fromHopEntity(hopEntity));

        // save parcel and its dependencies
        parcelRepository.save(parcel);
    }

    @Override
    public ParcelEntity submitParcel(ParcelEntity parcel) {
        entityValidatorService.validate(parcel);
        parcel.setTrackingId("");
        return parcelRepository.save(parcel);
    }

    @Override
    public ParcelEntity trackParcel(String trackingId) {
        return parcelRepository.findFirstByTrackingId(trackingId);
    }

    @Override
    public ParcelEntity transitionParcel(ParcelEntity parcel) {
        entityValidatorService.validate(parcel);
        return parcelRepository.save(parcel);
    }
}
