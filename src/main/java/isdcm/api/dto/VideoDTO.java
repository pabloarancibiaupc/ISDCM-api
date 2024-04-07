package isdcm.api.dto;

public class VideoDTO {
    Integer id;
    String titulo;
    UsuarioDTO autor;
    String fechaCreacion;
    String duracion;
    int reproducciones;
    String descripcion;
    String formato;
    
    // Getters
    public Integer getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public UsuarioDTO getAutor() {
        return autor;
    }
    public String getFechaCreacion() {
        return fechaCreacion;
    }
    public String getDuracion() {
        return duracion;
    }
    public int getReproducciones() {
        return reproducciones;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getFormato() {
        return formato;
    }
    
    // Setters
    public void setId(Integer id) {
        this.id = id;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setAutor(UsuarioDTO autor) {
        this.autor = autor;
    }
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
    public void setReproducciones(int reproducciones) {
        this.reproducciones = reproducciones;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setFormato(String formato) {
        this.formato = formato;
    }
}
