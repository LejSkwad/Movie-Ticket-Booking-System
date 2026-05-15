package com.mtb.cinemaservice.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SeatType {
    STANDARD, VIP, SWEETBOX, FOUR_DX;

    @JsonValue
    public String toJson() {
        return this == FOUR_DX ? "4DX" : name();
    }

    @JsonCreator
    public static SeatType fromJson(String value) {
        return "4DX".equals(value) ? FOUR_DX : valueOf(value);
    }
}
