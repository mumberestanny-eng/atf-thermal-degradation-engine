package com.mechanics.atf.app;

import com.mechanics.atf.core.calculation.AndradeViscosityCalculator;
import com.mechanics.atf.core.calculation.ViscosityCalculator;
import com.mechanics.atf.core.exception.AtfDegradationEngineException;
import com.mechanics.atf.core.model.Temperature;
import com.mechanics.atf.core.model.ViscosityResult;
import com.mechanics.atf.core.repository.FluidRepository;
import com.mechanics.atf.core.sensor.ThermalSensor;
import com.mechanics.atf.core.service.ViscosityMonitor;
import com.mechanics.atf.io.BuiltInFluids;
import com.mechanics.atf.io.InMemoryFluidRepository;
import com.mechanics.atf.io.SimulatedThermalSensor;

import java.util.List;
import java.util.Locale;

public final class Main {
    private Main() {}

    public static void main(String[] args) {
        FluidRepository repository = new InMemoryFluidRepository(BuiltInFluids.defaultRegistry());
        ThermalSensor sensor = new SimulatedThermalSensor(List.of(
                new Temperature(70.0), new Temperature(100.0), new Temperature(110.0)));
        ViscosityCalculator calculator = new AndradeViscosityCalculator();

        ViscosityMonitor monitor = new ViscosityMonitor(sensor, repository, calculator);

        for (int i = 0; i < 3; i++) {
            try {
                ViscosityResult r = monitor.check(BuiltInFluids.TOYOTA_WS_ID);
                System.out.printf(Locale.ROOT, "%.2f cSt  [%s]%n", r.viscosity().centistokes(), r.status());
            } catch (AtfDegradationEngineException e) {
                System.err.println("Check failed: " + e.getMessage());
            }
        }
    }
}