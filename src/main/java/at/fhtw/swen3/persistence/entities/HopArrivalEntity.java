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
    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DATE_TIME")
    @NotNull(message = "DateTime must not be null")
    private OffsetDateTime dateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull(message = "Parcel must not be null")
    private ParcelEntity parcel;
}

