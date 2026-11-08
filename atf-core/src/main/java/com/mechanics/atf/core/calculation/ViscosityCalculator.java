package com.mechanics.atf.core.calculation;

import com.mechanics.atf.core.exception.ViscosityNotComputableException;
import com.mechanics.atf.core.model.FluidSpec;
import com.mechanics.atf.core.model.Temperature;
import com.mechanics.atf.core.model.ViscosityResult;

public interface ViscosityCalculator {

    ViscosityResult viscosityAt(FluidSpec spec, Temperature temperature) throws ViscosityNotComputableException;
}
