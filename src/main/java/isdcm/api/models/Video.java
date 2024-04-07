package isdcm.api.models;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;

public class Video {
    Integer id;
    String titulo;
    Usuario autor;
    LocalDateTime fechaCreacion;
    LocalTime duracion;
    int reproducciones;
    String descripcion;
    String formato;
    
    public Video(Integer id, String titulo, Usuario autor, LocalDateTime fechaCreacion, LocalTime duracion, int reproducciones, String descripcion, String formato) {
        this.id = id;
        this.titulo = titulo.trim();
        this.autor = autor;
        this.fechaCreacion = fechaCreacion;
        this.duracion = duracion;
        this.reproducciones = reproducciones;
        this.descripcion = descripcion.trim();
        this.formato = formato.trim();
    }
    
    public static ArrayList<Video> SortByQuery(ArrayList<Video> videos, String query) {
        Collections.sort(videos, (v1, v2) -> {
            double sim1 = CalculateSimilarityByQuery(v1, query);
            double sim2 = CalculateSimilarityByQuery(v2, query);
            return Double.compare(sim1, sim2) * -1;
        });
        return videos;
    }
    
    public static ArrayList<Video> SortByAdvancedSearch(ArrayList<Video> videos, String titulo, String autor) {
        Collections.sort(videos, (v1, v2) -> {
            double sim1 = CalculateSimilarityByAdvancedSearch(v1, titulo, autor);
            double sim2 = CalculateSimilarityByAdvancedSearch(v2, titulo, autor);
            return Double.compare(sim1, sim2) * -1;
        });
        return videos;
    }
    
    private static double CalculateSimilarityByQuery(Video video, String query) {
        JaroWinklerSimilarity sim = new JaroWinklerSimilarity();
        String titulo = video.getTitulo().toLowerCase();
        Usuario autor = video.getAutor();
        String username = autor.getUsername().toLowerCase();
        Double tituloSim = sim.apply(titulo, query);
        Double usernameSim = sim.apply(username, query);
        return Double.max(tituloSim, usernameSim);
    }
    
    private static double CalculateSimilarityByAdvancedSearch(Video video, String titulo, String autor) {
        JaroWinklerSimilarity sim = new JaroWinklerSimilarity();
        String text = "";
        if (!titulo.isBlank()) {
            text += video.getTitulo().toLowerCase();
        }
        if (!autor.isBlank()) {
            text += video.getAutor().getUsername().toLowerCase();
        }
        return sim.apply(text, titulo+autor);
    }
    
    // Getters
    public Integer getId() {
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
