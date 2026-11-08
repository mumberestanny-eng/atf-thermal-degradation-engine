package com.mechanics.atf.core.model;


import java.time.YearMonth;
import java.util.Objects;

public record FluidSource(String documentName, YearMonth date) {

    public FluidSource {
        Objects.requireNonNull(documentName, "Document name cannot be null");
        Objects.requireNonNull(date, "Document date cannot be null");


        if (documentName.isBlank()){
            throw  new NullPointerException("Document name cannot be blank");
        }
    }

}
