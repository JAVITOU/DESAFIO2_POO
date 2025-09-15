package com.colegio;

import com.colegio.ui.LoginFrame;
import com.colegio.database.Database;

public class Main {
    public static void main(String[] args) {
        // Agregar shutdown hook para cerrar la conexión
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Database.getInstance().closeConnection();
        }));

        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}
