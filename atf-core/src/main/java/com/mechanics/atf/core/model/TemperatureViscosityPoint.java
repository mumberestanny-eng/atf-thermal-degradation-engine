package com.mechanics.atf.core.model;


import java.util.Objects;

public record TemperatureViscosityPoint(Temperature temperature, KinematicViscosity kinematicViscosity) {

    public TemperatureViscosityPoint {

        Objects.requireNonNull(temperature, "Point temperature cannot be null");
        Objects.requireNonNull(kinematicViscosity, "Kinematic viscosity cannot be null");
    }
}
