package gui;

import base.Actividad;
import base.Evento;
import base.Organizador;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
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

        for (Document document : eventos.find(query)) {
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
            eventos.replaceOne(objectToDocument(obj), objectToDocument(obj));
        } else if (obj instanceof Actividad) {
            actividades.replaceOne(objectToDocument(obj), objectToDocument(obj));
        } else if (obj instanceof Organizador) {
            organizadores.replaceOne(objectToDocument(obj), objectToDocument(obj));
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
        evento.setFecha(LocalDate.parse(dc.getString("fecha")));
        evento.setPrecio(dc.getDouble("precio"));
        evento.setOrganizadorId(dc.getObjectId("organizadorId"));
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
}
