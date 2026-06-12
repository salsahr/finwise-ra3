package br.pucpr.finwise.persistencia;

import br.pucpr.finwise.modelo.Carteira;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CarteiraDAO {

    private static final String CAMINHO_ARQUIVO = "carteiras.dat";

    // Grava a lista inteira de carteiras no arquivo
    public static void salvarLista(ArrayList<Carteira> carteiras) {
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }
            ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(arquivo));
            saida.writeObject(carteiras);
            saida.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar a lista de carteiras: " + e.getMessage());
        }
    }

    // Lê a lista inteira de carteiras do arquivo
    public static ArrayList<Carteira> lerLista() {
        ArrayList<Carteira> lista = new ArrayList<>();
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            if (arquivo.exists()) {
                ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(CAMINHO_ARQUIVO));
                lista = (ArrayList<Carteira>) entrada.readObject();
                entrada.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao ler a lista de carteiras: " + e.getMessage());
        }
        return lista;
    }
}