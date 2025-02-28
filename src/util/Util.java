package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;

public class Util {

    public static void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static int showConfirmDialog(String s, String salir) {
        return JOptionPane.showConfirmDialog(null, s, salir, JOptionPane.YES_NO_OPTION);
    }
}
