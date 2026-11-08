package com.mechanics.atf.core;

import com.mechanics.atf.core.model.Temperature;
import com.mechanics.atf.core.model.TemperatureRange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureRangeTest {

    @Test
    void validTemperatureRange() {
        assertDoesNotThrow(() -> new TemperatureRange(new Temperature(40.0), new Temperature(100.0)));
        assertDoesNotThrow(() -> new TemperatureRange(new Temperature(-273.14), new Temperature(273.13)));
    }

    @Test
    void invalidTemperatureRange() {
        assertThrows(IllegalArgumentException.class, () -> new TemperatureRange(new Temperature(100.0), new Temperature(40.0)));
        assertThrows(IllegalArgumentException.class, () -> new TemperatureRange(new Temperature(40.0), new Temperature(40.0)));
        assertThrows(NullPointerException.class, () -> new TemperatureRange(null, new Temperature(100.0)));
        assertThrows(NullPointerException.class, () -> new TemperatureRange(new Temperature(40.0), null));
    }

    @Test
    void flagOutsideRange() {
        TemperatureRange range = new TemperatureRange(new Temperature(40.0), new Temperature(100.0));
        assertTrue(range.contains(new Temperature(40.0)));
        assertTrue(range.contains(new Temperature(70.0)));
        assertTrue(range.contains(new Temperature(100.0)));

        assertFalse(range.contains(new Temperature(39.9)));
        assertFalse(range.contains(new Temperature(100.1)));
        assertFalse(range.contains(new Temperature(Math.nextUp(100.0))));
    }

}