package isdcm.api.exceptions;

public class UsuarioException extends Exception {
    
    public enum UsuarioError {
        USUARIO_ID_REQUIRED,
        USUARIO_ID_INVALID,
        USUARIO_NOMBRE_REQUIRED,
        USUARIO_APELLIDO_REQUIRED,
        USUARIO_EMAIL_REQUIRED,
        USUARIO_EMAIL_INVALID,
        USUARIO_USERNAME_REQUIRED,
        USUARIO_USERNAME_INVALID,
        USUARIO_PASSWORD_REQUIRED,
        USUARIO_PASSWORD_INVALID;
    }
    
    public UsuarioException(UsuarioError error) {
        super( error.toString());
    }
}
