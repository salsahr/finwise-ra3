package br.pucpr.finwise.modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Matricula implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nomeAluno;
    private Curso curso;
    private LocalDate dataMatricula;

    public Matricula(String nomeAluno, Curso curso) {
        this.nomeAluno = nomeAluno;
        this.curso = curso;
        this.dataMatricula = LocalDate.now();
    }

    public String getNomeAluno() { return nomeAluno; }
    public void setNomeAluno(String nomeAluno) { this.nomeAluno = nomeAluno; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public LocalDate getDataMatricula() { return dataMatricula; }

    @Override
    public String toString() {
        return "Aluno: " + nomeAluno + " | Curso: " + curso.getNome() + " | Data: " + dataMatricula;
    }
}