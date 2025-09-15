package com.colegio.database;

import com.colegio.models.Usuario;
import com.colegio.models.Estudiante;
import com.colegio.models.Nota;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Database instance;
    private List<Usuario> usuarios;
    private List<Estudiante> estudiantes;
    private List<Nota> notas;
    private int estudianteIdCounter = 1;
    private int notaIdCounter = 1;

    private Database() {
        usuarios = new ArrayList<>();
        estudiantes = new ArrayList<>();
        notas = new ArrayList<>();

        // Usuario por defecto
        usuarios.add(new Usuario("admin", "password123"));
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // Métodos para usuarios
    public boolean agregarUsuario(Usuario usuario) {
        if (existeUsuario(usuario.getUsername())) {
            return false;
        }
        usuarios.add(usuario);
        return true;
    }

    public boolean existeUsuario(String username) {
        return usuarios.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public boolean validarLogin(String username, String password) {
        return usuarios.stream()
                .anyMatch(u -> u.getUsername().equals(username) && u.getPassword().equals(password));
    }

    // Métodos para estudiantes
    public void agregarEstudiante(Estudiante estudiante) {
        estudiante.setId(estudianteIdCounter++);
        estudiantes.add(estudiante);
    }

    public List<Estudiante> getEstudiantes() {
        return new ArrayList<>(estudiantes);
    }

    public boolean actualizarEstudiante(int id, Estudiante estudianteActualizado) {
        for (int i = 0; i < estudiantes.size(); i++) {
            if (estudiantes.get(i).getId() == id) {
                estudianteActualizado.setId(id);
                estudiantes.set(i, estudianteActualizado);
                return true;
            }
        }
        return false;
    }

    public boolean eliminarEstudiante(int id) {
        return estudiantes.removeIf(e -> e.getId() == id);
    }

    public Estudiante getEstudiantePorId(int id) {
        return estudiantes.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Métodos para notas
    public void agregarNota(Nota nota) {
        nota.setId(notaIdCounter++);
        notas.add(nota);
    }

    public List<Nota> getNotas() {
        return new ArrayList<>(notas);
    }

    public boolean existeNota(Estudiante estudiante, String grado, String materia) {
        return notas.stream().anyMatch(n ->
                n.getEstudiante().getId() == estudiante.getId() &&
                        n.getGrado().equals(grado) &&
                        n.getMateria().equals(materia));
    }

    public boolean actualizarNota(int id, Nota notaActualizada) {
        for (int i = 0; i < notas.size(); i++) {
            if (notas.get(i).getId() == id) {
                notaActualizada.setId(id);
                notas.set(i, notaActualizada);
                return true;
            }
        }
        return false;
    }

    public boolean eliminarNota(int id) {
        return notas.removeIf(n -> n.getId() == id);
    }

    public Nota getNotaPorId(int id) {
        return notas.stream()
                .filter(n -> n.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void closeConnection() {
    }
}