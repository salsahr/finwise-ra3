package br.pucpr.finwise.modelo;

import java.io.Serializable;

public class Curso implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private int cargaHoraria;

    public Curso(int id, String nome, int cargaHoraria) {
        this.id = id;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getCargaHoraria() { return cargaHoraria; }
    public void setCargaHoraria(int cargaHoraria) { this.cargaHoraria = cargaHoraria; }

    @Override
    public String toString() {
        return id + " - " + nome + " (" + cargaHoraria + "h)";
    }
}