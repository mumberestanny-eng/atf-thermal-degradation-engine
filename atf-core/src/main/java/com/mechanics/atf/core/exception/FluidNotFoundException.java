package com.mechanics.atf.core.exception;

public class FluidNotFoundException extends AtfDegradationEngineException{

    private final String fluidId;

    public FluidNotFoundException(String fluidId){
        super(String.format("Fluid specification with ID '%s' could not be found.", fluidId));
        this.fluidId = fluidId;
    }

    public String getFluidId() { return fluidId; }
}
