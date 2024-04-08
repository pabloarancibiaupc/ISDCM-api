package isdcm.api.dto;

public class VideoCreationDTO {
    String titulo;
    String autor;
    String duracion;
    String descripcion;
    String formato;
    
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
