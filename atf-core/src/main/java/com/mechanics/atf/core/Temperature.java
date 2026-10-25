package com.mechanics.atf.core;

public record Temperature(double celsius) {

    private static final double ABSOLUTE_ZERO = -273.15;

    public Temperature {
        if (!Double.isFinite(celsius)) {
            throw invalidTemperature("Temperature must be a finite number", celsius);
        }

        if (celsius <= ABSOLUTE_ZERO) {
            throw invalidTemperature("Temperature cannot be less than or below absolute zero", celsius);
        }
    }

    public double toKelvin() { return celsius + 273.15; }

    private IllegalArgumentException invalidTemperature(String message, double value) {
        return new IllegalArgumentException(
                message + " at value " + value
        );
    }
}
