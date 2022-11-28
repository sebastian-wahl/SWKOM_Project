package at.fhtw.swen3.persistence.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * WarehouseAllOfNextHops
 */

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WAREHOUSE_NEXT_HOPS")
public class WarehouseNextHopsEntity implements BaseEntity {
    @Column(name = "ID")
    @Id
    private Long id;

    @Column(name = "TRAVELTIME_MINS")
    private Integer traveltimeMins;

    @NotNull
    @Valid
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private HopEntity hop;

    @ManyToOne(fetch = FetchType.EAGER)
    private WarehouseEntity warehouse;
}

