package isdcm.api.exceptions;

public class UsuarioConflictException extends Exception {
    
    public enum UsuarioConflictError {
        EXISTING_USUARIO;
    }
    
    public UsuarioConflictException(UsuarioConflictError error) {
        super(error.toString());
    }
    
    public UsuarioConflictException(UsuarioConflictError error, Throwable cause) {
        super(error.toString(), cause);
    }
}
