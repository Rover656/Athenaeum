package dev.nerdthings.athenaeum.energy.exceptions;

public class IncompatibleEnergyException extends RuntimeException {
    public IncompatibleEnergyException() {
        super();
    }

    public IncompatibleEnergyException(String errorMessage) {
        super(errorMessage);
    }
}
