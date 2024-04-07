package isdcm.api.exceptions;

public class ExistingUsuarioException extends Exception {
    
    public ExistingUsuarioException() {
        super("EXISTING_USUARIO");
    }
    
    public ExistingUsuarioException(Throwable cause) {
        super("EXISTING_USUARIO", cause);
    }
}
