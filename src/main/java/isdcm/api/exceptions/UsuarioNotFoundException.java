package isdcm.api.exceptions;

public class UsuarioNotFoundException extends Exception {
    
    public final static String ERROR_CODE = "USUARIO_NOT_FOUND";
    
    public UsuarioNotFoundException() {
        super(ERROR_CODE);
    }
    
    public UsuarioNotFoundException(Throwable cause) {
        super(ERROR_CODE, cause);
    }
}
