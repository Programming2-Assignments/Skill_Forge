package org.app;

import com.formdev.flatlaf.FlatDarkLaf;
import org.ui.admin.MainAppWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // 1️⃣ Apply FlatLaf FIRST
        FlatDarkLaf.setup();

        // 2️⃣ Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainAppWindow window = new MainAppWindow();
            window.setVisible(true);
        });
    }
}
