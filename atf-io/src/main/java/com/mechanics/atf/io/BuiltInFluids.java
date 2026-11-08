package com.mechanics.atf.io;

import com.mechanics.atf.core.model.FluidSpec;

import com.mechanics.atf.core.model.*;

import java.time.YearMonth;
import java.util.Map;

public final class BuiltInFluids {

    private BuiltInFluids() {}

    public static final String TOYOTA_WS_ID = "TOYOTA-WS";

    public static final FluidSpec TOYOTA_GENUINE_ATF_WS = new FluidSpec(
            new FluidSource("Toyota ATF WS datasheet", YearMonth.of(2020, 5)),
            "Toyota WS ATF",
            new TemperatureRange(new Temperature(40.0), new Temperature(100.0)),
            new TemperatureViscosityPoint(new Temperature(40.0), new KinematicViscosity(23.0)),
            new TemperatureViscosityPoint(new Temperature(100.0), new KinematicViscosity(5.30))
    );


    /**
     * Provides an unmodifiable pre-populated registry of all OEM fluid specs.
     */
    public static Map<String, FluidSpec> defaultRegistry() {
        return Map.of(
                TOYOTA_WS_ID, TOYOTA_GENUINE_ATF_WS
        );
    }
}