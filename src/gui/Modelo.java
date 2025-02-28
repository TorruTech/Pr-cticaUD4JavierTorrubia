package gui;

import base.Actividad;
import base.Evento;
import base.Organizador;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class Modelo {

    private MongoClient cliente;
    private MongoCollection<Document> eventos;
    private MongoCollection<Document> actividades;
    private MongoCollection<Document> organizadores;

    public void conectar() {
        cliente = new MongoClient();
        String DATABASE = "EventosApp";
        MongoDatabase db = cliente.getDatabase(DATABASE);

        String COLECCION_EVENTOS = "Eventos";
        eventos = db.getCollection(COLECCION_EVENTOS);
        String COLECCION_ACTIVIDADES = "Actividades";
        actividades = db.getCollection(COLECCION_ACTIVIDADES);
        String COLECCION_ORGANIZADORES = "Organizadores";
        organizadores = db.getCollection(COLECCION_ORGANIZADORES);
    }

    public void desconectar() {
        cliente.close();
        cliente = null;
    }

    public MongoClient getCliente() {
        return cliente;
    }

    public ArrayList<Evento> getEventos() {
        ArrayList<Evento> lista = new ArrayList<>();

        for (Document document : eventos.find()) {
            lista.add(documentToEvento(document));
        }
        return lista;
    }

    public ArrayList<Evento> getEventos(String comparador) {
        ArrayList<Evento> lista = new ArrayList<>();
        Document query = new Document();
        List<Document> listaCriterios = new ArrayList<>();

        listaCriterios.add(new Document("nombre", new Document("$regex", "/*" + comparador + "/*")));
        query.append("$or", listaCriterios);

        for (Document document : eventos.find(query)) {
            lista.add(documentToEvento(document));
        }

        return lista;
    }

    public ArrayList<Actividad> getActividades() {
        ArrayList<Actividad> lista = new ArrayList<>();

        for (Document document : actividades.find()) {
            lista.add(documentToActividad(document));
        }
        return lista;
    }

    public ArrayList<Actividad> getActividades(String comparador) {
        ArrayList<Actividad> lista = new ArrayList<>();
        Document query = new Document();
        List<Document> listaCriterios = new ArrayList<>();

        listaCriterios.add(new Document("descripcion", new Document("$regex", "/*" + comparador + "/*")));
        query.append("$or", listaCriterios);

        for (Document document : actividades.find(query)) {
            lista.add(documentToActividad(document));
        }

        return lista;
    }

    public ArrayList<Organizador> getOrganizadores() {
        ArrayList<Organizador> lista = new ArrayList<>();

        for (Document document : organizadores.find()) {
            lista.add(documentToOrganizador(document));
        }
        return lista;
    }

    public ArrayList<Organizador> getDepartamentos(String comparador) {
        ArrayList<Organizador> lista = new ArrayList<>();
        Document query = new Document();
        List<Document> listaCriterios = new ArrayList<>();

        listaCriterios.add(new Document("email", new Document("$regex", "/*" + comparador + "/*")));
        query.append("$or", listaCriterios);

        for (Document document : organizadores.find(query)) {
            lista.add(documentToOrganizador(document));
        }

        return lista;
    }

    public void guardarObjeto(Object obj) {
        if (obj instanceof Evento) {
            eventos.insertOne(objectToDocument(obj));
        } else if (obj instanceof Actividad) {
            actividades.insertOne(objectToDocument(obj));
        } else if (obj instanceof Organizador) {
            organizadores.insertOne(objectToDocument(obj));
        }
    }

    public void modificarObjeto(Object obj) {
        if (obj instanceof Evento) {
            Document filtro = new Document("_id", ((Evento) obj).getId()); // Filtrar por ID
            Document documentoActualizado = objectToDocument(obj);
            eventos.replaceOne(filtro, documentoActualizado);
        } else if (obj instanceof Actividad) {
            Document filtro = new Document("_id", ((Actividad) obj).getId());
            Document documentoActualizado = objectToDocument(obj);
            actividades.replaceOne(filtro, documentoActualizado);
        } else if (obj instanceof Organizador) {
            Document filtro = new Document("_id", ((Organizador) obj).getId());
            Document documentoActualizado = objectToDocument(obj);
            organizadores.replaceOne(filtro, documentoActualizado);
        }
    }

    public void eliminarObjeto(Object obj) {
        if (obj instanceof Evento) {
            Evento evento = (Evento) obj;
            eventos.deleteOne(objectToDocument(evento));
        } else if (obj instanceof Actividad) {
            Actividad actividad = (Actividad) obj;
            actividades.deleteOne(objectToDocument(actividad));
        } else if (obj instanceof Organizador) {
            Organizador organizador = (Organizador) obj;
            organizadores.deleteOne(objectToDocument(organizador));
        }
    }

    public Evento documentToEvento(Document dc) {
        Evento evento = new Evento();

        evento.setId(dc.getObjectId("_id"));
        evento.setNombre(dc.getString("nombre"));
        evento.setPrecio(dc.getDouble("precio"));
        evento.setOrganizadorId(dc.getObjectId("organizadorId"));

        String fechaStr = dc.getString("fecha");
        if (fechaStr != null && !fechaStr.trim().isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                evento.setFecha(LocalDate.parse(fechaStr, formatter));
            } catch (DateTimeParseException e) {
                System.err.println("Error al convertir la fecha: " + fechaStr);
                evento.setFecha(null); // O manejarlo con un valor por defecto
            }
        } else {
            evento.setFecha(null); // Evitar NullPointerException si la fecha es nula
        }

        return evento;
    }

    public Actividad documentToActividad(Document dc) {
        Actividad actividad = new Actividad();

        actividad.setId(dc.getObjectId("_id"));
        actividad.setDescripcion(dc.getString("descripcion"));
        actividad.setDuracion(dc.getDouble("duracion"));
        actividad.setNumParticipantes(dc.getInteger("numParticipantes"));
        actividad.setEventoId(dc.getObjectId("eventoId"));
        return actividad;
    }

    public Organizador documentToOrganizador(Document dc) {
        Organizador organizador = new Organizador();
        organizador.setId(dc.getObjectId("_id"));
        organizador.setNombre(dc.getString("nombre"));
        organizador.setEmail(dc.getString("email"));
        organizador.setEdad(dc.getInteger("edad"));
        return organizador;
    }

    public Document objectToDocument(Object obj) {
        Document dc = new Document();

        if (obj instanceof Evento) {
            Evento evento = (Evento) obj;

            dc.append("nombre", evento.getNombre());
            dc.append("fecha", evento.getFecha().toString());
            dc.append("precio", evento.getPrecio());
            dc.append("organizadorId", evento.getOrganizadorId());

        } else if (obj instanceof Actividad) {
            Actividad actividad = (Actividad) obj;

            dc.append("descripcion", actividad.getDescripcion());
            dc.append("duracion", actividad.getDuracion());
            dc.append("numParticipantes", actividad.getNumParticipantes());
            dc.append("eventoId", actividad.getEventoId());

        } else if (obj instanceof Organizador) {
            Organizador organizador = (Organizador) obj;

            dc.append("nombre", organizador.getNombre());
            dc.append("email", organizador.getEmail());
            dc.append("edad", organizador.getEdad());
        } else {
            return null;
        }
        return dc;
    }

    public Evento[] getEventosPorOrganizador(Organizador organizador) {
        ArrayList<Evento> eventosPorOrganizador = new ArrayList<>();

        for (Document document : eventos.find(new Document("organizadorId", organizador.getId()))) {
            eventosPorOrganizador.add(documentToEvento(document));
        }

        return eventosPorOrganizador.toArray(new Evento[0]);
    }

    public Actividad[] getActividadesPorEvento(Evento evento) {
        ArrayList<Actividad> actividadesPorEvento = new ArrayList<>();

        for (Document document : actividades.find(new Document("eventoId", evento.getId()))) {
            actividadesPorEvento.add(documentToActividad(document));
        }

        return actividadesPorEvento.toArray(new Actividad[0]);
    }
}
