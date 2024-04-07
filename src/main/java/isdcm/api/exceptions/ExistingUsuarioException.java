package isdcm.api.exceptions;

public class ExistingUsuarioException extends Exception {
    
    public final static String ERROR_CODE = "EXISTING_USUARIO";
    
    public ExistingUsuarioException() {
        super(ERROR_CODE);
    }
    
    public ExistingUsuarioException(Throwable cause) {
        super(ERROR_CODE, cause);
    }
}
