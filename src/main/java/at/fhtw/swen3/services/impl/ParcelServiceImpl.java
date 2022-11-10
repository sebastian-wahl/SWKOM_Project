package at.fhtw.swen3.services.impl;

import at.fhtw.swen3.services.ParcelService;
import at.fhtw.swen3.services.dto.NewParcelInfo;
import at.fhtw.swen3.services.dto.Parcel;
import at.fhtw.swen3.services.dto.TrackingInformation;
import org.springframework.stereotype.Service;

@Service
public class ParcelServiceImpl implements ParcelService {

    @Override
    public void reportParcelDelivery(String trackingId) {

    }

    @Override
    public void reportParcelHop(String trackingId, String code) {

    }

    @Override
    public NewParcelInfo submitParcel(Parcel parcel) {
        return null;
    }

    @Override
    public TrackingInformation trackParcel(String trackingId) {
        return null;
    }

    @Override
    public NewParcelInfo transitionParcel(Parcel parcel) {
        return null;
    }
}
