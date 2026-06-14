package br.pucpr.finwise.persistencia;

import br.pucpr.finwise.modelo.Curso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CursoDAO {

    private static final String CAMINHO_ARQUIVO = "cursos.dat";

    public static void salvarLista(ArrayList<Curso> cursos) {
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }
            ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(arquivo));
            saida.writeObject(cursos);
            saida.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar a lista de cursos: " + e.getMessage());
        }
    }

    public static ArrayList<Curso> lerLista() {
        ArrayList<Curso> lista = new ArrayList<>();
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            if (arquivo.exists()) {
                ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(CAMINHO_ARQUIVO));
                lista = (ArrayList<Curso>) entrada.readObject();
                entrada.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao ler a lista de cursos: " + e.getMessage());
        }
        return lista;
    }
}