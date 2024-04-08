package isdcm.api.exceptions;

public class UsuarioConflictException extends Exception {
    
    public final static String ERROR_CODE = "USUARIO_CONFLICT";
    
    public UsuarioConflictException() {
        super(ERROR_CODE);
    }
    
    public UsuarioConflictException(Throwable cause) {
        super(ERROR_CODE, cause);
    }
}
