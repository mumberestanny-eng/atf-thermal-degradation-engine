package com.mechanics.atf.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureTest
{

    @Test
    void validTemperature() {

        assertDoesNotThrow(() -> new Temperature(25.0));
        assertDoesNotThrow(() -> new Temperature(0.0));
        assertDoesNotThrow(() -> new Temperature(1000));
    }

    @Test
    void invalidTemperature() {

        assertThrows(IllegalArgumentException.class, () -> new Temperature(-274.0));
        assertThrows(IllegalArgumentException.class, () -> new Temperature(-273.15));
        assertThrows(IllegalArgumentException.class, () -> new Temperature(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> new Temperature(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> new Temperature(Double.NEGATIVE_INFINITY));
    }

    @Test
    void toKelvin () {

        assertEquals(298.15, new Temperature(25.0).toKelvin(), 0.01);
        assertEquals(273.15, new Temperature(0.0).toKelvin(), 0.01);
        assertEquals(373.15, new Temperature(100.0).toKelvin(), 0.01);
        assertEquals(313.15, new Temperature(40.0).toKelvin(), 0.01);
    }

    @Test
    void invalidTemperatureMessage() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Temperature(-274.0));
        assertTrue(exception.getMessage().contains("Temperature cannot be less than or below absolute zero"));
        assertTrue(exception.getMessage().contains("-274.0"));

        exception = assertThrows(IllegalArgumentException.class, () -> new Temperature(Double.NaN));
        assertTrue(exception.getMessage().contains("Temperature must be a finite number"));
        assertTrue(exception.getMessage().contains("NaN"));

        exception = assertThrows(IllegalArgumentException.class, () -> new Temperature(Double.POSITIVE_INFINITY));
        assertTrue(exception.getMessage().contains("Temperature must be a finite number"));
        assertTrue(exception.getMessage().contains("Infinity"));

        exception = assertThrows(IllegalArgumentException.class, () -> new Temperature(Double.NEGATIVE_INFINITY));
        assertTrue(exception.getMessage().contains("Temperature must be a finite number"));
        assertTrue(exception.getMessage().contains("-Infinity"));
    }

    @Test
    void testToKelvin() {

        Temperature temperature = new Temperature(25.0);
        double kelvin = temperature.toKelvin();
        assertEquals(298.15, kelvin, 0.01);
        assertEquals(298.15, new Temperature(25.0).toKelvin(), 0.01);
    }

    @Test
    void testToKelvinWithNegativeTemperature() {

        Temperature temperature = new Temperature(-100.0);
        double kelvin = temperature.toKelvin();
        assertEquals(173.15, kelvin, 0.01);
        assertEquals(173.15, new Temperature(-100.0).toKelvin(), 0.01);
    }

}