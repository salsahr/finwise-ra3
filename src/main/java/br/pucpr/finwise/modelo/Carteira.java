package br.pucpr.finwise.modelo;

import java.io.Serializable;

public class Carteira implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private int investidorId;
    private double valorInvestido;
    private double rentabilidade;

    public Carteira(int id, String nome, int investidorId, double valorInvestido, double rentabilidade){
        this.id = id;
        this.nome = nome;
        this.investidorId = investidorId;
        this.valorInvestido = valorInvestido;
        this.rentabilidade = rentabilidade;
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

    public int getInvestidorId() {
        return investidorId;
    }

    public void setInvestidorId(int investidorId) {
        this.investidorId = investidorId;
    }

    public double getValorInvestido() {
        return valorInvestido;
    }

    public void setValorInvestido(double valorInvestido) {
        this.valorInvestido = valorInvestido;
    }

    public double getRentabilidade() {
        return rentabilidade;
    }

    public void setRentabilidade(double rentabilidade) {
        this.rentabilidade = rentabilidade;
    }
}
