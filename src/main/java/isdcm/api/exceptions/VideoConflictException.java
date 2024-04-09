package isdcm.api.exceptions;

public class VideoConflictException extends Exception {
    
    public enum VideoConflictError {
        EXISTING_VIDEO,
        AUTOR_NOT_EXISTS;
    }
    
    public VideoConflictException(VideoConflictError error) {
        super(error.toString());
    }
    
    public VideoConflictException(VideoConflictError error, Throwable cause) {
        super(error.toString(), cause);
    }
}
