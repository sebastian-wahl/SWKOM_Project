package at.fhtw.swen3.persistence.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
    //@Builder.Default
    @NotNull
    @Valid
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "warehouse", fetch = FetchType.EAGER)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "WAREHOUSE_NEXT_HOPS_ID")
    private List<WarehouseNextHopsEntity> nextHops = new ArrayList<>();
}

