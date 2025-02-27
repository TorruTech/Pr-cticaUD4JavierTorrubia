package gui;

import base.Evento;
import util.Util;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDate;
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
            vista.setTitle("App eventos - <CONECTADO>");
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
        vista.btnAddOrganizador.addActionListener(listener);
        vista.btnModOrganizador.addActionListener(listener);
        vista.btnDelOrganizador.addActionListener(listener);

        vista.itemConectar.addActionListener(listener);
        vista.itemSalir.addActionListener(listener);
    }

    private void addListSelectionListeners(ListSelectionListener listener){
        vista.listEventos.addListSelectionListener(listener);
        vista.listActividades.addListSelectionListener(listener);
        vista.listOrganizadores.addListSelectionListener(listener);
    }

    private void addKeyListeners(KeyListener listener){
        vista.txtBuscarEvento.addKeyListener(listener);
        vista.txtBuscarActividades.addKeyListener(listener);
        vista.txtBuscarOrganizador.addKeyListener(listener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "conexion":
                try {
                    if (modelo.getCliente() == null) {
                        modelo.conectar();
                        vista.itemConectar.setText("Desconectar");
                        vista.setTitle("App eventos - <CONECTADO>");
                        setBotonesActivados(true);
                        listarProductos();
                        listarEmpleados();
                        listarDepartamentos();
                    } else {
                        modelo.desconectar();
                        vista.itemConectar.setText("Conectar");
                        vista.setTitle("App eventos - <SIN CONEXION>");
                        setBotonesActivados(false);
                        vista.dlmEventos.clear();
                        vista.dlmActividades.clear();
                        vista.dlmOrganizadores.clear();
                        limpiarCamposEvento();
                        limpiarCamposActividad();
                        limpiarCamposOrganizador();
                    }
                } catch (Exception ex) {
                    Util.mostrarMensajeError("Imposible establecer conexión con el servidor.");
                }
                break;

            case "salir":
                modelo.desconectar();
                System.exit(0);
                break;

            case "addEvento":
                if (comprobarCamposEvento()) {
                    modelo.guardarObjeto(new Evento(vista.nombreEventoTxt.getText(),
                            LocalDate.parse(vista.fechaEventoDPicker.getText()),
                            Float.parseFloat(vista.precioEventoTxt.getText()),
                            vista.organizadorComboBox.getSelectedItem().getId()));
                    limpiarCamposEvento();
                } else {
                    Util.mostrarMensajeError("No ha sido posible insertar el producto en la base de datos.\n" +
                            "Compruebe que los campos contengan el tipo de dato requerido.");
                }
                listarProductos();
                break;

            case "modProducto":
                if (vista.listProductos.getSelectedValue() != null) {
                    if (comprobarCamposEvento()) {
                        Producto producto = vista.listProductos.getSelectedValue();
                        producto.setNombre(vista.txtNombreProducto.getText());
                        producto.setGrados(Integer.parseInt(vista.txtGradosProducto.getText()));
                        producto.setPrecio(Float.parseFloat(vista.txtPrecioProducto.getText()));
                        modelo.modificarObjeto(producto);
                        limpiarCamposEvento();
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
                    limpiarCamposEvento();
                } else {
                    Util.mostrarMensajeError("No hay ningún elemento seleccionado.");
                }
                break;

            case "addEmpleado":
                if (comprobarCamposEmpleado()) {
                    modelo.guardarObjeto(new Empleado(vista.descripcionActividadTxt.getText(),
                            vista.duracionActividadTxt.getText(),
                            vista.dateNacimientoEmpleado.getDate()));
                    limpiarCamposActividad();
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
                        limpiarCamposActividad();
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
                    limpiarCamposActividad();
                } else {
                    Util.mostrarMensajeError("No hay ningún elemento seleccionado.");
                }
                break;

            case "addDepartamento":
                if (comprobarCamposDepartamento()) {
                    modelo.guardarObjeto(new Departamento(vista.txtDepartamento.getText()));
                    limpiarCamposOrganizador();
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
                        limpiarCamposOrganizador();
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
                    limpiarCamposOrganizador();
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
        } else if (e.getSource() == vista.txtBuscarActividades) {
            listarEmpleadosBusqueda(modelo.getEmpleados(vista.txtBuscarActividades.getText()));
            if (vista.txtBuscarActividades.getText().isEmpty()) {
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
        if (e.getSource() == vista.listEventos) {
            if (vista.listEventos.getSelectedValue() != null) {
                Evento evento = vista.listEventos.getSelectedValue();
                vista.nombreEventoTxt.setText(evento.getNombre());
                vista.fechaEventoDPicker.setText(evento.getFecha().toString());
                vista.precioEventoTxt.setText(String.valueOf(evento.getPrecio()));
                vista.organizadorComboBox.setSelectedItem(evento.getOrganizador());
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

    private boolean comprobarCamposEvento() {
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

    private void limpiarCamposEvento() {
        vista.nombreEventoTxt.setText("");
        vista.fechaEventoDPicker.clear();
        vista.precioEventoTxt.setText("");
        vista.organizadorComboBox.setSelectedIndex(-1);
        vista.txtBuscarEvento.setText("");
    }

    private void limpiarCamposActividad() {
        vista.descripcionActividadTxt.setText("");
        vista.duracionActividadTxt.setText("");
        vista.participantesActividadTxt.setText("");
        vista.eventoComboBox.setSelectedIndex(-1);
        vista.txtBuscarActividades.setText("");
    }

    private void limpiarCamposOrganizador() {
        vista.nombreOrganizadorTxt.setText("");
        vista.emailOrganizadorTxt.setText("");
        vista.edadOrganizadorTxt.setText("");
        vista.txtBuscarOrganizador.setText("");
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
        for (Producto producto : modelo.getEventos()) {
            vista.dlmProductos.addElement(producto);
        }
    }

    private void listarEmpleados() {
        vista.dlmEmpleados.clear();
        for (Empleado empleado : modelo.getActividades()) {
            vista.dlmEmpleados.addElement(empleado);
        }
    }

    private void listarDepartamentos() {
        vista.dlmDepartamentos.clear();
        for (Departamento departamento : modelo.getOrganizadores()) {
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
        vista.btnAddOrganizador.setEnabled(activados);
        vista.btnModOrganizador.setEnabled(activados);
        vista.btnDelOrganizador.setEnabled(activados);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}
}
