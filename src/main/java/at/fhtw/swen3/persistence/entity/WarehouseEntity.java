package at.fhtw.swen3.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Warehouse
 */


@Getter
@Setter
public class WarehouseEntity extends HopEntity implements BaseEntity {

    private Integer level;

    private List<WarehouseNextHopsEntity> nextHops = new ArrayList<>();
}

