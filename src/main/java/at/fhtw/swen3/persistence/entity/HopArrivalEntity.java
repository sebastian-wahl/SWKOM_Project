package at.fhtw.swen3.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * HopArrival
 */

@Getter
@Setter
@AllArgsConstructor
@Builder
public class HopArrivalEntity {
    private String code;

    private String description;

    private OffsetDateTime dateTime;
}

