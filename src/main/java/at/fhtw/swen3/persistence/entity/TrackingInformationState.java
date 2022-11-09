package at.fhtw.swen3.persistence.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TrackingInformationState implements BaseEntity {
    PICKUP("Pickup"),

    INTRANSPORT("InTransport"),

    INTRUCKDELIVERY("InTruckDelivery"),

    TRANSFERRED("Transferred"),

    DELIVERED("Delivered");

    private final String value;

    TrackingInformationState(String value) {
        this.value = value;
    }

    public static TrackingInformationState fromValue(String value) {
        for (TrackingInformationState b : TrackingInformationState.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
