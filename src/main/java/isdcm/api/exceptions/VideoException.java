package isdcm.api.exceptions;

public class VideoException extends Exception {
    
    public enum VideoError {
        VIDEO_ID_REQUIRED,
        VIDEO_ID_INVALID,
        VIDEO_TITULO_REQUIRED,
        VIDEO_AUTOR_REQUIRED,
        VIDEO_FECHA_CREACION_REQUIRED,
        VIDEO_FECHA_CREACION_INVALID,
        VIDEO_DURACION_REQUIRED,
        VIDEO_DURACION_INVALID,
        VIDEO_REPRODUCCIONES_INVALID,
        VIDEO_DESCRIPCION_REQUIRED,
        VIDEO_FORMATO_REQUIRED;
    }
    
    public VideoException(VideoError error) {
        super( error.toString());
    }
    
    public VideoException(VideoError error, Throwable cause) {
        super( error.toString(), cause);
    }
}
