package at.fhtw.swen3.services;

import at.fhtw.swen3.persistence.entities.ParcelEntity;

import java.util.Optional;

public interface ParcelService {
    Optional<ParcelEntity> reportParcelDelivery(String trackingId);

    Optional<ParcelEntity> reportParcelHop(String trackingId, String code);

    ParcelEntity submitParcel(ParcelEntity parcel);

    Optional<ParcelEntity> trackParcel(String trackingId);

    ParcelEntity transitionParcel(ParcelEntity parcel);
}
