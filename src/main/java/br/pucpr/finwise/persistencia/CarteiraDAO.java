package br.pucpr.finwise.persistencia;

import br.pucpr.finwise.modelo.Carteira;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CarteiraDAO {

    private static final String ARQUIVO = "carteiras.csv";

    private String paraLinha(Carteira carteira) {
        String linha = carteira.getId() + ";"
                + carteira.getNome() + ";"
                + carteira.getInvestidorId() + ";"
                + carteira.getValorInvestido() + ";"
                + carteira.getRentabilidade();
        return linha;
    }

    private Carteira paraCarteira(String linha) {
        String[] campos = linha.split(";");

        int id = Integer.parseInt(campos[0]);
        String nome = campos[1];
        int investidorId = Integer.parseInt(campos[2]);
        double valorInvestido = Double.parseDouble(campos[3]);
        double rentabilidade = Double.parseDouble(campos[4]);

        Carteira carteira = new Carteira(id, nome, investidorId, valorInvestido, rentabilidade);
        return carteira;
    }

    public void salvarTodos(List<Carteira> carteiras) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Carteira carteira : carteiras) {
                String linha = paraLinha(carteira);
                escritor.write(linha);
                escritor.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

    public List<Carteira> listarTodos() {
        List<Carteira> carteiras = new ArrayList<>();
        try (BufferedReader leitor = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha = leitor.readLine();
            while (linha != null) {
                Carteira carteira = paraCarteira(linha);
                carteiras.add(carteira);
                linha = leitor.readLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        return carteiras;
    }
}