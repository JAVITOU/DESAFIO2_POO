package com.colegio.ui;

import com.colegio.database.Database;
import com.colegio.models.Estudiante;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EstudianteFrame extends JFrame {
    private JTextField txtNombre, txtEdad, txtDireccion, txtTelefono;
    private JButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar, btnVolver;
    private JList<Estudiante> listaEstudiantes;
    private DefaultListModel<Estudiante> listModel;
    private Database database;

    public EstudianteFrame() {
        database = Database.getInstance();
        initializeUI();
        cargarEstudiantes();
    }

    private void initializeUI() {
        setTitle("Gestión de Estudiantes");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StyleUtils.BACKGROUND_COLOR);
        setLayout(new BorderLayout(15, 15));

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleUtils.BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("👥 Gestión de Estudiantes");
        titleLabel.setFont(StyleUtils.TITLE_FONT);
        titleLabel.setForeground(StyleUtils.PRIMARY_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        btnVolver = StyleUtils.createSecondaryButton("← Volver al Menú");
        btnVolver.addActionListener(e -> {
            new MainFrame();
            this.dispose();
        });
        headerPanel.add(btnVolver, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel izquierdo - Formulario
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setPreferredSize(new Dimension(400, 0));

        JPanel formPanel = StyleUtils.createStyledPanel("Datos del Estudiante");
        formPanel.setLayout(new GridLayout(5, 2, 10, 15));

        JLabel lblNombre = new JLabel("Nombre Completo:");
        lblNombre.setFont(StyleUtils.LABEL_FONT);
        formPanel.add(lblNombre);

        txtNombre = new JTextField();
        StyleUtils.styleTextField(txtNombre);
        formPanel.add(txtNombre);

        JLabel lblEdad = new JLabel("Edad:");
        lblEdad.setFont(StyleUtils.LABEL_FONT);
        formPanel.add(lblEdad);

        txtEdad = new JTextField();
        StyleUtils.styleTextField(txtEdad);
        formPanel.add(txtEdad);

        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setFont(StyleUtils.LABEL_FONT);
        formPanel.add(lblDireccion);

        txtDireccion = new JTextField();
        StyleUtils.styleTextField(txtDireccion);
        formPanel.add(txtDireccion);

        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(StyleUtils.LABEL_FONT);
        formPanel.add(lblTelefono);

        txtTelefono = new JTextField();
        StyleUtils.styleTextField(txtTelefono);
        formPanel.add(txtTelefono);

        leftPanel.add(formPanel, BorderLayout.NORTH);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(StyleUtils.PANEL_BACKGROUND);

        btnGuardar = StyleUtils.createPrimaryButton("💾 Guardar");
        btnActualizar = StyleUtils.createSecondaryButton("✏️ Actualizar");
        btnEliminar = StyleUtils.createDangerButton("🗑️ Eliminar");
        btnLimpiar = StyleUtils.createSecondaryButton("🧹 Limpiar");

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnLimpiar);

        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Panel derecho - Lista de estudiantes
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));

        JLabel listaTitle = new JLabel("Estudiantes Registrados", SwingConstants.CENTER);
        listaTitle.setFont(StyleUtils.SUBTITLE_FONT);
        listaTitle.setForeground(StyleUtils.PRIMARY_COLOR);
        rightPanel.add(listaTitle, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listaEstudiantes = new JList<>(listModel);
        listaEstudiantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listaEstudiantes);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Dividir la pantalla
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.4);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);

        // Listeners
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarEstudiante(e);
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarEstudiante(e);
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarEstudiante(e);
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario(e);
            }
        });

        listaEstudiantes.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    seleccionarEstudiante();
                }
            }
        });

        // ESTA LÍNEA ES CRÍTICA - DEBE ESTAR AL FINAL
        setVisible(true);
    }

    private void cargarEstudiantes() {
        listModel.clear();
        for (Estudiante estudiante : database.getEstudiantes()) {
            listModel.addElement(estudiante);
        }
    }

    private void guardarEstudiante(ActionEvent e) {
        if (!validarDatos()) return;

        Estudiante estudiante = new Estudiante(0,
                txtNombre.getText().trim(),
                Integer.parseInt(txtEdad.getText().trim()),
                txtDireccion.getText().trim(),
                txtTelefono.getText().trim());

        database.agregarEstudiante(estudiante);
        cargarEstudiantes();
        limpiarFormulario(null);
        JOptionPane.showMessageDialog(this, "✅ Estudiante guardado exitosamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarEstudiante(ActionEvent e) {
        Estudiante seleccionado = listaEstudiantes.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione un estudiante para actualizar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarDatos()) return;

        Estudiante estudianteActualizado = new Estudiante(seleccionado.getId(),
                txtNombre.getText().trim(),
                Integer.parseInt(txtEdad.getText().trim()),
                txtDireccion.getText().trim(),
                txtTelefono.getText().trim());

        if (database.actualizarEstudiante(seleccionado.getId(), estudianteActualizado)) {
            cargarEstudiantes();
            limpiarFormulario(null);
            JOptionPane.showMessageDialog(this, "✅ Estudiante actualizado exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void eliminarEstudiante(ActionEvent e) {
        Estudiante seleccionado = listaEstudiantes.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione un estudiante para eliminar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar este estudiante?\n\nNombre: " + seleccionado.getNombreCompleto() +
                        "\nEsta acción también eliminará todas sus notas.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (database.eliminarEstudiante(seleccionado.getId())) {
                cargarEstudiantes();
                limpiarFormulario(null);
                JOptionPane.showMessageDialog(this, "✅ Estudiante eliminado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void seleccionarEstudiante() {
        Estudiante seleccionado = listaEstudiantes.getSelectedValue();
        if (seleccionado != null) {
            txtNombre.setText(seleccionado.getNombreCompleto());
            txtEdad.setText(String.valueOf(seleccionado.getEdad()));
            txtDireccion.setText(seleccionado.getDireccion());
            txtTelefono.setText(seleccionado.getTelefono());
        }
    }

    private void limpiarFormulario(ActionEvent e) {
        txtNombre.setText("");
        txtEdad.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        listaEstudiantes.clearSelection();
    }

    private boolean validarDatos() {
        String nombre = txtNombre.getText().trim();
        String edadStr = txtEdad.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();

        // Validar nombre
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ El nombre completo es obligatorio",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            JOptionPane.showMessageDialog(this, "⚠️ El nombre solo puede contener letras y espacios",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar edad
        if (edadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ La edad es obligatoria",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int edad = Integer.parseInt(edadStr);
            if (edad < 5 || edad > 25) {
                JOptionPane.showMessageDialog(this, "⚠️ La edad debe estar entre 5 y 25 años",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "⚠️ La edad debe ser un número válido",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar dirección
        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ La dirección es obligatoria",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (direccion.length() < 10) {
            JOptionPane.showMessageDialog(this, "⚠️ La dirección debe tener al menos 10 caracteres",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar teléfono
        if (telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ El teléfono es obligatorio",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!telefono.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "⚠️ El teléfono solo puede contener números",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (telefono.length() != 8) {
            JOptionPane.showMessageDialog(this, "⚠️ El teléfono debe tener 8 dígitos",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}