package isdcm.api.models;

public class Usuario {
    Integer id;
    String nombre;
    String apellido;
    String email;
    String username;
    String password;
    
    public Usuario(String nombre, String apellido, String email, String username, String password) {
        this.id = null;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.username = username;
        this.password = password;
    }
    
    public Usuario(int id, String nombre, String apellido, String email, String username) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.username = username;
        this.password = null;
    }
    
    // Getters
    public int getId() {
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
    public void setId(int id) {
        this.id = id;
    }
}
