package isdcm.api.exceptions;

public class VideoModelException extends Exception {
    
    public enum VideoErrorCode {
        VIDEO_TITULO_REQUIRED,
        VIDEO_AUTOR_REQUIRED,
        VIDEO_FECHA_CREACION_REQUIRED,
        VIDEO_FECHA_CREACION_INVALID,
        VIDEO_DURACION_REQUIRED,
        VIDEO_DURACION_INVALID,
        VIDEO_REPRODUCCIONES_NEGATIVE,
        VIDEO_DESCRIPCION_REQUIRED,
        VIDEO_FORMATO_REQUIRED,
    }
    
    public VideoModelException(VideoErrorCode errorCode) {
        super( errorCode.toString());
    }
}
