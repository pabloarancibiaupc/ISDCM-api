package isdcm.api.models;

import isdcm.api.exceptions.VideoModelException;
import isdcm.api.exceptions.VideoModelException.VideoErrorCode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;

public class Video {
    private Integer id;
    private String titulo;
    private Usuario autor;
    private LocalDateTime fechaCreacion;
    private LocalTime duracion;
    private int reproducciones;
    private String descripcion;
    private String formato;
    
    public Video(Integer id, String titulo, Usuario autor, LocalDateTime fechaCreacion, LocalTime duracion, int reproducciones, String descripcion, String formato)
            throws VideoModelException
    {
        this.id = id;
        if (titulo == null || titulo.isBlank()) {
            throw new VideoModelException(VideoErrorCode.VIDEO_TITULO_REQUIRED);
        }
        this.titulo = titulo.trim();
        if (autor == null) {
            throw new VideoModelException(VideoErrorCode.VIDEO_AUTOR_REQUIRED);
        }
        this.autor = autor;
        if (fechaCreacion == null) {
            throw new VideoModelException(VideoErrorCode.VIDEO_FECHA_CREACION_REQUIRED);
        }
        this.fechaCreacion = fechaCreacion;
        if (duracion == null) {
            throw new VideoModelException(VideoErrorCode.VIDEO_DURACION_REQUIRED);
        }
        this.duracion = duracion;
        if (reproducciones < 0) {
            throw new VideoModelException(VideoErrorCode.VIDEO_REPRODUCCIONES_NEGATIVE);
        }
        this.reproducciones = reproducciones;
        if (descripcion == null || descripcion.isBlank()) {
            throw new VideoModelException(VideoErrorCode.VIDEO_DESCRIPCION_REQUIRED);
        }
        this.descripcion = descripcion.trim();
        if (formato == null || formato.isBlank()) {
            throw new VideoModelException(VideoErrorCode.VIDEO_FORMATO_REQUIRED);
        }
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
    
    // Setters
    public void setId(Integer id) {
        this.id = id;
    }
}
