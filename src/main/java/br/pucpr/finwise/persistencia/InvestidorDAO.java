package br.pucpr.finwise.persistencia;

import br.pucpr.finwise.modelo.Investidor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class InvestidorDAO {

    private static final String CAMINHO_ARQUIVO = "investidores.dat";

    // Grava a lista inteira de investidores no arquivo
    public static void salvarLista(ArrayList<Investidor> investidores) {
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }
            ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(arquivo));
            saida.writeObject(investidores);
            saida.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar a lista de investidores: " + e.getMessage());
        }
    }

    // Lê a lista inteira de investidores do arquivo
    public static ArrayList<Investidor> lerLista() {
        ArrayList<Investidor> lista = new ArrayList<>();
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            if (arquivo.exists()) {
                ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(CAMINHO_ARQUIVO));
                lista = (ArrayList<Investidor>) entrada.readObject();
                entrada.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao ler a lista de investidores: " + e.getMessage());
        }
        return lista;
    }
}