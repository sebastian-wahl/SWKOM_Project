package at.fhtw.swen3.persistence.entities;

import lombok.*;

import javax.persistence.*;


/**
 * GeoCoordinate
 */

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}
