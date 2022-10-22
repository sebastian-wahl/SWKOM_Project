package at.fhtw.swen3.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Parcel {
    // Parcel
    private Float weight;

    private Recipient recipient;

    private Recipient sender;

    // NewParcelInfo
    private String trackingId;

    // TrackingInformation
    private TrackingInformationState state;

    private List<HopArrival> visitedHops;

    private List<HopArrival> futureHops;
}
