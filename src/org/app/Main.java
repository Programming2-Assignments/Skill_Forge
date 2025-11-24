package org.app;

import com.formdev.flatlaf.FlatDarkLaf;
import org.ui.admin.MainAppWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        FlatDarkLaf.setup();

        SwingUtilities.invokeLater(() -> {
            MainAppWindow window = new MainAppWindow();
            window.setVisible(true);
        });
    }
}

