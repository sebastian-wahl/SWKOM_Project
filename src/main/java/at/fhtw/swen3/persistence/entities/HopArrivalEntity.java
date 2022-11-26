package at.fhtw.swen3.persistence.entities;

import at.fhtw.swen3.services.validation.annotation.HopArrivalCodeValidation;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

/**
 * HopArrival
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "HOP_ARRIVAL")
public class HopArrivalEntity implements BaseEntity {
    @Column(name = "ID")
    @Id
    @GeneratedValue
    private Long id;

    @HopArrivalCodeValidation
    @Column(name = "CODE") // unique = true ?
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DATE_TIME")
    @NotNull(message = "DateTime must not be null")
    private OffsetDateTime dateTime;

    /*@OneToMany(mappedBy = "hopArrival")
    private List<ParcelHopArrivalEntity> parcelHopArrival = new ArrayList<>();*/

    /*@Singular
    @NotNull(message = "Visited parcels must not be null")
    @ManyToMany(mappedBy = "visitedHops")
    private List<ParcelEntity> visitedParcels = new ArrayList<>();

    @Singular
    @NotNull(message = "Future parcels must not be null")
    //@OneToMany(mappedBy = "hopArrival", fetch = FetchType.EAGER)
    @ManyToMany(mappedBy = "futureHops")
    private List<ParcelEntity> futureHops = new ArrayList<>();*/
}

