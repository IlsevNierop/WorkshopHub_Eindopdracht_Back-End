package nl.workshophub.workshophubeindopdrachtbackend.exceptions;

public class NoAvailableSpotsException extends RuntimeException {
    public NoAvailableSpotsException(String message) {
        super(message);
    }
}
