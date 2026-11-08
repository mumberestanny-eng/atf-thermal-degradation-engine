package com.mechanics.atf.core.sensor;

import com.mechanics.atf.core.exception.SensorReadException;
import com.mechanics.atf.core.model.Temperature;

public interface ThermalSensor {

    Temperature currentTemperature() throws SensorReadException;
}
