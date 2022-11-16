package at.fhtw.swen3.services;

import at.fhtw.swen3.persistence.entities.ParcelEntity;
import at.fhtw.swen3.services.dto.NewParcelInfo;
import at.fhtw.swen3.services.dto.TrackingInformation;

public interface ParcelService {
    void reportParcelDelivery(String trackingId);
    void reportParcelHop(String trackingId, String code);
    NewParcelInfo submitParcel(ParcelEntity parcel);
    TrackingInformation trackParcel(String trackingId);
    NewParcelInfo transitionParcel(ParcelEntity parcel);
}
