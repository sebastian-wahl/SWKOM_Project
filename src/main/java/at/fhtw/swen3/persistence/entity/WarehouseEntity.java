package at.fhtw.swen3.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Warehouse
 */


@Getter
@Setter
public class WarehouseEntity extends HopEntity implements BaseEntity {

    private Integer level;

    @NotNull
    private List<WarehouseNextHopsEntity> nextHops = new ArrayList<>();
}

