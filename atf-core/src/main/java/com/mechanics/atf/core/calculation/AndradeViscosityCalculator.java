package com.mechanics.atf.core.calculation;

import com.mechanics.atf.core.exception.ViscosityNotComputableException;
import com.mechanics.atf.core.model.FluidSpec;
import com.mechanics.atf.core.model.KinematicViscosity;
import com.mechanics.atf.core.model.Temperature;
import com.mechanics.atf.core.model.ViscosityResult;

public class AndradeViscosityCalculator implements ViscosityCalculator {

    @Override
    public ViscosityResult viscosityAt(FluidSpec spec, Temperature temperature) {

        double viscosityR1 = spec.firstReferencePoint().kinematicViscosity().centistokes();
        double viscosityR2 = spec.secondReferencePoint().kinematicViscosity().centistokes();
        double tempR1 = spec.firstReferencePoint().temperature().toKelvin();
        double tempR2 = spec.secondReferencePoint().temperature().toKelvin();

        boolean isInRange = spec.validatedRange().contains(temperature);

        double B = Math.log(viscosityR1 / viscosityR2) / (1.0 / tempR1 - 1.0 / tempR2);
        double A = Math.log(viscosityR1) - (B / tempR1);
        double tTargetK = temperature.toKelvin();
        double vTargetTemp = Math.exp(A + (B / tTargetK));

        KinematicViscosity viscosity = new KinematicViscosity(vTargetTemp);

        if (isInRange) {
            return new ViscosityResult(viscosity, ViscosityResult.Status.VALID);
        } else {
            return new ViscosityResult(viscosity, ViscosityResult.Status.EXTRAPOLATED);
        }
    }
}
