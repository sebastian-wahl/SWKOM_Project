package at.fhtw.swen3.persistence.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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
    @Min(value = 0, message = "Weight must be greater than 0")
    @Column(name = "WEIGHT")
    private Float weight;

    @NotNull(message = "Recipient must not be null")
    @OneToOne(mappedBy = "parcel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RecipientEntity recipient;

    @NotNull(message = "Sender must not be null")
    @OneToOne(mappedBy = "parcel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RecipientEntity sender;

    // NewParcelInfo
    @Column(name = "TRACKING_ID")
    private String trackingId;

    // TrackingInformation
    @Enumerated(EnumType.STRING)
    private TrackingInformationState state;

    @Singular
    @NotNull(message = "Visited hops must not be null")
    @OneToMany(mappedBy = "parcel", cascade = CascadeType.ALL)
    private List<HopArrivalEntity> visitedHops = new ArrayList<>();

    @Singular
    @NotNull(message = "Future hops must not be null")
    @OneToMany(mappedBy = "parcel", cascade = CascadeType.ALL)
    private List<HopArrivalEntity> futureHops = new ArrayList<>();
}
