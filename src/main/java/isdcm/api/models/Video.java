package isdcm.api.models;

import isdcm.api.exceptions.VideoModelException;
import isdcm.api.exceptions.VideoModelException.VideoErrorCode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;

public class Video {
    private Integer id;
    private String titulo;
    private Usuario autor;
    private LocalDateTime fechaCreacion;
    private LocalTime duracion;
    private Integer reproducciones;
    private String descripcion;
    private String formato;
    
    private Video(String titulo, Usuario autor, String descripcion, String formato) throws VideoModelException {
        this.id = null;
        if (titulo == null || titulo.isBlank()) {
            throw new VideoModelException(VideoErrorCode.VIDEO_TITULO_REQUIRED);
        }
        this.titulo = titulo.trim();
        setAutor(autor);
        this.fechaCreacion = LocalDateTime.now();
        this.duracion = null;
        this.reproducciones = 0;
        if (descripcion == null || descripcion.isBlank()) {
            throw new VideoModelException(VideoErrorCode.VIDEO_DESCRIPCION_REQUIRED);
        }
        this.descripcion = descripcion.trim();
        if (formato == null || formato.isBlank()) {
            throw new VideoModelException(VideoErrorCode.VIDEO_FORMATO_REQUIRED);
        }
        this.formato = formato.trim();
    }
    
    public Video(String titulo, Usuario autor, String duracion, String descripcion, String formato) throws VideoModelException {
        this(titulo, autor, descripcion, formato);
        if (duracion == null) {
            throw new VideoModelException(VideoErrorCode.VIDEO_DURACION_REQUIRED);
        }
        try {
            this.duracion = LocalTime.parse(duracion);
        } catch (DateTimeParseException e) {
            throw new VideoModelException(VideoErrorCode.VIDEO_DURACION_INVALID);
        }
    }
    
    public Video(String titulo, Usuario autor, LocalTime duracion, String descripcion, String formato) throws VideoModelException {
        this(titulo, autor, descripcion, formato);
        if (duracion == null) {
            throw new VideoModelException(VideoErrorCode.VIDEO_DURACION_REQUIRED);
        }
        this.duracion = duracion;
    }
    
    public Video(String titulo, Usuario autor, String fechaCreacion, String duracion, Integer reproducciones, String descripcion, String formato)
            throws VideoModelException
    {
        this(titulo, autor, duracion, descripcion, formato);
        if (fechaCreacion == null) {
            throw new VideoModelException(VideoErrorCode.VIDEO_FECHA_CREACION_REQUIRED);
        }
        try {
            this.fechaCreacion = LocalDateTime.parse(fechaCreacion);
        } catch (DateTimeParseException e) {
            throw new VideoModelException(VideoErrorCode.VIDEO_FECHA_CREACION_INVALID);
        }        
        if (reproducciones < 0) {
            throw new VideoModelException(VideoErrorCode.VIDEO_REPRODUCCIONES_INVALID);
        }
        this.reproducciones = reproducciones;
    }
    
    public Video(String titulo, Usuario autor, LocalDateTime fechaCreacion, LocalTime duracion, Integer reproducciones, String descripcion, String formato)
            throws VideoModelException
    {
        this(titulo, autor, duracion, descripcion, formato);
        if (fechaCreacion == null) {
            throw new VideoModelException(VideoErrorCode.VIDEO_FECHA_CREACION_REQUIRED);
        }
        this.fechaCreacion = fechaCreacion;
        if (reproducciones < 0) {
            throw new VideoModelException(VideoErrorCode.VIDEO_REPRODUCCIONES_INVALID);
        }
        this.reproducciones = reproducciones;
    }
    
    public Video(Integer id, String titulo, Usuario autor, String fechaCreacion, String duracion, Integer reproducciones, String descripcion, String formato)
            throws VideoModelException
    {
        this(titulo, autor, fechaCreacion, duracion, reproducciones, descripcion, formato);
        setId(id);
    }
    
    public Video(Integer id, String titulo, Usuario autor, LocalDateTime fechaCreacion, LocalTime duracion, Integer reproducciones, String descripcion, String formato)
            throws VideoModelException
    {
        this(titulo, autor, fechaCreacion, duracion, reproducciones, descripcion, formato);
        setId(id);
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
    public Integer getReproducciones() {
        return reproducciones;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getFormato() {
        return formato;
    }
    
    // Setters
    public final void setId(Integer id) throws VideoModelException {
        if (id == null) {
            throw new VideoModelException(VideoErrorCode.VIDEO_ID_REQUIRED);
        }
        if (id < 0) {
            throw new VideoModelException(VideoErrorCode.VIDEO_ID_INVALID);
        }
        this.id = id;
    }
    public final void setAutor(Usuario autor) throws VideoModelException {
        if (autor == null) {
            throw new VideoModelException(VideoErrorCode.VIDEO_AUTOR_REQUIRED);
        }
        this.autor = autor;
    }
}
