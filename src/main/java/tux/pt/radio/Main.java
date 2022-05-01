package tux.pt.radio;

import com.formdev.flatlaf.FlatDarculaLaf;
import tux.pt.radio.gui.RadioGui;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(RadioGui.class.getName()).log(Level.SEVERE, "Error: ", ex);
        } finally {
            EventQueue.invokeLater(() -> new RadioGui().setVisible(true));
        }
    }
}
