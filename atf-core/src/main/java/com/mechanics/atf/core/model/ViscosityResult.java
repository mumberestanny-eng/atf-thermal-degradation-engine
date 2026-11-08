package com.mechanics.atf.core.model;


import java.util.Objects;

public record ViscosityResult(KinematicViscosity viscosity, Status status) {


    public ViscosityResult {
        Objects.requireNonNull(viscosity, "Viscosity cannot be null");
        Objects.requireNonNull(status, "Status cannot be null");
    }

    public enum Status{VALID, EXTRAPOLATED}

}
