package at.fhtw.swen3.persistence.entities;

import at.fhtw.swen3.services.validation.annotation.WarehouseDescriptionValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Hop
 */

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "HOP")
@Inheritance(strategy = InheritanceType.JOINED)
public class HopEntity implements BaseEntity {
    @Column(name = "ID")
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "HOP_TYPE")
    private String hopType;

    @Column(name = "CODE") // unique = true ?
    private String code;

    @WarehouseDescriptionValidation
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PROCESSING_DELAY_MINS")
    private Integer processingDelayMins;

    @Column(name = "LOCATION_NAME")
    private String locationName;


    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GEO_COORDINATE_ID", referencedColumnName = "ID")
    private GeoCoordinateEntity locationCoordinates;
}

