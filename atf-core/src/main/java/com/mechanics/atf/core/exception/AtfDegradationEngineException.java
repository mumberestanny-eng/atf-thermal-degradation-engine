package com.mechanics.atf.core.exception;

public abstract class AtfDegradationEngineException extends Exception {

    protected AtfDegradationEngineException(String errorMessage){
        super(errorMessage);
    }
}
