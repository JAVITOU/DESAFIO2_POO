package com.colegio.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
    private JButton btnGestionEstudiantes, btnGestionNotas, btnListarEstudiantes, btnSalir;

    public MainFrame() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Sistema de Gestión de Notas - Panel Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StyleUtils.BACKGROUND_COLOR);
        setLayout(new BorderLayout(20, 20));

        // Panel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titleLabel = new JLabel("Panel de Control", SwingConstants.CENTER);
        titleLabel.setFont(StyleUtils.TITLE_FONT);
        titleLabel.setForeground(StyleUtils.PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        buttonPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        btnGestionEstudiantes = StyleUtils.createPrimaryButton("👥 Gestión de Estudiantes");
        btnGestionNotas = StyleUtils.createPrimaryButton("📊 Gestión de Notas");
        btnListarEstudiantes = StyleUtils.createSecondaryButton("📋 Listar Estudiantes con Notas");
        btnSalir = StyleUtils.createDangerButton("🚪 Salir del Sistema");

        buttonPanel.add(btnGestionEstudiantes);
        buttonPanel.add(btnGestionNotas);
        buttonPanel.add(btnListarEstudiantes);
        buttonPanel.add(btnSalir);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        // Información del sistema
        JLabel infoLabel = new JLabel("Sistema de Gestión Escolar v1.0", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(Color.GRAY);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        mainPanel.add(infoLabel, gbc);

        add(mainPanel, BorderLayout.CENTER);

     
        btnGestionEstudiantes.addActionListener(e -> abrirGestionEstudiantes());
        btnGestionNotas.addActionListener(e -> abrirGestionNotas());
        btnListarEstudiantes.addActionListener(e -> abrirListaEstudiantes());
        btnSalir.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    // MÉTODOS CORREGIDOS - SIN dispose()
    private void abrirGestionEstudiantes() {
        System.out.println("Abriendo gestión de estudiantes...");
        new EstudianteFrame();
        // this.dispose(); ← REMOVER ESTA LÍNEA
    }

    private void abrirGestionNotas() {
        System.out.println("Abriendo gestión de notas...");
        new NotaFrame();
        // this.dispose(); ← REMOVER ESTA LÍNEA
    }

    private void abrirListaEstudiantes() {
        System.out.println("Abriendo lista de estudiantes...");
        new ListaFrame();
        // this.dispose(); ← REMOVER ESTA LÍNEA
    }
}
