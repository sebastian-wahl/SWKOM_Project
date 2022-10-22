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
public class ParcelEntity {
    // Parcel
    private Float weight;

    private RecipientEntity recipient;

    private RecipientEntity sender;

    // NewParcelInfo
    private String trackingId;

    // TrackingInformation
    private TrackingInformationState state;

    private List<HopArrivalEntity> visitedHops;

    private List<HopArrivalEntity> futureHops;
}
