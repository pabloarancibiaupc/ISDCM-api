package isdcm.api.exceptions;

public class UsuarioModelException extends Exception {
    
    public enum UsuarioErrorCode {
        USUARIO_NOMBRE_REQUIRED,
        USUARIO_APELLIDO_REQUIRED,
        USUARIO_EMAIL_REQUIRED,
        USUARIO_EMAIL_INVALID,
        USUARIO_USERNAME_REQUIRED,
        USUARIO_PASSWORD_REQUIRED;        
    }
    
    public UsuarioModelException(UsuarioErrorCode errorCode) {
        super( errorCode.toString());
    }
}