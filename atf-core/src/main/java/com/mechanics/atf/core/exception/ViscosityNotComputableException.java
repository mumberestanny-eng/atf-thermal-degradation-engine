package com.mechanics.atf.core.exception;

import com.mechanics.atf.core.model.Temperature;

import java.util.Locale;

public class ViscosityNotComputableException extends AtfDegradationEngineException{

    private final Temperature temperature;

    public ViscosityNotComputableException(Temperature temperature){
        super(String.format(Locale.ROOT, "Viscosity cannot be computed at %.2f °C", temperature.celsius()));
        this.temperature = temperature;

    }

    public Temperature getTemperature(){
        return temperature;
    }
}
