package com.mechanics.atf.core;

import com.mechanics.atf.core.model.Temperature;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemperatureTest {

    @Test
    void validTemperature() {
        assertDoesNotThrow(() -> new Temperature(25.0));
        assertDoesNotThrow(() -> new Temperature(0.0));
        assertDoesNotThrow(() -> new Temperature(Math.nextUp(-273.15)));
        assertDoesNotThrow(() -> new Temperature(Math.nextDown(0.0)));
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
    void toKelvin() {

        assertThat(new Temperature(25.0).toKelvin()).isCloseTo(298.15, offset(1e-12));
        assertThat(new Temperature(0.0).toKelvin()).isCloseTo(273.15, offset(1e-12));
        assertThat(new Temperature(100.0).toKelvin()).isCloseTo(373.15, offset(1e-12));
        assertThat(new Temperature(40.0).toKelvin()).isCloseTo(313.15, offset(1e-12));
        assertThat(new Temperature(-100.0).toKelvin()).isCloseTo(173.15, offset(1e-12));
    }

    @Test
    void invalidTemperatureMessage() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Temperature(-274.0));
        assertTrue(exception.getMessage().contains("Temperature cannot be less than absolute zero"));
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

}