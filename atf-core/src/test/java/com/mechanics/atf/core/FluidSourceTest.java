package com.mechanics.atf.core;

import com.mechanics.atf.core.model.FluidSource;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class FluidSourceTest {

    @Test
    void rejectsIncompleteFluidSourceDate() {
        assertThrows(NullPointerException.class, () -> new FluidSource(null, null));
        assertThrows(NullPointerException.class, () -> new FluidSource("ExonMobil ATF WS datasheet", null));
        assertThrows(NullPointerException.class, () -> new FluidSource(null, YearMonth.of(2020, 5)));
    }

    @Test
    void validFluidSource() {
        assertDoesNotThrow(() -> new FluidSource("ExxonMobil ATF WS datasheet", YearMonth.of(2020, 5)));
    }

    @Test
    void shouldThrowMessageWhenBlankNameOrYear() {

        assertThatThrownBy(() -> new FluidSource("", YearMonth.of(2020, 5)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("blank");

    }
}