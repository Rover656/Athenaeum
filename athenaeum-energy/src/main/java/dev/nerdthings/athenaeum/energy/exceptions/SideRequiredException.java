package dev.nerdthings.athenaeum.energy.exceptions;

public class SideRequiredException extends RuntimeException {
    public SideRequiredException(String errorMessage) {
        super(errorMessage);
    }
}
