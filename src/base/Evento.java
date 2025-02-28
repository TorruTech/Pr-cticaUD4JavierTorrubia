package base;

import org.bson.types.ObjectId;
import java.sql.Date;
import java.time.LocalDate;

public class Evento {
    private ObjectId id;
    private String nombre;
    private LocalDate fecha;
    private double precio;
    private ObjectId organizadorId;

    // Constructor
    // Constructor
    public Evento() {
    }

    public Evento(String nombre, LocalDate fecha, double precio, ObjectId organizadorId) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.precio = precio;
        this.organizadorId = organizadorId;
    }

    // Getters y setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public ObjectId getOrganizadorId() {
        return organizadorId;
    }

    public void setOrganizadorId(ObjectId organizadorId) {
        this.organizadorId = organizadorId;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fecha=" + fecha +
                ", precio=" + precio +
                ", organizadorId=" + organizadorId +
                '}';
    }
}
