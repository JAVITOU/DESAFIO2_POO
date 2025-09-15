package com.colegio.ui;

import com.colegio.database.Database;
import com.colegio.models.Estudiante;
import com.colegio.models.Nota;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

public class NotaFrame extends JFrame {
    private JComboBox<Estudiante> cmbEstudiante;
    private JComboBox<String> cmbGrado, cmbMateria;
    private JTextField txtNota;
    private JButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar;
    private JList<Nota> listaNotas;
    private DefaultListModel<Nota> listModel;
    private Database database;

    private static final List<String> GRADOS = Arrays.asList(
            "1° Primaria", "2° Primaria", "3° Primaria", "4° Primaria", "5° Primaria", "6° Primaria",
            "1° Secundaria", "2° Secundaria", "3° Secundaria", "4° Secundaria", "5° Secundaria"
    );

    private static final List<String> MATERIAS = Arrays.asList(
            "Matemáticas", "Lenguaje", "Ciencias", "Historia", "Geografía",
            "Inglés", "Educación Física", "Arte", "Música", "Tecnología"
    );

    public NotaFrame() {
        database = Database.getInstance();
        initializeUI();
        cargarNotas();
    }

    private void initializeUI() {
        setTitle("Gestión de Notas");
        setSize(1200, 800);
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

        JLabel titleLabel = new JLabel("📊 Gestión de Notas");
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

        // Panel izquierdo - Formulario
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setPreferredSize(new Dimension(450, 0));

        JPanel formPanel = StyleUtils.createStyledPanel("Registro de Notas");
        formPanel.setLayout(new GridLayout(5, 2, 10, 15));

        // Estudiante
        JLabel lblEstudiante = new JLabel("👤 Estudiante:");
        lblEstudiante.setFont(StyleUtils.LABEL_FONT);
        formPanel.add(lblEstudiante);

        cmbEstudiante = new JComboBox<>();
        StyleUtils.styleComboBox(cmbEstudiante);
        cargarEstudiantesCombo();
        formPanel.add(cmbEstudiante);

        // Grado
        JLabel lblGrado = new JLabel("🎓 Grado:");
        lblGrado.setFont(StyleUtils.LABEL_FONT);
        formPanel.add(lblGrado);

        cmbGrado = new JComboBox<>(GRADOS.toArray(new String[0]));
        StyleUtils.styleComboBox(cmbGrado);
        formPanel.add(cmbGrado);

        // Materia
        JLabel lblMateria = new JLabel("📚 Materia:");
        lblMateria.setFont(StyleUtils.LABEL_FONT);
        formPanel.add(lblMateria);

        cmbMateria = new JComboBox<>(MATERIAS.toArray(new String[0]));
        StyleUtils.styleComboBox(cmbMateria);
        formPanel.add(cmbMateria);

        // Nota
        JLabel lblNota = new JLabel("⭐ Nota Final (0-10):");
        lblNota.setFont(StyleUtils.LABEL_FONT);
        formPanel.add(lblNota);

        txtNota = new JTextField();
        StyleUtils.styleTextField(txtNota);
        formPanel.add(txtNota);

        leftPanel.add(formPanel, BorderLayout.NORTH);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBackground(StyleUtils.PANEL_BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(15, 10, 10, 10));

        btnGuardar = StyleUtils.createPrimaryButton("💾 Guardar");
        btnActualizar = StyleUtils.createSecondaryButton("✏️ Actualizar");
        btnEliminar = StyleUtils.createDangerButton("🗑️ Eliminar");
        btnLimpiar = StyleUtils.createSecondaryButton("🧹 Limpiar");

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnLimpiar);

        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Panel derecho - Lista de notas
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));

        JLabel listaTitle = new JLabel("📋 Notas Registradas", SwingConstants.CENTER);
        listaTitle.setFont(StyleUtils.SUBTITLE_FONT);
        listaTitle.setForeground(StyleUtils.PRIMARY_COLOR);
        rightPanel.add(listaTitle, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listaNotas = new JList<>(listModel);
        listaNotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaNotas.setCellRenderer(new NotaListCellRenderer());

        JScrollPane scrollPane = new JScrollPane(listaNotas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        scrollPane.setPreferredSize(new Dimension(500, 0));

        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de estadísticas
        JPanel statsPanel = StyleUtils.createStyledPanel("📈 Estadísticas");
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        statsPanel.setPreferredSize(new Dimension(0, 80));

        JLabel lblTotalNotas = new JLabel("Total notas: 0");
        JLabel lblPromedioGeneral = new JLabel("Promedio general: 0.00");
        JLabel lblMateriasRegistradas = new JLabel("Materias: 0");

        lblTotalNotas.setFont(StyleUtils.LABEL_FONT);
        lblPromedioGeneral.setFont(StyleUtils.LABEL_FONT);
        lblMateriasRegistradas.setFont(StyleUtils.LABEL_FONT);

        statsPanel.add(lblTotalNotas);
        statsPanel.add(lblPromedioGeneral);
        statsPanel.add(lblMateriasRegistradas);

        rightPanel.add(statsPanel, BorderLayout.SOUTH);

        // Dividir la pantalla
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(450);
        splitPane.setResizeWeight(0.4);
        splitPane.setBorder(new EmptyBorder(10, 0, 0, 0));

        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);

        // Listeners
        btnGuardar.addActionListener(this::guardarNota);
        btnActualizar.addActionListener(this::actualizarNota);
        btnEliminar.addActionListener(this::eliminarNota);
        btnLimpiar.addActionListener(this::limpiarFormulario);
        listaNotas.addListSelectionListener(e -> seleccionarNota());

        // Actualizar estadísticas
        actualizarEstadisticas(lblTotalNotas, lblPromedioGeneral, lblMateriasRegistradas);

        setVisible(true);
    }

    // Renderer personalizado para la lista de notas
    private class NotaListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Nota) {
                Nota nota = (Nota) value;
                String text = String.format("<html><b>%s</b> - %s<br>%s: <font color='%s'>%.2f</font></html>",
                        nota.getEstudiante().getNombreCompleto(),
                        nota.getGrado(),
                        nota.getMateria(),
                        nota.getNotaFinal() >= 6 ? "green" : "red",
                        nota.getNotaFinal());

                setText(text);
            }

            if (isSelected) {
                setBackground(new Color(220, 237, 255));
                setForeground(Color.BLACK);
            } else {
                setBackground(index % 2 == 0 ? new Color(250, 250, 250) : Color.WHITE);
            }

            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            setFont(StyleUtils.LABEL_FONT);

            return this;
        }
    }

    private void cargarEstudiantesCombo() {
        cmbEstudiante.removeAllItems();
        database.getEstudiantes().forEach(cmbEstudiante::addItem);
    }

    private void cargarNotas() {
        listModel.clear();
        database.getNotas().forEach(listModel::addElement);
    }

    private void guardarNota(ActionEvent e) {
        if (!validarDatosNota()) return;

        Estudiante estudiante = (Estudiante) cmbEstudiante.getSelectedItem();
        String grado = (String) cmbGrado.getSelectedItem();
        String materia = (String) cmbMateria.getSelectedItem();
        double nota = Double.parseDouble(txtNota.getText().trim());

        if (database.existeNota(estudiante, grado, materia)) {
            mostrarError("⚠️ Ya existe una nota registrada para este estudiante en la misma materia y grado");
            return;
        }

        Nota nuevaNota = new Nota(0, estudiante, grado, materia, nota);
        database.agregarNota(nuevaNota);
        cargarNotas();
        limpiarFormulario(null);
        mostrarExito("✅ Nota guardada exitosamente");
    }

    private void actualizarNota(ActionEvent e) {
        Nota seleccionada = listaNotas.getSelectedValue();
        if (seleccionada == null) {
            mostrarAdvertencia("⚠️ Seleccione una nota para actualizar");
            return;
        }

        if (!validarDatosNota()) return;

        Estudiante estudiante = (Estudiante) cmbEstudiante.getSelectedItem();
        String grado = (String) cmbGrado.getSelectedItem();
        String materia = (String) cmbMateria.getSelectedItem();
        double nota = Double.parseDouble(txtNota.getText().trim());

        if (database.existeNota(estudiante, grado, materia) &&
                !(seleccionada.getEstudiante().getId() == estudiante.getId() &&
                        seleccionada.getGrado().equals(grado) &&
                        seleccionada.getMateria().equals(materia))) {
            mostrarError("⚠️ Ya existe una nota registrada para este estudiante en la misma materia y grado");
            return;
        }

        Nota notaActualizada = new Nota(seleccionada.getId(), estudiante, grado, materia, nota);
        if (database.actualizarNota(seleccionada.getId(), notaActualizada)) {
            cargarNotas();
            limpiarFormulario(null);
            mostrarExito("✅ Nota actualizada exitosamente");
        }
    }

    private void eliminarNota(ActionEvent e) {
        Nota seleccionada = listaNotas.getSelectedValue();
        if (seleccionada == null) {
            mostrarAdvertencia("⚠️ Seleccione una nota para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                String.format("<html><b>¿Está seguro de eliminar esta nota?</b><br><br>" +
                                "Estudiante: %s<br>" +
                                "Grado: %s<br>" +
                                "Materia: %s<br>" +
                                "Nota: <font color='%s'>%.2f</font></html>",
                        seleccionada.getEstudiante().getNombreCompleto(),
                        seleccionada.getGrado(),
                        seleccionada.getMateria(),
                        seleccionada.getNotaFinal() >= 6 ? "green" : "red",
                        seleccionada.getNotaFinal()),
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (database.eliminarNota(seleccionada.getId())) {
                cargarNotas();
                limpiarFormulario(null);
                mostrarExito("✅ Nota eliminada exitosamente");
            }
        }
    }

    private void seleccionarNota() {
        Nota seleccionada = listaNotas.getSelectedValue();
        if (seleccionada != null) {
            cmbEstudiante.setSelectedItem(seleccionada.getEstudiante());
            cmbGrado.setSelectedItem(seleccionada.getGrado());
            cmbMateria.setSelectedItem(seleccionada.getMateria());
            txtNota.setText(String.valueOf(seleccionada.getNotaFinal()));
        }
    }

    private void limpiarFormulario(ActionEvent e) {
        if (cmbEstudiante.getItemCount() > 0) cmbEstudiante.setSelectedIndex(0);
        if (cmbGrado.getItemCount() > 0) cmbGrado.setSelectedIndex(0);
        if (cmbMateria.getItemCount() > 0) cmbMateria.setSelectedIndex(0);
        txtNota.setText("");
        listaNotas.clearSelection();
    }

    private boolean validarDatosNota() {
        if (cmbEstudiante.getSelectedItem() == null) {
            mostrarError("⚠️ Seleccione un estudiante");
            return false;
        }

        String notaStr = txtNota.getText().trim();
        if (notaStr.isEmpty()) {
            mostrarError("⚠️ La nota es obligatoria");
            return false;
        }

        try {
            double nota = Double.parseDouble(notaStr);
            if (nota < 0 || nota > 10) {
                mostrarError("⚠️ La nota debe estar entre 0 y 10");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("⚠️ La nota debe ser un número válido");
            return false;
        }

        return true;
    }

    private void actualizarEstadisticas(JLabel lblTotalNotas, JLabel lblPromedioGeneral, JLabel lblMateriasRegistradas) {
        List<Nota> notas = database.getNotas();
        List<Estudiante> estudiantes = database.getEstudiantes();

        int totalNotas = notas.size();
        int totalMaterias = (int) notas.stream()
                .map(n -> n.getMateria() + n.getEstudiante().getId())
                .distinct()
                .count();

        double promedioGeneral = 0;
        if (totalNotas > 0) {
            promedioGeneral = notas.stream()
                    .mapToDouble(Nota::getNotaFinal)
                    .average()
                    .orElse(0);
        }

        lblTotalNotas.setText("📊 Total notas: " + totalNotas);
        lblPromedioGeneral.setText("⭐ Promedio general: " + String.format("%.2f", promedioGeneral));
        lblMateriasRegistradas.setText("📚 Materias únicas: " + totalMaterias);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}