package at.fhtw.swen3.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "HOP")
@Inheritance(strategy = InheritanceType.JOINED)
public class HopEntity implements BaseEntity {
    @Column(name = "ID")
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "HOP_TYPE")
    private String hopType;
    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PROCESSING_DELAY_MINS")
    private Integer processingDelayMins;

    @Column(name = "LOCATION_NAME")
    private String locationName;

    @NotNull
    @OneToOne(mappedBy = "hop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private GeoCoordinateEntity locationCoordinates;
}

