package nl.workshophub.workshophubeindopdrachtbackend.exceptions;

public class VariableCannotBeEmptyException extends RuntimeException {
    public VariableCannotBeEmptyException() {
        super();
    }

    public VariableCannotBeEmptyException(String message) {
        super(message);
    }
}
