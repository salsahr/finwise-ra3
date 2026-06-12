package br.pucpr.finwise.ui;

import br.pucpr.finwise.modelo.Investidor;
import br.pucpr.finwise.persistencia.InvestidorDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class TelaInvestidor {


    private final ObservableList<Investidor> investidores = FXCollections.observableArrayList();

    private final TextField campoNome = new TextField();
    private final TextField campoEmail = new TextField();
    private final ComboBox<String> campoPerfil = new ComboBox<>();
    private final TextField campoSaldo = new TextField();
    private final DatePicker campoData = new DatePicker();

    private final TableView<Investidor> tabela = new TableView<>();

    private final DateTimeFormatter formatoBR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void mostrar() {
        Stage janela = new Stage();
        janela.setTitle("Finwise - Cadastro de Investidores");

        Label titulo = new Label("Gerenciamento de Investidores");

        GridPane formulario = montarFormulario();
        montarTabela();

        Button botaoNovo = new Button("Novo");
        Button botaoSalvar = new Button("Salvar");
        Button botaoAtualizar = new Button("Atualizar");
        Button botaoExcluir = new Button("Excluir");

        botaoNovo.setOnAction(e -> limparCampos());
        botaoSalvar.setOnAction(e -> salvar());
        botaoAtualizar.setOnAction(e -> atualizar());
        botaoExcluir.setOnAction(e -> excluir());

        HBox barraBotoes = new HBox();
        barraBotoes.setSpacing(10);
        barraBotoes.setPadding(new Insets(10));
        barraBotoes.getChildren().add(botaoNovo);
        barraBotoes.getChildren().add(botaoSalvar);
        barraBotoes.getChildren().add(botaoAtualizar);
        barraBotoes.getChildren().add(botaoExcluir);

        HBox centro = new HBox();
        centro.setSpacing(20);
        centro.setPadding(new Insets(10));
        centro.getChildren().add(formulario);
        centro.getChildren().add(tabela);

        BorderPane raiz = new BorderPane();
        raiz.setPadding(new Insets(15));
        raiz.setTop(titulo);
        raiz.setCenter(centro);
        raiz.setBottom(barraBotoes);

        Scene cena = new Scene(raiz, 800, 450);
        janela.setScene(cena);

        carregarDados();
        configurarSelecaoTabela();

        janela.show();
    }

    private GridPane montarFormulario() {
        campoPerfil.getItems().add("Conservador");
        campoPerfil.getItems().add("Moderado");
        campoPerfil.getItems().add("Agressivo");

        GridPane grade = new GridPane();
        grade.setHgap(10);
        grade.setVgap(10);

        grade.add(new Label("Nome:"), 0, 0);
        grade.add(campoNome, 1, 0);
        grade.add(new Label("Email:"), 0, 1);
        grade.add(campoEmail, 1, 1);
        grade.add(new Label("Perfil de Risco:"), 0, 2);
        grade.add(campoPerfil, 1, 2);
        grade.add(new Label("Saldo:"), 0, 3);
        grade.add(campoSaldo, 1, 3);
        grade.add(new Label("Data de Cadastro:"), 0, 4);
        grade.add(campoData, 1, 4);

        return grade;
    }

    private void montarTabela() {
        TableColumn<Investidor, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Investidor, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Investidor, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Investidor, String> colPerfil = new TableColumn<>("Perfil");
        colPerfil.setCellValueFactory(new PropertyValueFactory<>("perfilRisco"));

        TableColumn<Investidor, Double> colSaldo = new TableColumn<>("Saldo");
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));

        TableColumn<Investidor, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));

        tabela.getColumns().add(colId);
        tabela.getColumns().add(colNome);
        tabela.getColumns().add(colEmail);
        tabela.getColumns().add(colPerfil);
        tabela.getColumns().add(colSaldo);
        tabela.getColumns().add(colData);

        tabela.setItems(investidores);
    }

    private void configurarSelecaoTabela() {
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                preencherCampos(selecionado);
            }
        });
    }

    private void carregarDados() {
        ArrayList<Investidor> lista = InvestidorDAO.lerLista();
        investidores.setAll(lista);
    }

    private int proximoId() {
        int maior = 0;
        for (Investidor investidor : investidores) {
            if (investidor.getId() > maior) {
                maior = investidor.getId();
            }
        }
        return maior + 1;
    }

    private void salvar() {
        try {
            String nome = campoNome.getText();
            String email = campoEmail.getText();
            String perfil = campoPerfil.getValue();
            double saldo = Double.parseDouble(campoSaldo.getText());

            LocalDate data = campoData.getValue();
            String dataTexto = data.format(formatoBR);

            int id = proximoId();

            Investidor novo = new Investidor(id, nome, email, perfil, saldo, dataTexto);
            investidores.add(novo);
            InvestidorDAO.salvarLista(new ArrayList<>(investidores));

            limparCampos();
            mostrarInfo("Investidor salvo com sucesso!");
        } catch (NumberFormatException e) {
            mostrarErro("O saldo deve ser um número válido. Ex: 1500.50");
        } catch (NullPointerException e) {
            mostrarErro("Preencha todos os campos (perfil e data são obrigatórios).");
        }
    }

    private void atualizar() {
        Investidor selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarErro("Selecione um investidor na tabela para atualizar.");
            return;
        }
        try {
            selecionado.setNome(campoNome.getText());
            selecionado.setEmail(campoEmail.getText());
            selecionado.setPerfilRisco(campoPerfil.getValue());
            selecionado.setSaldo(Double.parseDouble(campoSaldo.getText()));

            LocalDate data = campoData.getValue();
            selecionado.setDataCadastro(data.format(formatoBR));

            InvestidorDAO.salvarLista(new ArrayList<>(investidores));
            tabela.refresh();

            limparCampos();
            mostrarInfo("Investidor atualizado com sucesso!");
        } catch (NumberFormatException e) {
            mostrarErro("O saldo deve ser um número válido. Ex: 1500.50");
        } catch (NullPointerException e) {
            mostrarErro("Preencha todos os campos (perfil e data são obrigatórios).");
        }
    }

    private void excluir() {
        Investidor selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarErro("Selecione um investidor na tabela para excluir.");
            return;
        }
        investidores.remove(selecionado);
        InvestidorDAO.salvarLista(new ArrayList<>(investidores));
        limparCampos();
        mostrarInfo("Investidor excluído com sucesso!");
    }

    private void preencherCampos(Investidor investidor) {
        campoNome.setText(investidor.getNome());
        campoEmail.setText(investidor.getEmail());
        campoPerfil.setValue(investidor.getPerfilRisco());
        campoSaldo.setText(String.valueOf(investidor.getSaldo()));

        LocalDate data = LocalDate.parse(investidor.getDataCadastro(), formatoBR);
        campoData.setValue(data);
    }

    private void limparCampos() {
        campoNome.clear();
        campoEmail.clear();
        campoPerfil.setValue(null);
        campoSaldo.clear();
        campoData.setValue(null);
        tabela.getSelectionModel().clearSelection();
    }

    private void mostrarErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void mostrarInfo(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Sucesso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}