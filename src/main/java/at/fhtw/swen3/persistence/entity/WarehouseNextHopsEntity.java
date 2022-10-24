package at.fhtw.swen3.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * WarehouseAllOfNextHops
 */

@Getter
@Setter
public class WarehouseNextHopsEntity implements BaseEntity {

    private Integer traveltimeMins;

    @NotNull
    private HopEntity hop;
}

