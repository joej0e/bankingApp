package spring.exception;

public class LoginAlreadyExistsException extends Exception {
    public LoginAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}



