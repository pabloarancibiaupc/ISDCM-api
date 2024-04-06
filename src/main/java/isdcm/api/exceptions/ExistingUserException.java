package isdcm.api.exceptions;

public class ExistingUserException extends Exception {
    
    public ExistingUserException() {
        super("Existing user");
    }
    
    public ExistingUserException(Throwable cause) {
        super("Existing user", cause);
    }
}
