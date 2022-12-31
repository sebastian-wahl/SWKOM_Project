package at.fhtw.swen3.services.exception.BLException;

import at.fhtw.swen3.services.BLException;

public class BLWarehouseNextHopsNotFound extends BLException {
    public BLWarehouseNextHopsNotFound(String hopCode) {
        this.message = "WarehouseNextHop with code \"" + hopCode + "\" not found!";
    }
}
