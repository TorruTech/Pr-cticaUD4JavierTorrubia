package main;

import com.formdev.flatlaf.FlatLightLaf;
import gui.Controlador;
import gui.Modelo;
import gui.Vista;

public class Main {

    public static void main(String[] args) {

        FlatLightLaf.setup();

        new Controlador(new Modelo(), new Vista());
    }
}
