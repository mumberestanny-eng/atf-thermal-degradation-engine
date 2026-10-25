package com.mechanics.atf.core;

public record FluidSpec (String name, Temperature initialTemperate, double viscosityAtInitialTemperature,
                         Temperature maxTemperature, double viscosityAtMaxTemperature) {


    public FluidSpec {
        if (name == null || name.isBlank()) throw invalidFluidSpec("Fluid name cannot be null or blank", name);
        if (viscosityAtMaxTemperature >= viscosityAtInitialTemperature)
            throw invalidFluidSpec("Viscosity at max temperature must be less than viscosity at initial temperature", viscosityAtMaxTemperature);
        if (initialTemperate.celsius() >= maxTemperature.celsius())
            throw invalidFluidSpec("Initial temperature must be less than max temperature", initialTemperate);
    }


    public IllegalArgumentException invalidFluidSpec( String message, Object value) {
        return new IllegalArgumentException(
                message + " at value " + value
        );
    }

}
