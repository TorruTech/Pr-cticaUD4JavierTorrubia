package gui;

import base.Actividad;
import base.Evento;
import base.Organizador;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import util.Util;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Controlador extends WindowAdapter implements ActionListener, KeyListener, ListSelectionListener {
    private Modelo modelo;
    private Vista vista;
    private boolean modoOscuro = false;

    public Controlador(Modelo modelo, Vista vista) {
        this.vista = vista;
        this.modelo = modelo;

        addActionListeners(this);
        addKeyListeners(this);
        addListSelectionListeners(this);
        vista.addWindowListener(this);

        try {
            modelo.conectar();
            vista.itemConectar.setText("Desconectar");
            vista.setTitle("App eventos - <CONECTADO>");
            setBotonesActivados(true);

            listarEventos();
            listarActividades();
            listarOrganizadores();
            refrescarComboOrganizador();
            refrescarComboEvento();

        } catch (Exception ex) {
            Util.mostrarMensajeError("Imposible establecer conexión con el servidor.");
        }
    }

    private void addActionListeners(ActionListener listener){
        vista.btnAddEvento.addActionListener(listener);
        vista.btnAddEvento.setActionCommand("addEvento");
        vista.btnModEvento.addActionListener(listener);
        vista.btnModEvento.setActionCommand("modEvento");
        vista.btnDelEvento.addActionListener(listener);
        vista.btnDelEvento.setActionCommand("delEvento");
        vista.btnAddActividad.addActionListener(listener);
        vista.btnAddActividad.setActionCommand("addActividad");
        vista.btnModActividad.addActionListener(listener);
        vista.btnModActividad.setActionCommand("modActividad");
        vista.btnDelActividad.addActionListener(listener);
        vista.btnDelActividad.setActionCommand("delActividad");
        vista.btnAddOrganizador.addActionListener(listener);
        vista.btnAddOrganizador.setActionCommand("addOrganizador");
        vista.btnModOrganizador.addActionListener(listener);
        vista.btnModOrganizador.setActionCommand("modOrganizador");
        vista.btnDelOrganizador.addActionListener(listener);
        vista.btnDelOrganizador.setActionCommand("delOrganizador");

        vista.itemConectar.addActionListener(listener);
        vista.modoOscuroItem.addActionListener(listener);
        vista.itemSalir.addActionListener(listener);
    }

    private void addListSelectionListeners(ListSelectionListener listener){
        vista.listEventos.addListSelectionListener(listener);
        vista.listActividades.addListSelectionListener(listener);
        vista.listOrganizadores.addListSelectionListener(listener);
        vista.listActividadesPorEvento.addListSelectionListener(listener);
        vista.listEventosPorOrganizador.addListSelectionListener(listener);
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
                        refrescarComboEvento();
                        refrescarComboOrganizador();
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
                    Organizador organizador = (Organizador) vista.organizadorComboBox.getSelectedItem();
                    System.out.println(organizador);

                    LocalDate fechaEvento = vista.fechaEventoDPicker.getDate();
                    System.out.println(fechaEvento);
                    // String fechaFormateada = Util.formatearFecha(fechaStr);

                    if (fechaEvento != null) {
                        try {
                            modelo.guardarObjeto(new Evento(vista.nombreEventoTxt.getText(),
                                    fechaEvento,
                                    Float.parseFloat(vista.precioEventoTxt.getText()),
                                    organizador.getId()));

                            limpiarCamposEvento();
                        } catch (Exception er) {
                            Util.mostrarMensajeError("Error al procesar la fecha: " + er.getMessage());
                        }
                    } else {
                        Util.mostrarMensajeError("Formato de fecha incorrecto. Use DD/MM/YYYY.");
                    }
                } else {
                    Util.mostrarMensajeError("No ha sido posible guardar el evento en la base de datos.\n" +
                            "Compruebe que los campos contengan el tipo de dato requerido.");
                }
                listarEventos();
                break;
            case "modEvento":
                if (vista.listEventos.getSelectedValue() != null) {
                    if (comprobarCamposEvento()) {
                        Organizador organizador = (Organizador) vista.organizadorComboBox.getSelectedItem();
                        Evento evento = vista.listEventos.getSelectedValue();
                        evento.setNombre(vista.nombreEventoTxt.getText());
                        evento.setFecha(vista.fechaEventoDPicker.getDate());
                        evento.setPrecio(Float.parseFloat(vista.precioEventoTxt.getText()));
                        evento.setOrganizadorId(organizador.getId());
                        modelo.modificarObjeto(evento);
                        limpiarCamposEvento();
                    } else {
                        Util.mostrarMensajeError("No ha sido posible modificar el evento en la base de datos.\n" +
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
                    Evento evento = (Evento) vista.eventoComboBox.getSelectedItem();
                    modelo.guardarObjeto(new Actividad(vista.descripcionActividadTxt.getText(),
                            Double.parseDouble(vista.duracionActividadTxt.getText()),
                            Integer.parseInt((vista.participantesActividadTxt.getText())),
                            evento.getId()));
                    limpiarCamposActividad();
                } else {
                    Util.mostrarMensajeError("No ha sido posible insertar la actividad en la base de datos.\n" +
                            "Compruebe que los campos contengan el tipo de dato requerido.");
                }
                listarActividades();
                break;
            case "modActividad":
                if (vista.listActividades.getSelectedValue() != null) {
                    if (comprobarCamposActividad()) {
                        Actividad actividad = vista.listActividades.getSelectedValue();
                        System.out.println(actividad);
                        actividad.setDescripcion(vista.descripcionActividadTxt.getText());
                        actividad.setDuracion(Double.parseDouble(vista.duracionActividadTxt.getText()));
                        actividad.setNumParticipantes(Integer.parseInt(vista.participantesActividadTxt.getText()));
                        Evento evento = (Evento) vista.eventoComboBox.getSelectedItem();
                        actividad.setEventoId(evento.getId());
                        System.out.println(actividad);
                        modelo.modificarObjeto(actividad);
                        limpiarCamposActividad();
                    } else {
                        Util.mostrarMensajeError("No ha sido posible modificar la actividad en la base de datos.\n" +
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
                try {
                    if (comprobarCamposOrganizador()) {
                        modelo.guardarObjeto(new Organizador(vista.nombreOrganizadorTxt.getText(),
                                vista.emailOrganizadorTxt.getText(),
                                Integer.parseInt(vista.edadOrganizadorTxt.getText())));
                        limpiarCamposOrganizador();
                    } else {
                        Util.mostrarMensajeError("No ha sido posible insertar el organizador en la base de datos.\n" +
                                "Compruebe que los campos contengan el tipo de dato requerido.");
                    }
                    listarOrganizadores();
                } catch (NumberFormatException err) {
                    Util.mostrarMensajeError("La edad tiene que ser un número");
                }
                break;
            case "modOrganizador":
                try {
                    if (vista.listOrganizadores.getSelectedValue() != null) {
                        if (comprobarCamposOrganizador()) {
                            Organizador organizador = vista.listOrganizadores.getSelectedValue();
                            organizador.setNombre(vista.nombreOrganizadorTxt.getText());
                            organizador.setEmail(vista.emailOrganizadorTxt.getText());
                            organizador.setEdad(Integer.parseInt(vista.edadOrganizadorTxt.getText()));
                            modelo.modificarObjeto(organizador);
                            limpiarCamposOrganizador();
                        } else {
                            Util.mostrarMensajeError("No ha sido posible modificar el organizador en la base de datos.\n" +
                                    "Compruebe que los campos contengan el tipo de dato requerido.");
                        }
                        listarOrganizadores();
                    } else {
                        Util.mostrarMensajeError("No hay ningún elemento seleccionado.");
                    }
                } catch (NumberFormatException err) {
                    Util.mostrarMensajeError("La edad tiene que ser un número");
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
                break;
            case "Modo Claro":
            case "Modo Oscuro":
                cambiarTema();
                break;
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
                vista.fechaEventoDPicker.setDate(evento.getFecha());
                vista.precioEventoTxt.setText(String.valueOf(evento.getPrecio()));
                obtenerOrganizadorDeEvento(evento);
                listarActividadesPorEvento(evento);
            }
        } else if (e.getSource() == vista.listActividades) {
            if (vista.listActividades.getSelectedValue() != null) {
                Actividad actividad = vista.listActividades.getSelectedValue();
                vista.descripcionActividadTxt.setText(actividad.getDescripcion());
                vista.duracionActividadTxt.setText(String.valueOf(actividad.getDuracion()));
                vista.participantesActividadTxt.setText(String.valueOf(actividad.getNumParticipantes()));
                obtenerEventoDeActividad(actividad);
            }
        } else if (e.getSource() == vista.listOrganizadores) {
            if (vista.listOrganizadores.getSelectedValue() != null) {
                Organizador organizador = vista.listOrganizadores.getSelectedValue();
                vista.nombreOrganizadorTxt.setText(organizador.getNombre());
                vista.emailOrganizadorTxt.setText(organizador.getEmail());
                vista.edadOrganizadorTxt.setText(String.valueOf(organizador.getEdad()));
                listarEventosPorOrganizador(organizador);
            }
        }
    }

    private void obtenerOrganizadorDeEvento(Evento evento) {
        for (int i = 0; i < vista.organizadorComboBox.getItemCount(); i++) {
            Organizador organizador = vista.organizadorComboBox.getItemAt(i);
            if (organizador.getId().equals(evento.getOrganizadorId())) {
                vista.organizadorComboBox.setSelectedItem(organizador);
                break;
            }
        }
    }

    private void obtenerEventoDeActividad(Actividad actividad) {
        for (int i = 0; i < vista.eventoComboBox.getItemCount(); i++) {
            Evento evento = vista.eventoComboBox.getItemAt(i);
            if (evento.getId().equals(actividad.getEventoId())) {
                vista.eventoComboBox.setSelectedItem(evento);
                break;
            }
        }
    }

    private boolean comprobarCamposEvento() {
        return !vista.nombreEventoTxt.getText().isEmpty() &&
                !vista.fechaEventoDPicker.getText().isEmpty() &&
                !vista.precioEventoTxt.getText().isEmpty() &&
                !vista.organizadorComboBox.getSelectedItem().toString().isEmpty();
    }

    private boolean comprobarCamposActividad() {
        return !vista.descripcionActividadTxt.getText().isEmpty() &&
                !vista.duracionActividadTxt.getText().isEmpty() &&
                !vista.participantesActividadTxt.getText().isEmpty() &&
                comprobarFloat(vista.duracionActividadTxt.getText()) &&
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
        vista.fechaEventoDPicker.setDate(null);
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

    private void refrescarComboEvento() {
        vista.dcbEventos.removeAllElements();
        for (Evento evento : modelo.getEventos()) {
            vista.dcbEventos.addElement(evento);
        }
    }

    private void refrescarComboOrganizador() {
        vista.dcbOrganizadores.removeAllElements();
        for (Organizador organizador : modelo.getOrganizadores()) {
            vista.dcbOrganizadores.addElement(organizador);
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

    private void listarEventosPorOrganizador(Organizador organizador) {
        vista.dlmEventosPorOrganizador.clear();
        for (Evento evento : modelo.getEventosPorOrganizador(organizador)) {
            vista.dlmEventosPorOrganizador.addElement(evento);
        }
    }

    private void listarActividadesPorEvento(Evento evento) {
        vista.dlmActividadesPorEvento.clear();
        for (Actividad actividad : modelo.getActividadesPorEvento(evento)) {
            vista.dlmActividadesPorEvento.addElement(actividad);
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

    private void cambiarTema() {
        try {
            if (modoOscuro) {
                UIManager.setLookAndFeel(new FlatLightLaf());
                vista.modoOscuroItem.setText("Modo Oscuro");
            } else {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                vista.modoOscuroItem.setText("Modo Claro");
            }
            modoOscuro = !modoOscuro;
            SwingUtilities.updateComponentTreeUI(vista);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        int resp = Util.showConfirmDialog("¿Desea salir de la aplicación?", "Salir");
        if (resp == JOptionPane.OK_OPTION) {
            modelo.desconectar();
            System.exit(0);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }
}
