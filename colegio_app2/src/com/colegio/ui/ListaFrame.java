package com.colegio.ui;

import com.colegio.database.Database;
import com.colegio.models.Estudiante;
import com.colegio.models.Nota;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListaFrame extends JFrame {
    private JTable tablaEstudiantes;
    private DefaultTableModel tableModel;
    private Database database;

    public ListaFrame() {
        database = Database.getInstance();
        initializeUI();
        cargarDatos();
    }

    private void initializeUI() {
        setTitle("Lista de Estudiantes con Notas");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StyleUtils.BACKGROUND_COLOR);
        setLayout(new BorderLayout(15, 15));

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header con botón volver
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleUtils.BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("📋 Lista de Estudiantes con Notas");
        titleLabel.setFont(StyleUtils.TITLE_FONT);
        titleLabel.setForeground(StyleUtils.PRIMARY_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton btnVolver = StyleUtils.createSecondaryButton("← Volver al Menú");
        btnVolver.addActionListener(e -> {
            new MainFrame();
            this.dispose();
        });
        headerPanel.add(btnVolver, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Modelo de tabla
        String[] columnas = {"ID", "Nombre Completo", "Edad", "Dirección", "Teléfono",
                "Promedio General", "Materias Registradas"};
        tableModel = new DefaultTableModel(columnas, 0) {
                  public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };

        tablaEstudiantes = new JTable(tableModel);
        StyleUtils.styleTable(tablaEstudiantes);

        JScrollPane scrollPane = new JScrollPane(tablaEstudiantes);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de estadísticas
        JPanel statsPanel = StyleUtils.createStyledPanel("Estadísticas Generales");
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        statsPanel.setPreferredSize(new Dimension(0, 80));

        JLabel lblTotalEstudiantes = new JLabel("Total estudiantes: 0");
        JLabel lblTotalNotas = new JLabel("Total notas registradas: 0");
        JLabel lblPromedioGeneral = new JLabel("Promedio general: 0.00");

        lblTotalEstudiantes.setFont(StyleUtils.LABEL_FONT);
        lblTotalNotas.setFont(StyleUtils.LABEL_FONT);
        lblPromedioGeneral.setFont(StyleUtils.LABEL_FONT);

        statsPanel.add(lblTotalEstudiantes);
        statsPanel.add(lblTotalNotas);
        statsPanel.add(lblPromedioGeneral);

        mainPanel.add(statsPanel, BorderLayout.SOUTH);

        add(mainPanel);


        setVisible(true);

        // Actualizar estadísticas después de cargar datos
        actualizarEstadisticas(lblTotalEstudiantes, lblTotalNotas, lblPromedioGeneral);
    }

    private void cargarDatos() {
        tableModel.setRowCount(0); // Limpiar tabla

        List<Estudiante> estudiantes = database.getEstudiantes();
        List<Nota> notas = database.getNotas();

        // Agrupar notas por estudiante
        Map<Estudiante, List<Nota>> notasPorEstudiante = notas.stream()
                .collect(Collectors.groupingBy(Nota::getEstudiante));

        for (Estudiante estudiante : estudiantes) {
            List<Nota> notasEstudiante = notasPorEstudiante.get(estudiante);

            double promedio = 0;
            int materiasRegistradas = 0;
            String materiasStr = "Ninguna";

            if (notasEstudiante != null && !notasEstudiante.isEmpty()) {
                promedio = notasEstudiante.stream()
                        .mapToDouble(Nota::getNotaFinal)
                        .average()
                        .orElse(0);

                materiasRegistradas = notasEstudiante.size();
                materiasStr = notasEstudiante.stream()
                        .map(n -> n.getMateria() + " (" + n.getGrado() + ")")
                        .collect(Collectors.joining(", "));
            }

            Object[] row = {
                    estudiante.getId(),
                    estudiante.getNombreCompleto(),
                    estudiante.getEdad(),
                    estudiante.getDireccion(),
                    estudiante.getTelefono(),
                    String.format("%.2f", promedio),
                    materiasRegistradas + " materias: " + materiasStr
            };

            tableModel.addRow(row);
        }
    }

    private void actualizarEstadisticas(JLabel lblTotalEstudiantes, JLabel lblTotalNotas, JLabel lblPromedioGeneral) {
        List<Estudiante> estudiantes = database.getEstudiantes();
        List<Nota> notas = database.getNotas();

        int totalEstudiantes = estudiantes.size();
        int totalNotas = notas.size();

        double promedioGeneral = 0;
        if (totalNotas > 0) {
            promedioGeneral = notas.stream()
                    .mapToDouble(Nota::getNotaFinal)
                    .average()
                    .orElse(0);
        }

        lblTotalEstudiantes.setText("Total estudiantes: " + totalEstudiantes);
        lblTotalNotas.setText("Total notas registradas: " + totalNotas);
        lblPromedioGeneral.setText("Promedio general: " + String.format("%.2f", promedioGeneral));
    }
}
