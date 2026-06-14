package br.pucpr.finwise.persistencia;

import br.pucpr.finwise.modelo.Matricula;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MatriculaDAO {

    private static final String CAMINHO_ARQUIVO = "matriculas.dat";

    public static void salvarLista(ArrayList<Matricula> matriculas) {
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }
            ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(arquivo));
            saida.writeObject(matriculas);
            saida.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar a lista de matrículas: " + e.getMessage());
        }
    }

    public static ArrayList<Matricula> lerLista() {
        ArrayList<Matricula> lista = new ArrayList<>();
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            if (arquivo.exists()) {
                ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(CAMINHO_ARQUIVO));
                lista = (ArrayList<Matricula>) entrada.readObject();
                entrada.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao ler a lista de matrículas: " + e.getMessage());
        }
        return lista;
    }
}