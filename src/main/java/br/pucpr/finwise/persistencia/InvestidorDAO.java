package br.pucpr.finwise.persistencia;

import br.pucpr.finwise.modelo.Investidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InvestidorDAO {

    private static final String ARQUIVO = "investidores.csv";

    // Transforma UM investidor em uma linha de texto (CSV)
    private String paraLinha(Investidor investidor) {
        String linha = investidor.getId() + ";"
                + investidor.getNome() + ";"
                + investidor.getEmail() + ";"
                + investidor.getPerfilRisco() + ";"
                + investidor.getSaldo() + ";"
                + investidor.getDataCadastro();
        return linha;
    }

    // Transforma UMA linha de texto em um objeto Investidor
    private Investidor paraInvestidor(String linha) {
        String[] campos = linha.split(";");

        int id = Integer.parseInt(campos[0]);
        String nome = campos[1];
        String email = campos[2];
        String perfilRisco = campos[3];
        double saldo = Double.parseDouble(campos[4]);
        String dataCadastro = campos[5];

        Investidor investidor = new Investidor(id, nome, email, perfilRisco, saldo, dataCadastro);
        return investidor;
    }

    // Grava a lista inteira no arquivo (reescreve tudo)
    public void salvarTodos(List<Investidor> investidores) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Investidor investidor : investidores) {
                String linha = paraLinha(investidor);
                escritor.write(linha);
                escritor.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

    // Lê o arquivo inteiro e devolve a lista de investidores
    public List<Investidor> listarTodos() {
        List<Investidor> investidores = new ArrayList<>();
        try (BufferedReader leitor = new BufferedReader(new FileReader(ARQUIVO))){
            String linha = leitor.readLine();
            while (linha != null) {
                Investidor investidor = paraInvestidor(linha);
                investidores.add(investidor);
                linha = leitor.readLine();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return investidores;
    }
}