package at.fhtw.swen3.persistence.entities;

import at.fhtw.swen3.persistence.entities.enums.TrackingInformationState;
import at.fhtw.swen3.services.validation.annotation.MinExclusiveValidation;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.Valid;
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
    @MinExclusiveValidation
    @Column(name = "WEIGHT")
    private Float weight;

    @Valid
    @NotNull(message = "Recipient must not be null")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RECIPIENT_ID", referencedColumnName = "ID")
    private RecipientEntity recipient;

    @NotNull(message = "Sender must not be null")
    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SENDER_ID", referencedColumnName = "ID")
    private RecipientEntity sender;

    // NewParcelInfo
    @Column(name = "TRACKING_ID") // unique = true ?
    private String trackingId;

    // TrackingInformation
    @Enumerated(EnumType.STRING)
    @NotNull(message = "State must not be null")
    private TrackingInformationState state;

    @Singular
    //@Builder.Default
    @NotNull(message = "Visited hops must not be null")
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "VISITED_PARCEL_HOP_ARRIVAL",
            joinColumns = @JoinColumn(name = "PARCEL_ID"),
            inverseJoinColumns = @JoinColumn(name = "HOP_ARRIVAL_ID"))
    private List<HopArrivalEntity> visitedHops = new ArrayList<>();

    @Singular
    //@Builder.Default
    @NotNull(message = "Future hops must not be null")
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "FUTURE_PARCEL_HOP_ARRIVAL",
            joinColumns = @JoinColumn(name = "PARCEL_ID"),
            inverseJoinColumns = @JoinColumn(name = "HOP_ARRIVAL_ID"))
    private List<HopArrivalEntity> futureHops = new ArrayList<>();
}
