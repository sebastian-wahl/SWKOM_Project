package at.fhtw.swen3.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ParcelEntity implements BaseEntity {
    // Parcel
    @Min(0)
    private Float weight;

    @NotNull
    private RecipientEntity recipient;

    @NotNull
    private RecipientEntity sender;

    // NewParcelInfo
    private String trackingId;

    // TrackingInformation
    @NotNull
    private TrackingInformationState state;

    @NotNull
    private List<HopArrivalEntity> visitedHops;

    @NotNull
    private List<HopArrivalEntity> futureHops;
}
