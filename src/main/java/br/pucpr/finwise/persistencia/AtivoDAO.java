package br.pucpr.finwise.persistencia;

import br.pucpr.finwise.modelo.Ativo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class AtivoDAO {

    private static final String ARQUIVO = "ativos.dat";

    public static void salvarLista(ArrayList<Ativo> lista) {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(ARQUIVO))) {

            out.writeObject(lista);

        } catch (IOException e) {
            System.out.println("Erro ao salvar ativos.");
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Ativo> lerLista() {

        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(ARQUIVO))) {

            return (ArrayList<Ativo>) in.readObject();

        } catch (IOException | ClassNotFoundException e) {

            return new ArrayList<>();
        }
    }
    }