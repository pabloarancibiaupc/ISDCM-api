package isdcm.api.dto;

public class UsuarioDTO {
    private Integer id;
    private String nombre;
    private String apellido;
    private String email;
    private String username;
    
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
    
    // Setters
    public void setId(Integer id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
