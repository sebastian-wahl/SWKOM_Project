package at.fhtw.swen3.persistence.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * WarehouseAllOfNextHops
 */

@Getter
@Setter
public class WarehouseNextHopsEntity implements BaseEntity {

    private Integer traveltimeMins;

    private HopEntity hop;
}

