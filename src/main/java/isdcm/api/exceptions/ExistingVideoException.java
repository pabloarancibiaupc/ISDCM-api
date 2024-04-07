package isdcm.api.exceptions;

public class ExistingVideoException extends Exception {
    
    public final static String ERROR_CODE = "EXISTING_VIDEO";
    
    public ExistingVideoException() {
        super(ERROR_CODE);
    }
    
    public ExistingVideoException(Throwable cause) {
        super(ERROR_CODE, cause);
    }
}
