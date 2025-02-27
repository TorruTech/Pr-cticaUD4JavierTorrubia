package gui;

import barcafeteria.base.Departamento;
import barcafeteria.base.Empleado;
import barcafeteria.base.Producto;
import util.Util;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Controlador implements ActionListener, KeyListener, ListSelectionListener {
    private Modelo modelo;
    private Vista vista;

    public Controlador(Modelo modelo, Vista vista) {
        this.vista = vista;
        this.modelo = modelo;

        addActionListeners(this);
        addKeyListeners(this);
        addListSelectionListeners(this);

        try {
            modelo.conectar();
            vista.itemConectar.setText("Desconectar");
            vista.setTitle("Bar Manolo - <CONECTADO>");
            setBotonesActivados(true);
            listarProductos();
            listarEmpleados();
            listarDepartamentos();
        } catch (Exception ex) {
            Util.mostrarMensajeError("Imposible establecer conexión con el servidor.");
        }
    }

    private void addActionListeners(ActionListener listener){
        vista.btnAddEvento.addActionListener(listener);
        vista.btnModEvento.addActionListener(listener);
        vista.btnDelEvento.addActionListener(listener);
        vista.btnAddActividad.addActionListener(listener);
        vista.btnModActividad.addActionListener(listener);
        vista.btnDelActividad.addActionListener(listener);
        vista.btnAddDepartamento.addActionListener(listener);
        vista.btnModDepartamento.addActionListener(listener);
        vista.btnDelDepartamento.addActionListener(listener);

        vista.itemConectar.addActionListener(listener);
        vista.itemSalir.addActionListener(listener);
    }

    private void addListSelectionListeners(ListSelectionListener listener){
        vista.listProductos.addListSelectionListener(listener);
        vista.listEmpleados.addListSelectionListener(listener);
        vista.listDepartamentos.addListSelectionListener(listener);
    }

    private void addKeyListeners(KeyListener listener){
        vista.txtBuscarProducto.addKeyListener(listener);
        vista.txtBuscarEmpleado.addKeyListener(listener);
        vista.txtBuscarDepartamento.addKeyListener(listener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "conexion":
                try {
                    if (modelo.getCliente() == null) {
                        modelo.conectar();
                        vista.itemConectar.setText("Desconectar");
                        vista.setTitle("Bar Manolo - <CONECTADO>");
                        setBotonesActivados(true);
                        listarProductos();
                        listarEmpleados();
                        listarDepartamentos();
                    } else {
                        modelo.desconectar();
                        vista.itemConectar.setText("Conectar");
                        vista.setTitle("Bar Manolo - <SIN CONEXION>");
                        setBotonesActivados(false);
                        vista.dlmProductos.clear();
                        vista.dlmEmpleados.clear();
                        vista.dlmDepartamentos.clear();
                        limpiarCamposProducto();
                        limpiarCamposEmpleado();
                        limpiarCamposDepartamento();
                    }
                } catch (Exception ex) {
                    Util.mostrarMensajeError("Imposible establecer conexión con el servidor.");
                }
                break;

            case "salir":
                modelo.desconectar();
                System.exit(0);
                break;

            case "addProducto":
                if (comprobarCamposProducto()) {
                    modelo.guardarObjeto(new Producto(vista.txtNombreProducto.getText(),
                            Integer.parseInt(vista.txtGradosProducto.getText()),
                            Float.parseFloat(vista.txtPrecioProducto.getText())));
                    limpiarCamposProducto();
                } else {
                    Util.mostrarMensajeError("No ha sido posible insertar el producto en la base de datos.\n" +
                            "Compruebe que los campos contengan el tipo de dato requerido.");
                }
                listarProductos();
                break;

            case "modProducto":
                if (vista.listProductos.getSelectedValue() != null) {
                    if (comprobarCamposProducto()) {
                        Producto producto = vista.listProductos.getSelectedValue();
                        producto.setNombre(vista.txtNombreProducto.getText());
                        producto.setGrados(Integer.parseInt(vista.txtGradosProducto.getText()));
                        producto.setPrecio(Float.parseFloat(vista.txtPrecioProducto.getText()));
                        modelo.modificarObjeto(producto);
                        limpiarCamposProducto();
                    } else {
                        Util.mostrarMensajeError("No ha sido posible modificar el producto en la base de datos.\n" +
                                "Compruebe que los campos contengan el tipo de dato requerido.");
                    }
                    listarProductos();
                } else {
                    Util.mostrarMensajeError("No hay ningún elemento seleccionado.");
                }
                break;

            case "delProducto":
                if (vista.listProductos.getSelectedValue() != null) {
                    modelo.eliminarObjeto(vista.listProductos.getSelectedValue());
                    listarProductos();
                    limpiarCamposProducto();
                } else {
                    Util.mostrarMensajeError("No hay ningún elemento seleccionado.");
                }
                break;

            case "addEmpleado":
                if (comprobarCamposEmpleado()) {
                    modelo.guardarObjeto(new Empleado(vista.descripcionActividadTxt.getText(),
                            vista.duracionActividadTxt.getText(),
                            vista.dateNacimientoEmpleado.getDate()));
                    limpiarCamposEmpleado();
                } else {
                    Util.mostrarMensajeError("No ha sido posible insertar el empleado en la base de datos.\n" +
                            "Compruebe que los campos contengan el tipo de dato requerido.");
                }
                listarEmpleados();
                break;

            case "modEmpleado":
                if (vista.listEmpleados.getSelectedValue() != null) {
                    if (comprobarCamposEmpleado()) {
                        Empleado empleado = vista.listEmpleados.getSelectedValue();
                        empleado.setNombre(vista.descripcionActividadTxt.getText());
                        empleado.setApellidos(vista.duracionActividadTxt.getText());
                        empleado.setNacimiento(vista.dateNacimientoEmpleado.getDate());
                        modelo.modificarObjeto(empleado);
                        limpiarCamposEmpleado();
                    } else {
                        Util.mostrarMensajeError("No ha sido posible modificar el empleado en la base de datos.\n" +
                                "Compruebe que los campos contengan el tipo de dato requerido.");
                    }
                    listarEmpleados();
                } else {
                    Util.mostrarMensajeError("No hay ningún elemento seleccionado.");
                }
                break;

            case "delEmpleado":
                if (vista.listEmpleados.getSelectedValue() != null) {
                    modelo.eliminarObjeto(vista.listEmpleados.getSelectedValue());
                    listarEmpleados();
                    limpiarCamposEmpleado();
                } else {
                    Util.mostrarMensajeError("No hay ningún elemento seleccionado.");
                }
                break;

            case "addDepartamento":
                if (comprobarCamposDepartamento()) {
                    modelo.guardarObjeto(new Departamento(vista.txtDepartamento.getText()));
                    limpiarCamposDepartamento();
                } else {
                    Util.mostrarMensajeError("No ha sido posible insertar el departamento en la base de datos.\n" +
                            "Compruebe que los campos contengan el tipo de dato requerido.");
                }
                listarDepartamentos();
                break;

            case "modDepartamento":
                if (vista.listDepartamentos.getSelectedValue() != null) {
                    if (comprobarCamposDepartamento()) {
                        Departamento departamento = vista.listDepartamentos.getSelectedValue();
                        departamento.setDepartamento(vista.txtDepartamento.getText());
                        modelo.modificarObjeto(departamento);
                        limpiarCamposDepartamento();
                    } else {
                        Util.mostrarMensajeError("No ha sido posible modificar el departamento en la base de datos.\n" +
                                "Compruebe que los campos contengan el tipo de dato requerido.");
                    }
                    listarDepartamentos();
                } else {
                    Util.mostrarMensajeError("No hay ningún elemento seleccionado.");
                }
                break;

            case "delDepartamento":
                if (vista.listDepartamentos.getSelectedValue() != null) {
                    modelo.eliminarObjeto(vista.listDepartamentos.getSelectedValue());
                    listarDepartamentos();
                    limpiarCamposDepartamento();
                    break;
                } else {
                    Util.mostrarMensajeError("No hay ningún elemento seleccionado.");
                }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == vista.txtBuscarProducto) {
            listarProductosBusqueda(modelo.getProductos(vista.txtBuscarProducto.getText()));
            if (vista.txtBuscarProducto.getText().isEmpty()) {
                vista.dlmProductosBusqueda.clear();
            }
        } else if (e.getSource() == vista.txtBuscarEmpleado) {
            listarEmpleadosBusqueda(modelo.getEmpleados(vista.txtBuscarEmpleado.getText()));
            if (vista.txtBuscarEmpleado.getText().isEmpty()) {
                vista.dlmEmpleadosBusqueda.clear();
            }
        } else if (e.getSource() == vista.txtBuscarDepartamento) {
            listarDepartamentosBusqueda(modelo.getDepartamentos(vista.txtBuscarDepartamento.getText()));
            if (vista.txtBuscarDepartamento.getText().isEmpty()) {
                vista.dlmDepartamentosBusqueda.clear();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == vista.listProductos) {
            if (vista.listProductos.getSelectedValue() != null) {
                Producto producto = vista.listProductos.getSelectedValue();
                vista.txtNombreProducto.setText(producto.getNombre());
                vista.txtGradosProducto.setText(String.valueOf(producto.getGrados()));
                vista.txtPrecioProducto.setText(String.valueOf(producto.getPrecio()));
            }
        } else if (e.getSource() == vista.listEmpleados) {
            if (vista.listEmpleados.getSelectedValue() != null) {
                Empleado empleado = vista.listEmpleados.getSelectedValue();
                vista.descripcionActividadTxt.setText(empleado.getNombre());
                vista.duracionActividadTxt.setText(empleado.getApellidos());
                vista.dateNacimientoEmpleado.setDate(empleado.getNacimiento());
            }
        } else if (e.getSource() == vista.listDepartamentos) {
            if (vista.listDepartamentos.getSelectedValue() != null) {
                Departamento departamento = vista.listDepartamentos.getSelectedValue();
                vista.txtDepartamento.setText(departamento.getDepartamento());
            }
        }
    }

    private boolean comprobarCamposProducto() {
        return !vista.txtNombreProducto.getText().isEmpty() &&
                !vista.txtGradosProducto.getText().isEmpty() &&
                !vista.txtPrecioProducto.getText().isEmpty() &&
                comprobarInt(vista.txtGradosProducto.getText()) &&
                comprobarFloat(vista.txtPrecioProducto.getText());
    }

    private boolean comprobarCamposEmpleado() {
        return !vista.descripcionActividadTxt.getText().isEmpty() &&
                !vista.duracionActividadTxt.getText().isEmpty() &&
                !vista.dateNacimientoEmpleado.getText().isEmpty();
    }

    private boolean comprobarCamposDepartamento() {
        return !vista.txtDepartamento.getText().isEmpty();
    }

    private void limpiarCamposProducto() {
        vista.txtNombreProducto.setText("");
        vista.txtGradosProducto.setText("");
        vista.txtPrecioProducto.setText("");
        vista.txtBuscarProducto.setText("");
    }

    private void limpiarCamposEmpleado() {
        vista.descripcionActividadTxt.setText("");
        vista.duracionActividadTxt.setText("");
        vista.dateNacimientoEmpleado.clear();
        vista.txtBuscarEmpleado.setText("");
    }

    private void limpiarCamposDepartamento() {
        vista.txtDepartamento.setText("");
        vista.txtBuscarDepartamento.setText("");
    }

    private boolean comprobarInt(String txt) {
        try {
            Integer.parseInt(txt);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean comprobarFloat(String txt) {
        try {
            Float.parseFloat(txt);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private void listarProductos() {
        vista.dlmProductos.clear();
        for (Producto producto : modelo.getProductos()) {
            vista.dlmProductos.addElement(producto);
        }
    }

    private void listarEmpleados() {
        vista.dlmEmpleados.clear();
        for (Empleado empleado : modelo.getEmpleados()) {
            vista.dlmEmpleados.addElement(empleado);
        }
    }

    private void listarDepartamentos() {
        vista.dlmDepartamentos.clear();
        for (Departamento departamento : modelo.getDepartamentos()) {
            vista.dlmDepartamentos.addElement(departamento);
        }
    }

    private void listarProductosBusqueda(ArrayList<Producto> lista) {
        vista.dlmProductosBusqueda.clear();
        for (Producto producto : lista) {
            vista.dlmProductosBusqueda.addElement(producto);
        }
    }

    private void listarEmpleadosBusqueda(ArrayList<Empleado> lista) {
        vista.dlmEmpleadosBusqueda.clear();
        for (Empleado empleado : lista) {
            vista.dlmEmpleadosBusqueda.addElement(empleado);
        }
    }

    private void listarDepartamentosBusqueda(ArrayList<Departamento> lista) {
        vista.dlmDepartamentosBusqueda.clear();
        for (Departamento departamento : lista) {
            vista.dlmDepartamentosBusqueda.addElement(departamento);
        }
    }

    private void setBotonesActivados(boolean activados) {
        vista.btnAddEvento.setEnabled(activados);
        vista.btnModEvento.setEnabled(activados);
        vista.btnDelEvento.setEnabled(activados);
        vista.btnAddActividad.setEnabled(activados);
        vista.btnModActividad.setEnabled(activados);
        vista.btnDelActividad.setEnabled(activados);
        vista.btnAddDepartamento.setEnabled(activados);
        vista.btnModDepartamento.setEnabled(activados);
        vista.btnDelDepartamento.setEnabled(activados);
    }

    // Métodos innecesarios
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}
}
