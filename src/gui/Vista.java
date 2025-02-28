package gui;

import base.Actividad;
import base.Evento;
import base.Organizador;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;

public class Vista extends JFrame {
    private JPanel panelPrincipal;

    // Eventos
    JTextField nombreEventoTxt;
    DatePicker fechaEventoDPicker;
    JTextField precioEventoTxt;
    JComboBox<Organizador> organizadorComboBox;
    JButton btnAddEvento;
    JButton btnModEvento;
    JButton btnDelEvento;
    JList<Evento> listEventos;
    JTextField txtBuscarEvento;
    JList<Evento> listBusquedaEvento;
    JList<Actividad> listActividadesPorEvento;

    // Actividades
    JTextField descripcionActividadTxt;
    JTextField duracionActividadTxt;
    JTextField participantesActividadTxt;
    JComboBox<Evento> eventoComboBox;
    JList<Actividad> listActividades;
    JButton btnAddActividad;
    JButton btnModActividad;
    JButton btnDelActividad;
    JTextField txtBuscarActividades;
    JList<Actividad> listBusquedaActividad;

    // Organizadores
    JTextField nombreOrganizadorTxt;
    JTextField emailOrganizadorTxt;
    JTextField edadOrganizadorTxt;
    JButton btnAddOrganizador;
    JButton btnModOrganizador;
    JButton btnDelOrganizador;
    JList<Organizador> listOrganizadores;
    JList<Organizador> listBusquedaOrganizador;
    JList<Evento> listEventosPorOrganizador;
    JTextField txtBuscarOrganizador;

    // Modelos
    DefaultListModel<Evento> dlmEventos;
    DefaultListModel<Actividad> dlmActividades;
    DefaultListModel<Organizador> dlmOrganizadores;
    DefaultListModel<Evento> dlmEventosBusqueda;
    DefaultListModel<Actividad> dlmActividadBusqueda;
    DefaultListModel<Organizador> dlmOrganizadorBusqueda;
    DefaultListModel<Actividad> dlmActividadesPorEvento;
    DefaultListModel<Evento> dlmEventosPorOrganizador;

    DefaultComboBoxModel<Organizador> dcbOrganizadores;
    DefaultComboBoxModel<Evento> dcbEventos;

    // Menu
    JMenuItem itemConectar;
    JMenuItem modoOscuroItem;
    JMenuItem itemSalir;

    public Vista() {
        setTitle("App eventos - <SIN CONEXION>");
        ImageIcon logo = new ImageIcon("icon.png");
        this.setIconImage(logo.getImage());
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(900, 650));
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        inicializarModelos();
        inicializarMenu();
    }

    private void inicializarModelos() {
        dlmEventos = new DefaultListModel<>();
        dlmActividades = new DefaultListModel<>();
        dlmOrganizadores = new DefaultListModel<>();
        dlmEventosBusqueda = new DefaultListModel<>();
        dlmActividadBusqueda = new DefaultListModel<>();
        dlmOrganizadorBusqueda = new DefaultListModel<>();
        dlmEventosPorOrganizador = new DefaultListModel<>();
        dlmActividadesPorEvento = new DefaultListModel<>();

        dcbOrganizadores = new DefaultComboBoxModel<>();
        dcbEventos = new DefaultComboBoxModel<>();

        listEventos.setModel(dlmEventos);
        listActividades.setModel(dlmActividades);
        listOrganizadores.setModel(dlmOrganizadores);
        listBusquedaEvento.setModel(dlmEventosBusqueda);
        listBusquedaActividad.setModel(dlmActividadBusqueda);
        listBusquedaOrganizador.setModel(dlmOrganizadorBusqueda);
        listEventosPorOrganizador.setModel(dlmEventosPorOrganizador);
        listActividadesPorEvento.setModel(dlmActividadesPorEvento);

        organizadorComboBox.setModel(dcbOrganizadores);
        eventoComboBox.setModel(dcbEventos);
    }

    private void inicializarMenu() {
        itemConectar = new JMenuItem("Conectar");
        itemConectar.setActionCommand("conexion");

        modoOscuroItem = new JMenuItem("Modo Oscuro");
        modoOscuroItem.setActionCommand("Modo Oscuro");

        itemSalir = new JMenuItem("Salir");
        itemSalir.setActionCommand("salir");

        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.add(itemConectar);
        menuArchivo.add(modoOscuroItem);
        menuArchivo.add(itemSalir);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuArchivo);

        setJMenuBar(menuBar);
    }
}
