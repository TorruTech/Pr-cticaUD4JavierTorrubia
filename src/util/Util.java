package util;

import javax.swing.*;

public class Util {

    public static void mostrarMensajeError(String mensaje){
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
