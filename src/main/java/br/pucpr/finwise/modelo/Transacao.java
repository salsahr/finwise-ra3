package br.pucpr.finwise.modelo;

import java.io.Serializable;

public class Transacao implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int carteiraId;
    private int ativoId;
    private String tipoOperacao;
    private double valor;

    public Transacao(int id,
                     int carteiraId,
                     int ativoId,
                     String tipoOperacao,
                     double valor) {

        this.id = id;
        this.carteiraId = carteiraId;
        this.ativoId = ativoId;
        this.tipoOperacao = tipoOperacao;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public int getCarteiraId() {
        return carteiraId;
    }

    public void setCarteiraId(int carteiraId) {
        this.carteiraId = carteiraId;
    }

    public int getAtivoId() {
        return ativoId;
    }

    public void setAtivoId(int ativoId) {
        this.ativoId = ativoId;
    }

    public String getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(String tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}