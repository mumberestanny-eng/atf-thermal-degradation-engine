package com.mechanics.atf.core;

import com.mechanics.atf.core.model.*;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class FluidSpecTest {

    private static final FluidSource SOURCE =
            new FluidSource("Toyota ATF WS datasheet", YearMonth.of(2020, 5));
    private static final TemperatureRange VALID_RANGE =
            new TemperatureRange(new Temperature(40.0), new Temperature(100.0));


    @Test
    void acceptsCompleteFluidSpec() {
        FluidSpec spec = new FluidSpec(
                SOURCE,
                "Toyota WS ATF",
                VALID_RANGE,
                new TemperatureViscosityPoint(new Temperature(40.0), new KinematicViscosity(23.0)),
                new TemperatureViscosityPoint(new Temperature(100.0), new KinematicViscosity(5.30)));

        assertEquals("Toyota ATF WS datasheet", spec.source().documentName());
        assertEquals(YearMonth.of(2020, 5), spec.source().date());
        assertEquals(VALID_RANGE, spec.validatedRange());
    }

    @Test
    void rejectsNaNViscosity() {
        assertThrows(IllegalArgumentException.class,
                () -> new TemperatureViscosityPoint(new Temperature(40.0), new KinematicViscosity(Double.NaN)));
    }

    @Test
    void rejectsNegativeViscosity() {
        assertThrows(IllegalArgumentException.class,
                () -> new TemperatureViscosityPoint(new Temperature(40.0), new KinematicViscosity(-1.0)));
    }

    @Test
    void rejectsNullTemperature() {
        assertThrows(NullPointerException.class,
                () -> new TemperatureViscosityPoint(null, new KinematicViscosity(1.0)));
    }

    @Test
    void rejectsEqualReferenceTemperatures() {
        assertThrows(IllegalArgumentException.class, () -> new FluidSpec(
                SOURCE,
                "Water",
                VALID_RANGE,
                new TemperatureViscosityPoint(new Temperature(40.0), new KinematicViscosity(10.0)),
                new TemperatureViscosityPoint(new Temperature(40.0), new KinematicViscosity(5.0))));
    }

    @Test
    void rejectsReferenceTemperaturesInReverseOrder() {
        assertThrows(IllegalArgumentException.class, () -> new FluidSpec(
                SOURCE,
                "Water",
                VALID_RANGE,
                new TemperatureViscosityPoint(new Temperature(100.0), new KinematicViscosity(10.0)),
                new TemperatureViscosityPoint(new Temperature(40.0), new KinematicViscosity(5.0))));
    }

    @Test
    void rejectsReferencePointOutsideValidatedRange() {
        assertThrows(IllegalArgumentException.class, () -> new FluidSpec(
                SOURCE,
                "Water",
                VALID_RANGE,
                new TemperatureViscosityPoint(new Temperature(10.0), new KinematicViscosity(10.0)),
                new TemperatureViscosityPoint(new Temperature(100.0), new KinematicViscosity(5.0))));
    }

    @Test
    void rejectsNonDecreasingReferenceViscosity() {
        assertThrows(IllegalArgumentException.class, () -> new FluidSpec(
                SOURCE,
                "Water",
                VALID_RANGE,
                new TemperatureViscosityPoint(new Temperature(40.0), new KinematicViscosity(5.0)),
                new TemperatureViscosityPoint(new Temperature(100.0), new KinematicViscosity(10.0))));
    }
}
