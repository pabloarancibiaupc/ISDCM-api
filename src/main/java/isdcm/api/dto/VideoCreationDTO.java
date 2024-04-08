package isdcm.api.dto;

public class VideoCreationDTO {
    private String titulo;
    private String autor;
    private String duracion;
    private String descripcion;
    private String formato;
    
    // Getters
    public String getTitulo() {
        return titulo;
    }
    public String getAutor() {
        return autor;
    }
    public String getDuracion() {
        return duracion;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getFormato() {
        return formato;
    }
    
    // Setters
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setFormato(String formato) {
        this.formato = formato;
    }
}
