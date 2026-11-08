package com.mechanics.atf.calculation;

import com.mechanics.atf.core.calculation.AndradeViscosityCalculator;
import com.mechanics.atf.core.exception.ViscosityNotComputableException;
import com.mechanics.atf.core.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.YearMonth;

import static org.assertj.core.api.AssertionsForClassTypes.*;


class AndradeViscosityCalculatorTest {

    private static final FluidSource SOURCE =
            new FluidSource("Toyota ATF WS datasheet", YearMonth.of(2020, 5));
    // source : Toyota ATF WS datasheet, fluidName: Toyota WS ATF. These are real and came from official datasheet

    private static final TemperatureRange VALID_RANGE =
            new TemperatureRange(new Temperature(40.0), new Temperature(100.0));

    private static final FluidSpec SPEC = new FluidSpec(
            SOURCE,
            "Toyota WS ATF",
            VALID_RANGE,
            new TemperatureViscosityPoint(new Temperature(40.0), new KinematicViscosity(23.0)),
            new TemperatureViscosityPoint(new Temperature(100.0), new KinematicViscosity(5.30)));

    private static final AndradeViscosityCalculator CALCULATOR = new AndradeViscosityCalculator();

    @Test
    @DisplayName("Should compute valid viscosity and status when temperature is inside the range")
    void validKinematicViscosityWithinRange() {

        double expectedFromJShell = 10.35469068985478;
        ViscosityResult result = CALCULATOR.viscosityAt(SPEC, new Temperature(70.0));

        assertThat(result.status()).isEqualTo(ViscosityResult.Status.VALID);
        assertThat(result.viscosity().centistokes()).isGreaterThan(5.30).isLessThan(23.0);
        assertThat(result.viscosity().centistokes()).isCloseTo(expectedFromJShell, offset(1e-9));
    }

    @Test
    @DisplayName("Boundary test: Exact lower limit (40.0°C) should be marked VALID")
    void exactLowerBoundaryIsValid() {
        ViscosityResult result = CALCULATOR.viscosityAt(SPEC, new Temperature(40.0));

        assertThat(result.status()).isEqualTo(ViscosityResult.Status.VALID);
        assertThat(result.viscosity().centistokes()).isCloseTo(23.0, offset(1e-9));
    }

    @Test
    @DisplayName("Boundary test: Exact upper limit (100.0°C) should be marked VALID")
    void exactUpperBoundaryIsValid() {
        ViscosityResult result = CALCULATOR.viscosityAt(SPEC, new Temperature(100.0));

        assertThat(result.status()).isEqualTo(ViscosityResult.Status.VALID);
        assertThat(result.viscosity().centistokes()).isCloseTo(5.30, offset(1e-9));
    }


    @Test
    @DisplayName("Out or range test: Math.nextDown(VALID_RANGE.minimum()) should be marked EXTRAPOLATED")
    void outOfRangeExtrapolatedStatusLower() {
        ViscosityResult result = CALCULATOR.viscosityAt(SPEC, new Temperature(Math.nextDown(VALID_RANGE.minimum().celsius())));

        assertThat(result.status()).isEqualTo(ViscosityResult.Status.EXTRAPOLATED);
    }

    @Test
    @DisplayName("Out or range test: Math.nextDown(VALID_RANGE.maximum()) should be marked EXTRAPOLATED")
    void outOfRangeExtrapolatedStatusUpper() {
        ViscosityResult result = CALCULATOR.viscosityAt(SPEC, new Temperature(Math.nextUp(VALID_RANGE.maximum().celsius())));

        assertThat(result.status()).isEqualTo(ViscosityResult.Status.EXTRAPOLATED);
    }
    @Test
    @DisplayName("Out of range test: Temperature above upper limit (110.0°C) should be OUT_OF_RANGE")
    void temperatureAboveLimitIsExtrapolated() {
        double expectedFromJShell = 4.339541742632652;
        ViscosityResult result = CALCULATOR.viscosityAt(SPEC, new Temperature(110.0));

        assertThat(result.status()).isEqualTo(ViscosityResult.Status.EXTRAPOLATED);
        assertThat(result.viscosity().centistokes()).isCloseTo(expectedFromJShell, offset(1e-9));
    }

    @Test
    @DisplayName("Out of range test: Temperature below lower limit (30.0°C) should be OUT_OF_RANGE")
    void temperatureBelowLimitIsExtrapolated() {
        double expectedFromJShell = 31.08150509010328;
        ViscosityResult result = CALCULATOR.viscosityAt(SPEC, new Temperature(30.0));

        assertThat(result.status()).isEqualTo(ViscosityResult.Status.EXTRAPOLATED);
        assertThat(result.viscosity().centistokes()).isCloseTo(expectedFromJShell, offset(1e-9));

    }

    /*@Test
    @DisplayName("Extreme out of range test: Math.nextUp(-273.15)°C should throw ViscosityNotComputableException")
    void extremeOutOfRangeThrowsException() {
        assertThatThrownBy(() -> CALCULATOR.viscosityAt(SPEC, new Temperature(Math.nextUp(-273.15))))
                .isInstanceOf(ViscosityNotComputableException.class)
                .hasMessageContaining("Viscosity cannot be computed at");

    }*/

}