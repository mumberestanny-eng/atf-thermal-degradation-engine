package com.mechanics.atf.core.model;

import java.util.Objects;

public record TemperatureRange(Temperature minimum, Temperature maximum) {

    public TemperatureRange {
        Objects.requireNonNull(minimum, "Minimum temperature cannot be null");
        Objects.requireNonNull(maximum, "Maximum temperature cannot be null");

        if (minimum.celsius() >= maximum.celsius()) {
            throw new IllegalArgumentException("Temperature range minimum must be less than maximum");
        }
    }

    public boolean contains(Temperature temperature) {

        Objects.requireNonNull(temperature, "Temperature cannot be null");

        return temperature.celsius() >= minimum.celsius()
                && temperature.celsius() <= maximum.celsius();
    }
}
