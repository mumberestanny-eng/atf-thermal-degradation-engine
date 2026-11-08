package com.mechanics.atf.core.service;

import com.mechanics.atf.core.calculation.ViscosityCalculator;
import com.mechanics.atf.core.exception.AtfDegradationEngineException;
import com.mechanics.atf.core.model.FluidSpec;
import com.mechanics.atf.core.model.Temperature;
import com.mechanics.atf.core.model.ViscosityResult;
import com.mechanics.atf.core.repository.FluidRepository;
import com.mechanics.atf.core.sensor.ThermalSensor;

import java.util.Objects;

public class ViscosityMonitor {

    private final FluidRepository fluidRepository;
    private final ThermalSensor thermalSensor;
    private final ViscosityCalculator viscosityCalculator;


    public ViscosityMonitor(ThermalSensor sensor, FluidRepository repository, ViscosityCalculator calculator) {

        Objects.requireNonNull(sensor, "ThermalSensor cannot be null.");
        Objects.requireNonNull(repository, "FluidRepository cannot be null.");
        Objects.requireNonNull(calculator, "ViscosityCalculator cannot be null.");

        this.thermalSensor = sensor;
        this.fluidRepository = repository;
        this.viscosityCalculator = calculator;
    }

    public ViscosityResult check(String fluidId) throws AtfDegradationEngineException{

        Objects.requireNonNull(fluidId, "The fluid id must be not null");

        FluidSpec spec = fluidRepository.findById(fluidId);

        Temperature currentTemp = thermalSensor.currentTemperature();

        return viscosityCalculator.viscosityAt(spec, currentTemp);
    }
}
