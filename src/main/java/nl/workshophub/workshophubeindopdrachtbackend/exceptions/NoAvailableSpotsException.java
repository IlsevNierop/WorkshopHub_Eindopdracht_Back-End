package nl.workshophub.workshophubeindopdrachtbackend.exceptions;

public class NoAvailableSpotsException extends RuntimeException {
    public NoAvailableSpotsException() {
    }

    public NoAvailableSpotsException(String message) {
        super(message);
    }
}
