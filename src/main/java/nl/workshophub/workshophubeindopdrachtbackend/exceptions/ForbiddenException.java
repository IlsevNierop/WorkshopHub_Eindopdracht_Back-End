package nl.workshophub.workshophubeindopdrachtbackend.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
    }
    public ForbiddenException(String message) {
        super(message);
    }
}
