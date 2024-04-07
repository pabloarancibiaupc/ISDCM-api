package isdcm.api.exceptions;

public class ExistingVideoException extends Exception {
    
    public ExistingVideoException() {
        super("EXISTING_VIDEO");
    }
    
    public ExistingVideoException(Throwable cause) {
        super("EXISTING_VIDEO", cause);
    }
}
