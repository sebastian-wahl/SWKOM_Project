package at.fhtw.swen3.services.exception.blexception;

import at.fhtw.swen3.services.BLException;

public class BLWarehouseNextHopsNotFound extends BLException {

    public static final String ERROR_MESSAGE = "WarehouseNextHop with code %s not found!";

    public BLWarehouseNextHopsNotFound(String hopCode) {
        super(String.format(ERROR_MESSAGE, hopCode));
    }
}
