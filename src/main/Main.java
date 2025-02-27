package main;

import com.formdev.flatlaf.FlatLightLaf;

public class Main {

    public static void main(String[] args) {

        FlatLightLaf.setup();

        new Controlador(new Modelo(), new Vista());
    }
}
