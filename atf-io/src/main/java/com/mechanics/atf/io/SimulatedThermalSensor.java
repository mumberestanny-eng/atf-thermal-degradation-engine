package com.mechanics.atf.io;

import com.mechanics.atf.core.exception.SensorReadException;
import com.mechanics.atf.core.model.Temperature;
import com.mechanics.atf.core.sensor.ThermalSensor;

import java.util.List;
import java.util.Objects;

/**
SimulatedSensor is a simulated sensor that simulates a thermal sensor by providing a predefined sequence of temperature readings.
It implements the ThermalSensor interface and throws a SensorReadException when the readings are exhausted.
So, it's not thread safe because of the non-atomic nature of index++.
 */

public class SimulatedThermalSensor implements ThermalSensor {

    private final List<Temperature> scriptedReadings;
    private int index = 0;


    public SimulatedThermalSensor(List<Temperature> scriptedReadings) {

        Objects.requireNonNull(scriptedReadings, "Scripted Readings cannot be null");
        this.scriptedReadings = List.copyOf(scriptedReadings);
    }

    @Override
    public Temperature currentTemperature() throws SensorReadException {
        if (index >= scriptedReadings.size()) {
            throw new SensorReadException("Thermal sensor readings exhausted.");
        }
        return scriptedReadings.get(index++);
    }

}