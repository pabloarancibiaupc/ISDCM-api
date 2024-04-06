package isdcm.api.exceptions;

public class SystemErrorException extends Exception {
    public SystemErrorException() {
        super("System error");
    }
    
    public SystemErrorException(Throwable cause) {
        super("System error", cause);
    }
}
