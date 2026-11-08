package com.mechanics.atf.io;

import com.mechanics.atf.core.exception.FluidNotFoundException;
import com.mechanics.atf.core.model.FluidSpec;
import com.mechanics.atf.core.repository.FluidRepository;
import java.util.Map;
import java.util.Objects;

public class InMemoryFluidRepository implements FluidRepository {

    private final Map<String, FluidSpec> specs;

    public InMemoryFluidRepository(Map<String, FluidSpec> specs) {

        Objects.requireNonNull(specs, "Fluid specifications cannot be null");
        this.specs = Map.copyOf(specs);
    }  // defensive copy

    @Override
    public FluidSpec findById(String id) throws FluidNotFoundException{

        Objects.requireNonNull(id, "Fluid id cannot be null");

        if (id.isBlank())
            throw  new IllegalArgumentException("Fluid id cannot be blank or empty");


        FluidSpec spec = specs.get(id);
        if (spec == null) {
            throw new FluidNotFoundException(id);
        }
        return spec;
    }
}
