package com.mechanics.atf.io;

import com.mechanics.atf.core.exception.SensorReadException;
import com.mechanics.atf.core.model.Temperature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SimulatedThermalSensorTest {

    @Test
    @DisplayName("Should iterate sequentially through scripted temperature readings")
    void currentTemperatureSequencing() throws SensorReadException {
        List<Temperature> readings = List.of(
                new Temperature(40.0),
                new Temperature(70.0),
                new Temperature(100.0)
        );
        SimulatedThermalSensor sensor = new SimulatedThermalSensor(readings);

        assertThat(sensor.currentTemperature().celsius()).isEqualTo(40.0);
        assertThat(sensor.currentTemperature().celsius()).isEqualTo(70.0);
        assertThat(sensor.currentTemperature().celsius()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("Should throw SensorReadException when scripted readings are exhausted")
    void currentTemperatureExhaustedThrowsException() throws SensorReadException {
        List<Temperature> readings = List.of(new Temperature(80.0));
        SimulatedThermalSensor sensor = new SimulatedThermalSensor(readings);

        sensor.currentTemperature();

        assertThatThrownBy(sensor::currentTemperature)
                .isInstanceOf(SensorReadException.class)
                .hasMessageContaining("exhausted");
    }

    @Test
    @DisplayName("Should throw SensorReadException immediately if initialized with empty list")
    void emptyReadingsThrowsExceptionOnFirstCall() {
        SimulatedThermalSensor sensor = new SimulatedThermalSensor(List.of());

        assertThatThrownBy(sensor::currentTemperature)
                .isInstanceOf(SensorReadException.class);
    }

    @Test
    @DisplayName("Defensive Copy Test: Modifying external list must not alter sensor readings")
    void constructorDefensiveCopy() throws SensorReadException {
        List<Temperature> mutableReadings = new ArrayList<>();
        mutableReadings.add(new Temperature(50.0));

        SimulatedThermalSensor sensor = new SimulatedThermalSensor(mutableReadings);

        mutableReadings.add(new Temperature(90.0));

        assertThat(sensor.currentTemperature().celsius()).isEqualTo(50.0);

        assertThatThrownBy(sensor::currentTemperature)
                .isInstanceOf(SensorReadException.class);
    }
}