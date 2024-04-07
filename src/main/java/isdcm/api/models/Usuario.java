package isdcm.api.models;

import isdcm.api.exceptions.UsuarioModelException;
import isdcm.api.exceptions.UsuarioModelException.UsuarioErrorCode;

public class Usuario {
    private static final String EMAIL_REGEX = "^[a-z0-9_+-]+(?:\\.[a-z0-9_+-]+)*@(?:[a-z0-9-]+\\.)+[a-z]{2,7}$";
    
    private Integer id;
    private String nombre;
    private String apellido;
    private String email;
    private String username;
    private String password;
    
    public Usuario(String nombre, String apellido, String email, String username, String password) throws UsuarioModelException {
        this(null, nombre, apellido, email, username, password);
    }
    
    public Usuario(Integer id, String nombre, String apellido, String email, String username) throws UsuarioModelException {
        this(id, nombre, apellido, email, username, null);
    }
    
    public Usuario(Integer id, String nombre, String apellido, String email, String username, String password) throws UsuarioModelException {
        this.id = id;
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
        if (username == null || username.isBlank()) {
            throw new UsuarioModelException(UsuarioErrorCode.USUARIO_USERNAME_REQUIRED);
        }
        this.username = username.trim();
        this.password = password;
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
    public void setId(Integer id) {
        this.id = id;
    }
}
