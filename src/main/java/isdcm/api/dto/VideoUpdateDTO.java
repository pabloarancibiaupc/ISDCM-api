package isdcm.api.dto;

public class VideoUpdateDTO {
    private String titulo;
    private String autor;
    private String fechaCreacion;
    private String duracion;
    private Integer reproducciones;
    private String descripcion;
    private String formato;
    
    // Getters
    public String getTitulo() {
        return titulo;
    }
    public String getAutor() {
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
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
    public void setReproducciones(Integer reproducciones) {
        this.reproducciones = reproducciones;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setFormato(String formato) {
        this.formato = formato;
    }
}
