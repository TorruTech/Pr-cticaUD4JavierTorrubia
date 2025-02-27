package gui;

import barcafeteria.base.Departamento;
import barcafeteria.base.Empleado;
import barcafeteria.base.Producto;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Modelo {

    private MongoClient cliente;
    private MongoCollection<Document> productos;
    private MongoCollection<Document> empleados;
    private MongoCollection<Document> departamentos;

    public void conectar() {
        cliente = new MongoClient();
        String DATABASE = "BarManolo";
        MongoDatabase db = cliente.getDatabase(DATABASE);

        String COLECCION_PRODUCTOS = "Productos";
        productos = db.getCollection(COLECCION_PRODUCTOS);
        String COLECCION_EMPLEADOS = "Empleados";
        empleados = db.getCollection(COLECCION_EMPLEADOS);
        String COLECCION_DEPARTAMENTOS = "Departamentos";
        departamentos = db.getCollection(COLECCION_DEPARTAMENTOS);
    }

    public void desconectar() {
        cliente.close();
        cliente = null;
    }

    public MongoClient getCliente() {
        return cliente;
    }

    public ArrayList<Producto> getProductos() {
        ArrayList<Producto> lista = new ArrayList<>();

        for (Document document : productos.find()) {
            lista.add(documentToProducto(document));
        }
        return lista;
    }

    public ArrayList<Producto> getProductos(String comparador) {
        ArrayList<Producto> lista = new ArrayList<>();
        Document query = new Document();
        List<Document> listaCriterios = new ArrayList<>();

        listaCriterios.add(new Document("nombre", new Document("$regex", "/*" + comparador + "/*")));
        query.append("$or", listaCriterios);

        for (Document document : productos.find(query)) {
            lista.add(documentToProducto(document));
        }

        return lista;
    }

    public ArrayList<Empleado> getEmpleados() {
        ArrayList<Empleado> lista = new ArrayList<>();

        for (Document document : empleados.find()) {
            lista.add(documentToEmpleado(document));
        }
        return lista;
    }

    public ArrayList<Empleado> getEmpleados(String comparador) {
        ArrayList<Empleado> lista = new ArrayList<>();
        Document query = new Document();
        List<Document> listaCriterios = new ArrayList<>();

        listaCriterios.add(new Document("nombre", new Document("$regex", "/*" + comparador + "/*")));
        listaCriterios.add(new Document("apellidos", new Document("$regex", "/*" + comparador + "/*")));
        query.append("$or", listaCriterios);

        for (Document document : empleados.find(query)) {
            lista.add(documentToEmpleado(document));
        }

        return lista;
    }

    public ArrayList<Departamento> getDepartamentos() {
        ArrayList<Departamento> lista = new ArrayList<>();

        for (Document document : departamentos.find()) {
            lista.add(documentToDepartamento(document));
        }
        return lista;
    }

    public ArrayList<Departamento> getDepartamentos(String comparador) {
        ArrayList<Departamento> lista = new ArrayList<>();
        Document query = new Document();
        List<Document> listaCriterios = new ArrayList<>();

        listaCriterios.add(new Document("departamento", new Document("$regex", "/*" + comparador + "/*")));
        query.append("$or", listaCriterios);

        for (Document document : departamentos.find(query)) {
            lista.add(documentToDepartamento(document));
        }

        return lista;
    }

    public void guardarObjeto(Object obj) {
        if (obj instanceof Producto) {
            productos.insertOne(objectToDocument(obj));
        } else if (obj instanceof Empleado) {
            empleados.insertOne(objectToDocument(obj));
        } else if (obj instanceof Departamento) {
            departamentos.insertOne(objectToDocument(obj));
        }
    }

    public void modificarObjeto(Object obj) {
        if (obj instanceof Producto) {
            Producto producto = (Producto) obj;
            productos.replaceOne(new Document("_id", producto.getId()), objectToDocument(producto));
        } else if (obj instanceof Empleado) {
            Empleado empleado = (Empleado) obj;
            empleados.replaceOne(new Document("_id", empleado.getId()), objectToDocument(empleado));
        } else if (obj instanceof Departamento) {
            Departamento departamento = (Departamento) obj;
            departamentos.replaceOne(new Document("_id", departamento.getId()), objectToDocument(departamento));
        }
    }

    public void eliminarObjeto(Object obj) {
        if (obj instanceof Producto) {
            Producto producto = (Producto) obj;
            productos.deleteOne(objectToDocument(producto));
        } else if (obj instanceof Empleado) {
            Empleado empleado = (Empleado) obj;
            empleados.deleteOne(objectToDocument(empleado));
        } else if (obj instanceof Departamento) {
            Departamento departamento = (Departamento) obj;
            departamentos.deleteOne(objectToDocument(departamento));
        }
    }

    public Producto documentToProducto(Document dc) {
        Producto producto = new Producto();

        producto.setId(dc.getObjectId("_id"));
        producto.setNombre(dc.getString("nombre"));
        producto.setGrados(dc.getInteger("grados"));
        producto.setPrecio((Float.parseFloat(String.valueOf(dc.getDouble("precio")))));
        return producto;
    }

    public Empleado documentToEmpleado(Document dc) {
        Empleado empleado = new Empleado();

        empleado.setId(dc.getObjectId("_id"));
        empleado.setNombre(dc.getString("nombre"));
        empleado.setApellidos(dc.getString("apellidos"));
        empleado.setNacimiento(LocalDate.parse(dc.getString("nacimiento")));
        return empleado;
    }

    public Departamento documentToDepartamento(Document dc) {
        Departamento departamento = new Departamento();

        departamento.setId(dc.getObjectId("_id"));
        departamento.setDepartamento(dc.getString("departamento"));
        return departamento;
    }

    public Document objectToDocument(Object obj) {
        Document dc = new Document();

        if (obj instanceof Producto) {
            Producto producto = (Producto) obj;

            dc.append("nombre", producto.getNombre());
            dc.append("grados", producto.getGrados());
            dc.append("precio", producto.getPrecio());
        } else if (obj instanceof Empleado) {
            Empleado empleado = (Empleado) obj;

            dc.append("nombre", empleado.getNombre());
            dc.append("apellidos", empleado.getApellidos());
            dc.append("nacimiento", empleado.getNacimiento().toString());

        } else if (obj instanceof Departamento) {
            Departamento departamento = (Departamento) obj;

            dc.append("departamento", departamento.getDepartamento());
        } else {
            return null;
        }
        return dc;
    }
}
