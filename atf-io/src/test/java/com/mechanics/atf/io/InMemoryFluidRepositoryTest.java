package com.mechanics.atf.io;

import com.mechanics.atf.core.exception.FluidNotFoundException;
import com.mechanics.atf.core.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class InMemoryFluidRepositoryTest {

    private FluidSpec toyotaWsSpec;
    private Map<String, FluidSpec> initialSpecs;

    @BeforeEach
    void setUp() {
        toyotaWsSpec = BuiltInFluids.TOYOTA_GENUINE_ATF_WS;
        initialSpecs = new HashMap<>(BuiltInFluids.defaultRegistry());
    }

    @Test
    @DisplayName("Should return FluidSpec when valid ID is requested")
    void findByIdSuccess() throws FluidNotFoundException {
        InMemoryFluidRepository repository = new InMemoryFluidRepository(initialSpecs);

        FluidSpec result = repository.findById(BuiltInFluids.TOYOTA_WS_ID);

        assertThat(result).isNotNull();
        assertThat(result.fluidName()).isEqualTo("Toyota WS ATF");
    }

    @Test
    @DisplayName("Should throw FluidNotFoundException when requesting non-existent ID")
    void findByIdNotFoundThrowsException() {
        InMemoryFluidRepository repository = new InMemoryFluidRepository(initialSpecs);

        assertThatThrownBy(() -> repository.findById("NISSAN-CVT"))
                .isInstanceOf(FluidNotFoundException.class)
                .hasMessageContaining("NISSAN-CVT");
    }

    @Test
    @DisplayName("Defensive Copy Test: Modifying external map must not alter repository state")
    void constructorDefensiveCopy() throws FluidNotFoundException {
        InMemoryFluidRepository repository = new InMemoryFluidRepository(initialSpecs);

        initialSpecs.clear();

        FluidSpec result = repository.findById(BuiltInFluids.TOYOTA_WS_ID);
        assertThat(result).isEqualTo(toyotaWsSpec);
    }

    @Test
    @DisplayName("Caller Bug: Passing null ID should explicitly throw NullPointerException")
    void findByIdNullThrowsNpe() {
        InMemoryFluidRepository repository = new InMemoryFluidRepository(initialSpecs);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> repository.findById(null));
        assertTrue(exception.getMessage().contains("Fluid id cannot be null"));
    }

    @Test
    @DisplayName("Caller Bug: Passing blank string ID should throw IllegalArgumentException")
    void findByIdBlankThrowsIllegalArgument() {
        InMemoryFluidRepository repository = new InMemoryFluidRepository(initialSpecs);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repository.findById("   "));
        assertTrue(exception.getMessage().contains("Fluid id cannot be blank or empty"));
    }

}