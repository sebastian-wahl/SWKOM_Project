package at.fhtw.swen3.persistence.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Warehouse
 */


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WarehouseEntity extends HopEntity {

    @Column(name = "LEVEL")
    private Integer level;

    @NotNull
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WarehouseNextHopsEntity> nextHops;
}

