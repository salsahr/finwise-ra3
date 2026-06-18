package br.pucpr.finwise.modelo;

import java.io.Serializable;

public class Ativo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String ticker;
    private String tipo;

    public Ativo(int id, String nome, String ticker, String tipo) {
        this.id = id;
        this.nome = nome;
        this.ticker = ticker;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return nome + " (" + ticker + ")";
    }
}