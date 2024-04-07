package isdcm.api.exceptions;

public class UsuarioNotFoundException extends Exception {
    
    public UsuarioNotFoundException() {
        super("USUARIO_NOT_FOUND");
    }
    
    public UsuarioNotFoundException(Throwable cause) {
        super("USUARIO_NOT_FOUND", cause);
    }
}
