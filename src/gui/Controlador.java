package gui;

import base.Actividad;
import base.Evento;
import base.Organizador;
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
            listarEventos();
            listarActividades();
            listarOrganizadores();
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
                        listarEventos();
                        listarActividades();
                        listarOrganizadores();
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
                listarEventos();
                break;

            case "modEvento":
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
                    listarEventos();
                } else {
                    Util.mostrarMensajeError("No hay ningún elemento seleccionado.");
                }
                break;

            case "delEvento":
                if (vista.listEventos.getSelectedValue() != null) {
                    modelo.eliminarObjeto(vista.listEventos.getSelectedValue());
                    listarEventos();
                    limpiarCamposEvento();
                } else {
                    Util.mostrarMensajeError("No hay ningún elemento seleccionado.");
                }
                break;

            case "addActividad":
                if (comprobarCamposActividad()) {
                    modelo.guardarObjeto(new Empleado(vista.descripcionActividadTxt.getText(),
                            vista.duracionActividadTxt.getText(),
                            vista.dateNacimientoEmpleado.getDate()));
                    limpiarCamposActividad();
                } else {
                    Util.mostrarMensajeError("No ha sido posible insertar el empleado en la base de datos.\n" +
                            "Compruebe que los campos contengan el tipo de dato requerido.");
                }
                listarActividades();
                break;

            case "modActividad":
                if (vista.listEmpleados.getSelectedValue() != null) {
                    if (comprobarCamposActividad()) {
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
                    listarActividades();
                } else {
                    Util.mostrarMensajeError("No hay ningún elemento seleccionado.");
                }
                break;

            case "delActividad":
                if (vista.listActividades.getSelectedValue() != null) {
                    modelo.eliminarObjeto(vista.listActividades.getSelectedValue());
                    listarActividades();
                    limpiarCamposActividad();
                } else {
                    Util.mostrarMensajeError("No hay ningún elemento seleccionado.");
                }
                break;

            case "addOrganizador":
                if (comprobarCamposOrganizador()) {
                    modelo.guardarObjeto(new Departamento(vista.txtDepartamento.getText()));
                    limpiarCamposOrganizador();
                } else {
                    Util.mostrarMensajeError("No ha sido posible insertar el departamento en la base de datos.\n" +
                            "Compruebe que los campos contengan el tipo de dato requerido.");
                }
                listarOrganizadores();
                break;

            case "modOrganizador":
                if (vista.listDepartamentos.getSelectedValue() != null) {
                    if (comprobarCamposOrganizador()) {
                        Departamento departamento = vista.listDepartamentos.getSelectedValue();
                        departamento.setDepartamento(vista.txtDepartamento.getText());
                        modelo.modificarObjeto(departamento);
                        limpiarCamposOrganizador();
                    } else {
                        Util.mostrarMensajeError("No ha sido posible modificar el departamento en la base de datos.\n" +
                                "Compruebe que los campos contengan el tipo de dato requerido.");
                    }
                    listarOrganizadores();
                } else {
                    Util.mostrarMensajeError("No hay ningún elemento seleccionado.");
                }
                break;

            case "delOrganizador":
                if (vista.listOrganizadores.getSelectedValue() != null) {
                    modelo.eliminarObjeto(vista.listOrganizadores.getSelectedValue());
                    listarOrganizadores();
                    limpiarCamposOrganizador();
                    break;
                } else {
                    Util.mostrarMensajeError("No hay ningún elemento seleccionado.");
                }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == vista.txtBuscarEvento) {
            listarEventoBusqueda(modelo.getEventos(vista.txtBuscarEvento.getText()));
            if (vista.txtBuscarEvento.getText().isEmpty()) {
                vista.dlmEventosBusqueda.clear();
            }
        } else if (e.getSource() == vista.txtBuscarActividades) {
            listarActividadBusqueda(modelo.getActividades(vista.txtBuscarActividades.getText()));
            if (vista.txtBuscarActividades.getText().isEmpty()) {
                vista.dlmActividadBusqueda.clear();
            }
        } else if (e.getSource() == vista.txtBuscarOrganizador) {
            listarOrganizadorBusqueda(modelo.getDepartamentos(vista.txtBuscarOrganizador.getText()));
            if (vista.txtBuscarOrganizador.getText().isEmpty()) {
                vista.dlmOrganizadorBusqueda.clear();
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
                vista.organizadorComboBox.setSelectedItem(evento.getOrganizadorId().toString());
            }
        } else if (e.getSource() == vista.listActividades) {
            if (vista.listActividades.getSelectedValue() != null) {
                Actividad actividad = vista.listActividades.getSelectedValue();
                vista.descripcionActividadTxt.setText(actividad.getDescripcion());
                vista.duracionActividadTxt.setText(String.valueOf(actividad.getDuracion()));
                vista.participantesActividadTxt.setText(String.valueOf(actividad.getNumParticipantes()));
                vista.eventoComboBox.setSelectedItem(actividad.getEventoId().toString());
            }
        } else if (e.getSource() == vista.listOrganizadores) {
            if (vista.listOrganizadores.getSelectedValue() != null) {
                Organizador organizador = vista.listOrganizadores.getSelectedValue();
                vista.nombreOrganizadorTxt.setText(organizador.getNombre());
                vista.emailOrganizadorTxt.setText(organizador.getEmail());
                vista.edadOrganizadorTxt.setText(String.valueOf(organizador.getEdad()));
            }
        }
    }

    private boolean comprobarCamposEvento() {
        return !vista.nombreEventoTxt.getText().isEmpty() &&
                !vista.fechaEventoDPicker.getText().isEmpty() &&
                !vista.precioEventoTxt.getText().isEmpty() &&
                comprobarInt(vista.precioEventoTxt.getText()) &&
                !vista.organizadorComboBox.getSelectedItem().toString().isEmpty();
    }

    private boolean comprobarCamposActividad() {
        return !vista.descripcionActividadTxt.getText().isEmpty() &&
                !vista.duracionActividadTxt.getText().isEmpty() &&
                !vista.participantesActividadTxt.getText().isEmpty() &&
                comprobarInt(vista.duracionActividadTxt.getText()) &&
                comprobarInt(vista.participantesActividadTxt.getText()) &&
                !vista.eventoComboBox.getSelectedItem().toString().isEmpty();

    }

    private boolean comprobarCamposOrganizador() {
        return !vista.nombreOrganizadorTxt.getText().isEmpty() &&
                !vista.emailOrganizadorTxt.getText().isEmpty() &&
                !vista.edadOrganizadorTxt.getText().isEmpty();
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

    private void listarEventos() {
        vista.dlmEventos.clear();
        for (Evento evento : modelo.getEventos()) {
            vista.dlmEventos.addElement(evento);
        }
    }

    private void listarActividades() {
        vista.dlmActividades.clear();
        for (Actividad actividad : modelo.getActividades()) {
            vista.dlmActividades.addElement(actividad);
        }
    }

    private void listarOrganizadores() {
        vista.dlmOrganizadores.clear();
        for (Organizador organizador : modelo.getOrganizadores()) {
            vista.dlmOrganizadores.addElement(organizador);
        }
    }

    private void listarEventoBusqueda(ArrayList<Evento> lista) {
        vista.dlmEventosBusqueda.clear();
        for (Evento evento : lista) {
            vista.dlmEventosBusqueda.addElement(evento);
        }
    }

    private void listarActividadBusqueda(ArrayList<Actividad> lista) {
        vista.dlmActividadBusqueda.clear();
        for (Actividad actividad : lista) {
            vista.dlmActividadBusqueda.addElement(actividad);
        }
    }

    private void listarOrganizadorBusqueda(ArrayList<Organizador> lista) {
        vista.dlmOrganizadorBusqueda.clear();
        for (Organizador organizador : lista) {
            vista.dlmOrganizadorBusqueda.addElement(organizador);
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
