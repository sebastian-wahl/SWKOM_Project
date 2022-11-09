package at.fhtw.swen3.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


/**
 * GeoCoordinate
 */

@Getter
@Setter
@Entity
@Table(name = "GEO_COORDINATE")
public class GeoCoordinateEntity implements BaseEntity {
    @Column(name = "ID")
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "LAT")
    private Double lat;

    @Column(name = "LON")
    private Double lon;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOP_ID")
    private HopEntity hop;
}
