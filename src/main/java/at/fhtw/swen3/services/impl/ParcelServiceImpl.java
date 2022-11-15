package at.fhtw.swen3.services.impl;

import at.fhtw.swen3.persistence.entities.ParcelEntity;
import at.fhtw.swen3.services.ParcelService;
import at.fhtw.swen3.services.dto.HopArrival;
import at.fhtw.swen3.services.dto.NewParcelInfo;
import at.fhtw.swen3.services.dto.TrackingInformation;
import at.fhtw.swen3.services.validation.EntityValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;


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
        return dummyNewParcelInfo();
    }

    @Override
    public TrackingInformation trackParcel(String trackingId) {
        return dummyTrackingInformation();
    }

    @Override
    public NewParcelInfo transitionParcel(ParcelEntity parcel) {
        entityValidatorService.validate(parcel);
        return dummyNewParcelInfo();
    }

    private NewParcelInfo dummyNewParcelInfo() {
        return new NewParcelInfo()
                .trackingId("ABCDE6789");
    }

    private TrackingInformation dummyTrackingInformation() {
        return new TrackingInformation()
                .state(TrackingInformation.StateEnum.INTRANSPORT)
                .addVisitedHopsItem(
                    new HopArrival()
                        .code("ABCD1")
                        .description("visited")
                        .dateTime(OffsetDateTime.now())
                )
                .addFutureHopsItem(
                    new HopArrival()
                        .code("ABCD2")
                        .description("future")
                        .dateTime(OffsetDateTime.now())
                );
    }
}
