package at.fhtw.swen3.services;

import at.fhtw.swen3.persistence.entities.ParcelEntity;

public interface ParcelService {
    void reportParcelDelivery(String trackingId);

    void reportParcelHop(String trackingId, String code);

    ParcelEntity submitParcel(ParcelEntity parcel);

    ParcelEntity trackParcel(String trackingId);

    ParcelEntity transitionParcel(ParcelEntity parcel);
}
