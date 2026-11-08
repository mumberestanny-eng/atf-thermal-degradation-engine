package com.mechanics.atf.core.model;


public record KinematicViscosity(double centistokes) {

    private static final double MIN_VISCOSITY = 0.0;

    public KinematicViscosity{

        if (!Double.isFinite(centistokes))
            throw new IllegalArgumentException("Kinematic viscosity must be a finite number");

        if (centistokes <= MIN_VISCOSITY)
            throw new IllegalArgumentException("Kinematic viscosity cannot be equal or below 0.0");
    }

}
