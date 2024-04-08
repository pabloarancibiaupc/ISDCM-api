package isdcm.api.exceptions;

public class VideoConflictException extends Exception {
    
    public final static String ERROR_CODE = "VIDEO_CONFLICT";
    
    public VideoConflictException() {
        super(ERROR_CODE);
    }
    
    public VideoConflictException(Throwable cause) {
        super(ERROR_CODE, cause);
    }
}
