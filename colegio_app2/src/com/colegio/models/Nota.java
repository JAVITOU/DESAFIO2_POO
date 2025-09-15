package com.colegio.models;

public class Nota {
    private int id;
    private Estudiante estudiante;
    private String grado;
    private String materia;
    private double notaFinal;

    public Nota(int id, Estudiante estudiante, String grado, String materia, double notaFinal) {
        this.id = id;
        this.estudiante = estudiante;
        this.grado = grado;
        this.materia = materia;
        this.notaFinal = notaFinal;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public String getGrado() { return grado; }
    public void setGrado(String grado) { this.grado = grado; }

    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }

    public double getNotaFinal() { return notaFinal; }
    public void setNotaFinal(double notaFinal) { this.notaFinal = notaFinal; }
}