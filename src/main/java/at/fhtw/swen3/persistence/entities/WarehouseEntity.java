package at.fhtw.swen3.persistence.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Warehouse
 */


@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
public class WarehouseEntity extends HopEntity {

    @Column(name = "LEVEL")
    private Integer level;

    @Singular
    @NotNull
    @Valid
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WarehouseNextHopsEntity> nextHops = new ArrayList<>();
}

