package com.mechanics.atf.core.model;

import java.util.Objects;

/**
 * A fluid's source, validated temperature range, and two reference points used
 * to fit its viscosity behaviour.
 */

public record FluidSpec(
        FluidSource source,
        String fluidName,
        TemperatureRange validatedRange,
        TemperatureViscosityPoint firstReferencePoint,
        TemperatureViscosityPoint secondReferencePoint) {

    public FluidSpec {
        Objects.requireNonNull(source, "Source cannot be null");

        if (fluidName == null || fluidName.isBlank()) {
            throw new IllegalArgumentException("Fluid name cannot be null or blank");
        }

        Objects.requireNonNull(validatedRange, "Validated range cannot be null");
        Objects.requireNonNull(firstReferencePoint, "First reference point cannot be null");
        Objects.requireNonNull(secondReferencePoint, "Second reference point cannot be null");

        if (firstReferencePoint.temperature().celsius()
                >= secondReferencePoint.temperature().celsius()) {
            throw new IllegalArgumentException(
                    "First reference point temperature must be less than second reference point temperature");
        }
        if (!validatedRange.contains(firstReferencePoint.temperature())
                || !validatedRange.contains(secondReferencePoint.temperature())) {
            throw new IllegalArgumentException("Reference point temperatures must be within the validated range");
        }


        if ((secondReferencePoint.kinematicViscosity().centistokes()) >= (firstReferencePoint.kinematicViscosity().centistokes())) {
            throw new IllegalArgumentException(
                    "Second reference point viscosity must be less than first reference point viscosity");
        }
    }
}