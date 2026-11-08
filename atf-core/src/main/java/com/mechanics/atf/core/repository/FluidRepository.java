package com.mechanics.atf.core.repository;

import com.mechanics.atf.core.model.FluidSpec;
import com.mechanics.atf.core.exception.FluidNotFoundException;

public interface FluidRepository {

    FluidSpec findById(String fluidId) throws FluidNotFoundException;
}
