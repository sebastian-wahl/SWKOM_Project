package at.fhtw.swen3.services.impl;

import at.fhtw.swen3.persistence.entities.ParcelEntity;
import at.fhtw.swen3.services.ParcelService;
import at.fhtw.swen3.services.dto.NewParcelInfo;
import at.fhtw.swen3.services.dto.TrackingInformation;
import at.fhtw.swen3.services.validation.EntityValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ParcelServiceImpl implements ParcelService {

    @Autowired
    private EntityValidatorService entityValidatorService;

    @Override
    public void reportParcelDelivery(String trackingId) {

    }

    @Override
    public void reportParcelHop(String trackingId, String code) {

    }

    @Override
    public NewParcelInfo submitParcel(ParcelEntity parcel) {
        entityValidatorService.validate(parcel);
        return null;
    }

    @Override
    public TrackingInformation trackParcel(String trackingId) {
        return null;
    }

    @Override
    public NewParcelInfo transitionParcel(ParcelEntity parcel) {
        return null;
    }
}
