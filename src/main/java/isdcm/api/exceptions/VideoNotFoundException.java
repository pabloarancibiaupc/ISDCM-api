package isdcm.api.exceptions;

public class VideoNotFoundException extends Exception {
    
    public final static String ERROR_CODE = "VIDEO_NOT_FOUND";
    
    public VideoNotFoundException() {
        super(ERROR_CODE);
    }
    
    public VideoNotFoundException(Throwable cause) {
        super(ERROR_CODE, cause);
    }
}
