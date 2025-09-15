package com.colegio.ui;

import com.colegio.database.Database;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegistrar;
    private Database database;

    public LoginFrame() {
        database = Database.getInstance();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Sistema de Gestión de Notas - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StyleUtils.BACKGROUND_COLOR);
        setLayout(new BorderLayout(20, 20));

        // Panel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titleLabel = new JLabel("Sistema de Gestión de Notas", SwingConstants.CENTER);
        titleLabel.setFont(StyleUtils.TITLE_FONT);
        titleLabel.setForeground(StyleUtils.PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Panel de formulario
        JPanel formPanel = StyleUtils.createStyledPanel("Iniciar Sesión");
        formPanel.setLayout(new GridLayout(4, 2, 10, 15));

        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setFont(StyleUtils.LABEL_FONT);
        formPanel.add(userLabel);

        txtUsername = new JTextField();
        StyleUtils.styleTextField(txtUsername);
        formPanel.add(txtUsername);

        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(StyleUtils.LABEL_FONT);
        formPanel.add(passLabel);

        txtPassword = new JPasswordField();
        StyleUtils.styleTextField(txtPassword);
        formPanel.add(txtPassword);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        mainPanel.add(formPanel, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(StyleUtils.BACKGROUND_COLOR);

        btnLogin = StyleUtils.createPrimaryButton("Iniciar Sesión");
        btnRegistrar = StyleUtils.createSecondaryButton("Registrarse");

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegistrar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        // Footer
        JLabel footerLabel = new JLabel("© 2024 Sistema de Gestión Escolar", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerLabel.setForeground(Color.GRAY);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        mainPanel.add(footerLabel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Listeners
        btnLogin.addActionListener(this::loginAction);
        btnRegistrar.addActionListener(this::registrarAction);

        setVisible(true);
    }

    private void loginAction(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y contraseña son obligatorios",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 8) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 8 caracteres",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (database.validarLogin(username, password)) {
            JOptionPane.showMessageDialog(this, "¡Bienvenido al sistema!",
                    "Login exitoso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new MainFrame();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos",
                    "Error de autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarAction(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y contraseña son obligatorios",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 8) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 8 caracteres",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (database.existeUsuario(username)) {
            JOptionPane.showMessageDialog(this, "El usuario ya existe",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        database.agregarUsuario(new com.colegio.models.Usuario(username, password));
        JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente",
                "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

        // Limpiar campos después del registro
        txtPassword.setText("");
    }
}