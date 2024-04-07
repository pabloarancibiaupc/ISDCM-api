package isdcm.api.models;

public class Usuario {
    Integer id;
    String nombre;
    String apellido;
    String email;
    String username;
    String password;
    
    public Usuario(String nombre, String apellido, String email, String username, String password) {
        this(null, nombre, apellido, email, username, password);
    }
    
    public Usuario(Integer id, String nombre, String apellido, String email, String username) {
        this(id, nombre, apellido, email, username, null);
    }
    
    public Usuario(Integer id, String nombre, String apellido, String email, String username, String password) {
        this.id = id;
        this.nombre = nombre.trim();
        this.apellido = apellido.trim();
        this.email = email.toLowerCase().trim();
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
