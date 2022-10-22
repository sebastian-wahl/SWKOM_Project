package at.fhtw.swen3.persistence.entity;

import at.fhtw.swen3.services.dto.HopArrival;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Parcel {
    // Parcel
    private Float weight;

    private Recipient recipient;

    private Recipient sender;

    // NewParcelInfo
    private String trackingId;

    // TrackingInformation
    private TrackingInformationState state;

    private List<HopArrival> visitedHops = new ArrayList<>();

    private List<HopArrival> futureHops = new ArrayList<>();
}
