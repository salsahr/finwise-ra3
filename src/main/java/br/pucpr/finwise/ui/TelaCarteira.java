package br.pucpr.finwise.ui;

import br.pucpr.finwise.modelo.Carteira;
import br.pucpr.finwise.modelo.Investidor;
import br.pucpr.finwise.persistencia.CarteiraDAO;
import br.pucpr.finwise.persistencia.InvestidorDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

public class TelaCarteira {



    private final ObservableList<Carteira> carteiras = FXCollections.observableArrayList();

    private final TextField campoNome = new TextField();
    private final ComboBox<Investidor> campoInvestidor = new ComboBox<>();
    private final TextField campoValor = new TextField();
    private final TextField campoRentabilidade = new TextField();

    private final TableView<Carteira> tabela = new TableView<>();

    public void mostrar() {
        Stage janela = new Stage();
        janela.setTitle("Finwise - Cadastro de Carteiras");

        Label titulo = new Label("Gerenciamento de Carteiras");

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

        carregarInvestidores();
        carregarDados();
        configurarSelecaoTabela();

        janela.show();
    }

    private GridPane montarFormulario() {
        campoInvestidor.setConverter(new StringConverter<Investidor>() {
            @Override
            public String toString(Investidor investidor) {
                if (investidor == null) {
                    return "";
                }
                return investidor.getNome();
            }

            @Override
            public Investidor fromString(String texto) {
                return null;
            }
        });

        GridPane grade = new GridPane();
        grade.setHgap(10);
        grade.setVgap(10);

        grade.add(new Label("Nome da Carteira:"), 0, 0);
        grade.add(campoNome, 1, 0);
        grade.add(new Label("Investidor:"), 0, 1);
        grade.add(campoInvestidor, 1, 1);
        grade.add(new Label("Valor Investido:"), 0, 2);
        grade.add(campoValor, 1, 2);
        grade.add(new Label("Rentabilidade (%):"), 0, 3);
        grade.add(campoRentabilidade, 1, 3);

        return grade;
    }

    private void montarTabela() {
        TableColumn<Carteira, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Carteira, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Carteira, Integer> colInvestidor = new TableColumn<>("ID Investidor");
        colInvestidor.setCellValueFactory(new PropertyValueFactory<>("investidorId"));

        TableColumn<Carteira, Double> colValor = new TableColumn<>("Valor Investido");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorInvestido"));

        TableColumn<Carteira, Double> colRentabilidade = new TableColumn<>("Rentabilidade");
        colRentabilidade.setCellValueFactory(new PropertyValueFactory<>("rentabilidade"));

        tabela.getColumns().add(colId);
        tabela.getColumns().add(colNome);
        tabela.getColumns().add(colInvestidor);
        tabela.getColumns().add(colValor);
        tabela.getColumns().add(colRentabilidade);

        tabela.setItems(carteiras);
    }

    private void configurarSelecaoTabela() {
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                preencherCampos(selecionado);
            }
        });
    }

    private void carregarInvestidores() {
        ArrayList<Investidor> lista = InvestidorDAO.lerLista();
        campoInvestidor.getItems().setAll(lista);
    }

    private void carregarDados() {
        ArrayList<Carteira> lista = CarteiraDAO.lerLista();
        carteiras.setAll(lista);
    }

    private int proximoId() {
        int maior = 0;
        for (Carteira carteira : carteiras) {
            if (carteira.getId() > maior) {
                maior = carteira.getId();
            }
        }
        return maior + 1;
    }

    private void salvar() {
        try {
            String nome = campoNome.getText();
            Investidor investidor = campoInvestidor.getValue();
            int investidorId = investidor.getId();
            double valor = Double.parseDouble(campoValor.getText());
            double rentabilidade = Double.parseDouble(campoRentabilidade.getText());

            int id = proximoId();

            Carteira nova = new Carteira(id, nome, investidorId, valor, rentabilidade);
            carteiras.add(nova);
            CarteiraDAO.salvarLista(new ArrayList<>(carteiras));

            limparCampos();
            mostrarInfo("Carteira salva com sucesso!");
        } catch (NumberFormatException e) {
            mostrarErro("Valor e rentabilidade devem ser números válidos. Ex: 1500.50");
        } catch (NullPointerException e) {
            mostrarErro("Selecione um investidor e preencha todos os campos.");
        }
    }

    private void atualizar() {
        Carteira selecionada = tabela.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarErro("Selecione uma carteira na tabela para atualizar.");
            return;
        }
        try {
            selecionada.setNome(campoNome.getText());
            Investidor investidor = campoInvestidor.getValue();
            selecionada.setInvestidorId(investidor.getId());
            selecionada.setValorInvestido(Double.parseDouble(campoValor.getText()));
            selecionada.setRentabilidade(Double.parseDouble(campoRentabilidade.getText()));

            CarteiraDAO.salvarLista(new ArrayList<>(carteiras));
            tabela.refresh();

            limparCampos();
            mostrarInfo("Carteira atualizada com sucesso!");
        } catch (NumberFormatException e) {
            mostrarErro("Valor e rentabilidade devem ser números válidos. Ex: 1500.50");
        } catch (NullPointerException e) {
            mostrarErro("Selecione um investidor e preencha todos os campos.");
        }
    }

    private void excluir() {
        Carteira selecionada = tabela.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarErro("Selecione uma carteira na tabela para excluir.");
            return;
        }
        carteiras.remove(selecionada);
        CarteiraDAO.salvarLista(new ArrayList<>(carteiras));
        limparCampos();
        mostrarInfo("Carteira excluída com sucesso!");
    }

    private void preencherCampos(Carteira carteira) {
        campoNome.setText(carteira.getNome());
        campoValor.setText(String.valueOf(carteira.getValorInvestido()));
        campoRentabilidade.setText(String.valueOf(carteira.getRentabilidade()));
        selecionarInvestidorPorId(carteira.getInvestidorId());
    }

    private void selecionarInvestidorPorId(int investidorId) {
        for (Investidor investidor : campoInvestidor.getItems()) {
            if (investidor.getId() == investidorId) {
                campoInvestidor.setValue(investidor);
                return;
            }
        }
        campoInvestidor.setValue(null);
    }

    private void limparCampos() {
        campoNome.clear();
        campoInvestidor.setValue(null);
        campoValor.clear();
        campoRentabilidade.clear();
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