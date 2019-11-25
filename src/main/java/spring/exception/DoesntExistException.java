package spring.exception;

public class DoesntExistException extends Exception {
    public DoesntExistException(String errorMessage) {
        super(errorMessage);
    }
}



