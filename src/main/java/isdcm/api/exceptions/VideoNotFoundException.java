package isdcm.api.exceptions;

public class VideoNotFoundException extends Exception {
    
    public VideoNotFoundException() {
        super("Video not found");
    }
    
    public VideoNotFoundException(Throwable cause) {
        super("Video not found", cause);
    }
}
