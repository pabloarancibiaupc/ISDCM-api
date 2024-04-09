package isdcm.api.models;

import isdcm.api.exceptions.UsuarioModelException;
import isdcm.api.exceptions.UsuarioModelException.UsuarioErrorCode;

public class Usuario {
    
    private static final String EMAIL_REGEX = "^[a-z0-9_+-]+(?:\\.[a-z0-9_+-]+)*@(?:[a-z0-9-]+\\.)+[a-z]{2,7}$";
    private static final String USERNAME_REGEX = "^[a-zA-Z][a-zA-Z0-9_-]{2,25}$*";
    
    private Integer id;
    private String nombre;
    private String apellido;
    private String email;
    private String username;
    private String password;
    
    public Usuario(String username) throws UsuarioModelException {
        this.id = null;
        this.nombre = null;
        this.apellido = null;
        this.email = null;
        if (username == null || username.isBlank()) {
            throw new UsuarioModelException(UsuarioErrorCode.USUARIO_USERNAME_REQUIRED);
        }
        if (!username.matches(USERNAME_REGEX)) {
            throw new UsuarioModelException(UsuarioErrorCode.USUARIO_USERNAME_INVALID);
        }
        this.username = username.trim();
        this.password = null;
    }
    
    private Usuario(String nombre, String apellido, String email, String username) throws UsuarioModelException {
        this(username);
        if (nombre == null || nombre.isBlank()) {
            throw new UsuarioModelException(UsuarioErrorCode.USUARIO_NOMBRE_REQUIRED);
        }
        this.nombre = nombre.trim();
        if (apellido == null || apellido.isBlank()) {
            throw new UsuarioModelException(UsuarioErrorCode.USUARIO_APELLIDO_REQUIRED);
        }
        this.apellido = apellido.trim();
        if (email == null || email.isBlank()) {
            throw new UsuarioModelException(UsuarioErrorCode.USUARIO_EMAIL_REQUIRED);
        }
        email = email.toLowerCase().trim();
        if (!email.matches(EMAIL_REGEX)) {
            throw new UsuarioModelException(UsuarioErrorCode.USUARIO_EMAIL_INVALID);
        }
        this.email = email;
    }
    
    public Usuario(String nombre, String apellido, String email, String username, String password) throws UsuarioModelException {
        this(nombre, apellido, email, username);
        if (password == null || password.isBlank()) {
            throw new UsuarioModelException(UsuarioErrorCode.USUARIO_PASSWORD_REQUIRED);
        }
        if (password.length() < 8 || !validatePassword(password)) {
            throw new UsuarioModelException(UsuarioErrorCode.USUARIO_PASSWORD_INVALID);
        }
        this.password = password;
    }
    
    public Usuario(Integer id, String nombre, String apellido, String email, String username) throws UsuarioModelException {
        this(nombre, apellido, email, username);
        setId(id);
    }
    
    private static boolean validatePassword(String password) {
        boolean lowercase = false;
        boolean uppercase = false;
        boolean digit = false;
        for (int i = 0; i < password.length(); ++i) {
            char c = password.charAt(i);
            if (Character.isLowerCase(c)) {
                lowercase = true;
            } else if (Character.isUpperCase(c)) {
                uppercase = true;
            } else if (Character.isDigit(c)) {
                digit = true;
            }
        }
        return lowercase & uppercase & digit;
    }
    
    // Getters
    public Integer getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public String getEmail() {
        return email;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    
    // Setters
    public final void setId(Integer id) throws UsuarioModelException {
        if (id == null) {
            throw new UsuarioModelException(UsuarioErrorCode.USUARIO_ID_REQUIRED);
        }
        if (id < 0) {
            throw new UsuarioModelException(UsuarioErrorCode.USUARIO_ID_INVALID);
        }
        this.id = id;
    }
}
