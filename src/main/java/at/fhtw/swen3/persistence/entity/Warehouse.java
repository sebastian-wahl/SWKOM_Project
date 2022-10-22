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
public class Warehouse extends Hop {

    private Integer level;

    private List<WarehouseNextHops> nextHops = new ArrayList<>();
}

