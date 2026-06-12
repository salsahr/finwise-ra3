package br.pucpr.finwise.modelo;

import java.io.Serializable;

public class Investidor implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String email;
    private String perfilRisco;
    private double saldo;
    private String dataCadastro;

    public Investidor(int id, String nome,String email, String perfilRisco, double saldo, String dataCadastro){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfilRisco = perfilRisco;
        this.saldo = saldo;
        this.dataCadastro =dataCadastro;
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

    public String getPerfilRisco() {
        return perfilRisco;
    }

    public void setPerfilRisco(String perfilRisco) {
        this.perfilRisco = perfilRisco;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
