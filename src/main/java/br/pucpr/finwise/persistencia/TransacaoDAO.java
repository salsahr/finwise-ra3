package br.pucpr.finwise.persistencia;

import br.pucpr.finwise.modelo.Transacao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class TransacaoDAO {

    private static final String ARQUIVO = "transacoes.dat";

    public static void salvarLista(ArrayList<Transacao> lista) {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(ARQUIVO))) {

            out.writeObject(lista);

        } catch (IOException e) {
            System.out.println("Erro ao salvar transações.");
        }
    }

    public static ArrayList<Transacao> lerLista() {

        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(ARQUIVO))) {

            return (ArrayList<Transacao>) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
    return new ArrayList<>();
}
    }
}