package at.fhtw.swen3.persistence.entities;

import lombok.*;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;


/**
 * GeoCoordinate
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "GEO_COORDINATE")
public class GeoCoordinateEntity implements BaseEntity {
    @Column(name = "ID")
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "LOCATION")
    private Point location;
}
