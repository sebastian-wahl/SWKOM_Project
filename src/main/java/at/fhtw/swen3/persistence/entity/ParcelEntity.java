package at.fhtw.swen3.persistence.entity;

import at.fhtw.swen3.persistence.validation.annotation.MinExclusiveValidation;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PARCEL")
public class ParcelEntity implements BaseEntity {
    @Column(name = "ID")
    @Id
    @GeneratedValue
    private Long id;

    // Parcel
    @MinExclusiveValidation
    @Column(name = "WEIGHT")
    private Float weight;

    @NotNull
    @OneToOne(mappedBy = "parcel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RecipientEntity recipient;

    @NotNull
    @OneToOne(mappedBy = "parcel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RecipientEntity sender;

    // NewParcelInfo
    @Column(name = "TRACKING_ID")
    private String trackingId;

    // TrackingInformation
    @NotNull
    @Enumerated(EnumType.STRING)
    private TrackingInformationState state;

    @NotNull
    @OneToMany(mappedBy = "parcel", cascade = CascadeType.ALL)
    private List<HopArrivalEntity> visitedHops;

    @NotNull
    @OneToMany(mappedBy = "parcel", cascade = CascadeType.ALL)
    private List<HopArrivalEntity> futureHops;
}
