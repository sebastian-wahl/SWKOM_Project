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
    @GeneratedValue
    private Long id;

    @Column(name = "TRAVELTIME_MINS")
    private Integer traveltimeMins;

    @NotNull
    @Valid
    @OneToOne(mappedBy = "previousHop", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private HopEntity hop;

    // ref WarehouseEntity
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "WAREHOUSE_ID")
    private WarehouseEntity warehouse;

    public void setHop(HopEntity hop) {
        this.hop = hop;
        hop.setPreviousHop(this);
    }
}

