package isdcm.api.models;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Video {
    int id;
    String titulo;
    Usuario autor;
    LocalDateTime fechaCreacion;
    LocalTime duracion;
    int reproducciones;
    String descripcion;
    String formato;
    
    public Video(int id, String titulo, Usuario autor, LocalDateTime fechaCreacion, LocalTime duracion, int reproducciones, String descripcion, String formato) {
        this.id = id;
        this.autor = autor;
        this.fechaCreacion = fechaCreacion;
        this.duracion = duracion;
        this.reproducciones = reproducciones;
        this.descripcion = descripcion;
        this.formato = formato;
    }
    
    // Getters
    public int getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public Usuario getAutor() {
        return autor; 
    }
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public LocalTime getDuracion() {
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
}
