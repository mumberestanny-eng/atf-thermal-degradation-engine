package com.mechanics.atf.core.service;

import com.mechanics.atf.core.calculation.AndradeViscosityCalculator;
import com.mechanics.atf.core.calculation.ViscosityCalculator;
import com.mechanics.atf.core.exception.FluidNotFoundException;
import com.mechanics.atf.core.exception.SensorReadException;
import com.mechanics.atf.core.model.*;
import com.mechanics.atf.core.repository.FluidRepository;
import com.mechanics.atf.core.sensor.ThermalSensor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ViscosityMonitorTest {

    // --- HAND-WRITTEN FAKES ---

    static class FakeFluidRepository implements FluidRepository {
        private final FluidSpec specToReturn;

        FakeFluidRepository(FluidSpec specToReturn) {
            this.specToReturn = specToReturn;
        }

        @Override
        public FluidSpec findById(String id) throws FluidNotFoundException {
            if (specToReturn == null) {
                throw new FluidNotFoundException(id);
            }
            return specToReturn;
        }
    }

    static class FakeThermalSensor implements ThermalSensor {
        private final Temperature temperatureToReturn;
        private final boolean shouldFail;

        FakeThermalSensor(Temperature temperatureToReturn, boolean shouldFail) {
            this.temperatureToReturn = temperatureToReturn;
            this.shouldFail = shouldFail;
        }

        @Override
        public Temperature currentTemperature() throws SensorReadException {
            if (shouldFail) {
                throw new SensorReadException("Simulated hardware sensor failure.");
            }
            return temperatureToReturn;
        }
    }

    static class FakeViscosityCalculator implements ViscosityCalculator {
        private final ViscosityResult resultToReturn;
        private FluidSpec capturedSpec;
        private Temperature capturedTemperature;

        FakeViscosityCalculator(ViscosityResult resultToReturn) {
            this.resultToReturn = resultToReturn;
        }

        @Override
        public ViscosityResult viscosityAt(FluidSpec spec, Temperature temperature) {
            this.capturedSpec = spec;
            this.capturedTemperature = temperature;
            return resultToReturn;
        }

        public FluidSpec getCapturedSpec() { return capturedSpec; }
        public Temperature getCapturedTemperature() { return capturedTemperature; }
    }

    // --- SETUP FIXTURE ---

    private final String expectedId = "Test";

    private final FluidSpec sampleSpec = new FluidSpec(
            new FluidSource("OEM Spec", YearMonth.of(2021, 6)),
            expectedId,
            new TemperatureRange(new Temperature(40.0), new Temperature(100.0)),
            new TemperatureViscosityPoint(new Temperature(40.0), new KinematicViscosity(20.0)),
            new TemperatureViscosityPoint(new Temperature(100.0), new KinematicViscosity(5.0))
    );



    // --- TEST SUITE ---

    @Test
    @DisplayName("Should delegate correct spec and temperature to calculator and return exact result")
    void monitorDelegatesAndReturnsKnownResult() throws Exception {

        KinematicViscosity expectedViscosity = new KinematicViscosity(12.5);
        ViscosityResult expectedResult = new ViscosityResult(expectedViscosity, ViscosityResult.Status.VALID);
        FakeFluidRepository repo = new FakeFluidRepository(sampleSpec);
        FakeThermalSensor sensor = new FakeThermalSensor(new Temperature(65.0), false);
        FakeViscosityCalculator fakeCalculator = new FakeViscosityCalculator(expectedResult);

        ViscosityMonitor monitor = new ViscosityMonitor(sensor, repo, fakeCalculator);

        ViscosityResult actualResult = monitor.check(expectedId);
        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(fakeCalculator.getCapturedSpec()).isEqualTo(sampleSpec);
        assertThat(fakeCalculator.getCapturedTemperature().celsius()).isEqualTo(65.0);
    }

    @Test
    @DisplayName("Should propagate FluidNotFoundException containing requested ID message")
    void monitorThrowsFluidNotFoundWithCorrectMessage() {

        FakeFluidRepository repo = new FakeFluidRepository(null);
        FakeThermalSensor sensor = new FakeThermalSensor(new Temperature(70.0), false);
        ViscosityCalculator calculator = new AndradeViscosityCalculator();

        ViscosityMonitor monitor = new ViscosityMonitor(sensor, repo, calculator);


        assertThatThrownBy(() -> monitor.check("UNKNOWN-FLUID"))
                .isInstanceOf(FluidNotFoundException.class)
                .hasMessageContaining("UNKNOWN-FLUID");
    }

    @Test
    @DisplayName("Should flow EXTRAPOLATED status through monitor while preserving computed number")
    void monitorHandlesExtrapolatedTemperature() throws Exception {

        FakeFluidRepository repo = new FakeFluidRepository(sampleSpec);
        FakeThermalSensor sensor = new FakeThermalSensor(new Temperature(110.0), false);
        ViscosityCalculator realCalculator = new AndradeViscosityCalculator();

        ViscosityMonitor monitor = new ViscosityMonitor(sensor, repo, realCalculator);

        ViscosityResult result = monitor.check(expectedId);

        assertThat(result.status()).isEqualTo(ViscosityResult.Status.EXTRAPOLATED);
        assertThat(result.viscosity().centistokes()).isLessThan(5.0);
    }

    @Test
    @DisplayName("Should propagate SensorReadException when sensor fails")
    void monitorThrowsSensorReadException() {
        FakeFluidRepository repo = new FakeFluidRepository(sampleSpec);
        FakeThermalSensor sensor = new FakeThermalSensor(new Temperature(70.0), true);
        ViscosityCalculator calculator = new AndradeViscosityCalculator();

        ViscosityMonitor monitor = new ViscosityMonitor(sensor, repo, calculator);

        assertThatThrownBy(() -> monitor.check(expectedId))
                .isInstanceOf(SensorReadException.class);
    }

    @Test
    @DisplayName("Should propagate NullPointerException when check(null) is called")
    void monitorThrowsNullPointerExceptionOnNullId() {

        FakeFluidRepository repo = new FakeFluidRepository(sampleSpec);
        FakeThermalSensor sensor = new FakeThermalSensor(new Temperature(70.0), false);
        ViscosityCalculator calculator = new AndradeViscosityCalculator();

        ViscosityMonitor monitor = new ViscosityMonitor(sensor, repo, calculator);

        assertThatThrownBy(() -> monitor.check(null))
                .isInstanceOf(NullPointerException.class);
    }
}