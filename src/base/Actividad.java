package base;

import org.bson.types.ObjectId;
import java.util.Date;

public class Actividad {
    private ObjectId id;
    private String descripcion;
    private double duracion; // En horas
    private int numParticipantes;
    private ObjectId eventoId;

    // Constructor
    public Actividad(String descripcion, double duracion, int numParticipantes, ObjectId eventoId) {
        this.id = new ObjectId();
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.numParticipantes = numParticipantes;
        this.eventoId = eventoId;
    }

    // Getters y Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    public int getNumParticipantes() {
        return numParticipantes;
    }

    public void setNumParticipantes(int numParticipantes) {
        this.numParticipantes = numParticipantes;
    }

    public ObjectId getEventoId() {
        return eventoId;
    }

    public void setEventoId(ObjectId eventoId) {
        this.eventoId = eventoId;
    }

    @Override
    public String toString() {
        return "Actividad{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", duracion=" + duracion +
                ", numParticipantes=" + numParticipantes +
                ", eventoId=" + eventoId +
                '}';
    }
}
