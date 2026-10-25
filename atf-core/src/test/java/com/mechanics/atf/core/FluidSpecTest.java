package com.mechanics.atf.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class FluidSpecTest
{
    @Test
    void validFluidSpec() {
        assertDoesNotThrow(() -> new FluidSpec("Toyota Guinine ATF WS", new Temperature(40.0),
                23.0, new Temperature(100.0), 5.30)); // ExxonMobil May 2020 datasheet

        assertDoesNotThrow(() -> new FluidSpec("Toyota Guinine ATF WS", new Temperature(20.0),
                10.0, new Temperature(150.0), 5.0)); // Assumed
    }

    @Test
    void invalidFluidSpec() {
        assertThrows(IllegalArgumentException.class, () -> new FluidSpec("", new Temperature(25.0),
                1.0, new Temperature(100.0), 0.5)); // Assumed

        assertThrows(IllegalArgumentException.class, () -> new FluidSpec("Water", new Temperature(25.0),
                1.0, new Temperature(100.0), 1.5)); // Assumed

        assertThrows(IllegalArgumentException.class, () -> new FluidSpec("Water", new Temperature(100.0),
                1.0, new Temperature(25.0), 0.5));  // Assumed
    }

    @Test
    void invalidFluidSpecMessage() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new FluidSpec("", new Temperature(25.0),
                1.0, new Temperature(100.0), 0.5)); // Assumed
        assertTrue(exception.getMessage().contains("Fluid name cannot be null or blank"));
        assertTrue(true);

        exception = assertThrows(IllegalArgumentException.class, () -> new FluidSpec("Water", new Temperature(25.0),
                1.0, new Temperature(100.0), 1.5)); // Assumed
        assertTrue(exception.getMessage().contains("Viscosity at max temperature must be less than viscosity at initial temperature"));
        assertTrue(exception.getMessage().contains("1.5"));

        exception = assertThrows(IllegalArgumentException.class, () -> new FluidSpec("Water", new Temperature(100.0),
                1.0, new Temperature(25.0), 0.5));  // Assumed
        assertTrue(exception.getMessage().contains("Initial temperature must be less than max temperature"));
        assertTrue(exception.getMessage().contains("Temperature[celsius=100.0]"));
    }

}