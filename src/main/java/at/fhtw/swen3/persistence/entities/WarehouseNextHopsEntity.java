package at.fhtw.swen3.persistence.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * WarehouseAllOfNextHops
 */

@Getter
@Setter
@Entity
@Table(name = "WAREHOUSE_NEXT_HOPS")
public class WarehouseNextHopsEntity implements BaseEntity {
    @Column(name = "ID")
    @Id
    private Long id;

    @Column(name = "TRAVELTIME_MINS")
    private Integer traveltimeMins;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private HopEntity hop;

    @ManyToOne(fetch = FetchType.LAZY)
    private WarehouseEntity warehouse;
}

