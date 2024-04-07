package isdcm.api.exceptions;

public class VideoNotFoundException extends Exception {
    
    public VideoNotFoundException() {
        super("VIDEO_NOT_FOUND");
    }
    
    public VideoNotFoundException(Throwable cause) {
        super("VIDEO_NOT_FOUND", cause);
    }
}
