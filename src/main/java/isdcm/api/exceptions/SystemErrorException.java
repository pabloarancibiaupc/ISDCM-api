package isdcm.api.exceptions;

public class SystemErrorException extends Exception {
    
    public final static String ERROR_CODE = "SYSTEM_ERROR";
    
    public SystemErrorException() {
        super(ERROR_CODE);
    }
    
    public SystemErrorException(Throwable cause) {
        super(ERROR_CODE, cause);
    }
}
