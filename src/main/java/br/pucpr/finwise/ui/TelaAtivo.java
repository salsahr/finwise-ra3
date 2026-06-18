package br.pucpr.finwise.ui;

import br.pucpr.finwise.modelo.Ativo;
import br.pucpr.finwise.persistencia.AtivoDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class TelaAtivo {

    private final ObservableList<Ativo> ativos =
            FXCollections.observableArrayList();

    private final TableView<Ativo> tabela =
            new TableView<>();

    private final TextField campoNome =
            new TextField();

    private final TextField campoTicker =
            new TextField();

    private final TextField campoTipo =
            new TextField();

    public void mostrar() {

        Stage janela = new Stage();
        janela.setTitle("Finwise - Cadastro de Ativos");

        Label titulo =
                new Label("Gerenciamento de Ativos");

        GridPane formulario = montarFormulario();

        montarTabela();

        Button botaoNovo = new Button("Novo");
        Button botaoSalvar = new Button("Salvar");
        Button botaoAtualizar = new Button("Atualizar");
        Button botaoExcluir = new Button("Excluir");
        Button botaoVoltar = new Button("Voltar");

        botaoNovo.setOnAction(e -> limparCampos());
        botaoSalvar.setOnAction(e -> salvar());
        botaoAtualizar.setOnAction(e -> atualizar());
        botaoExcluir.setOnAction(e -> excluir());
        botaoVoltar.setOnAction(e -> janela.close());

        HBox barraBotoes = new HBox();
        barraBotoes.setSpacing(10);
        barraBotoes.setPadding(new Insets(10));

        barraBotoes.getChildren().add(botaoVoltar);
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

        GridPane grade = new GridPane();

        grade.setHgap(10);
        grade.setVgap(10);

        grade.add(new Label("Nome:"), 0, 0);
        grade.add(campoNome, 1, 0);

        grade.add(new Label("Ticker:"), 0, 1);
        grade.add(campoTicker, 1, 1);

        grade.add(new Label("Tipo:"), 0, 2);
        grade.add(campoTipo, 1, 2);

        return grade;
    }

    private void montarTabela() {

        TableColumn<Ativo, Integer> colunaId =
                new TableColumn<>("ID");

        colunaId.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        TableColumn<Ativo, String> colunaNome =
                new TableColumn<>("Nome");

        colunaNome.setCellValueFactory(
                new PropertyValueFactory<>("nome"));

        TableColumn<Ativo, String> colunaTicker =
                new TableColumn<>("Ticker");

        colunaTicker.setCellValueFactory(
                new PropertyValueFactory<>("ticker"));

        TableColumn<Ativo, String> colunaTipo =
                new TableColumn<>("Tipo");

        colunaTipo.setCellValueFactory(
                new PropertyValueFactory<>("tipo"));

        tabela.getColumns().clear();

        tabela.getColumns().add(colunaId);
        tabela.getColumns().add(colunaNome);
        tabela.getColumns().add(colunaTicker);
        tabela.getColumns().add(colunaTipo);

        tabela.setItems(ativos);

        tabela.setPrefWidth(450);
    }

    private void configurarSelecaoTabela() {

        tabela.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, antigo, selecionado) -> {

                    if (selecionado != null) {
                        preencherCampos(selecionado);
                    }
                });
    }

    private void carregarDados() {

        ArrayList<Ativo> lista =
                AtivoDAO.lerLista();

        ativos.setAll(lista);
    }

    private int proximoId() {

        int maior = 0;

        for (Ativo ativo : ativos) {

            if (ativo.getId() > maior) {
                maior = ativo.getId();
            }
        }

        return maior + 1;
    }

    private void salvar() {

        try {

            String nome = campoNome.getText().trim();
            String ticker = campoTicker.getText().trim();
            String tipo = campoTipo.getText().trim();

            if (nome.isEmpty()
                    || ticker.isEmpty()
                    || tipo.isEmpty()) {

                mostrarErro(
                        "Preencha todos os campos."
                );

                return;
            }

            Ativo ativo = new Ativo(
                    proximoId(),
                    nome,
                    ticker,
                    tipo
            );

            ativos.add(ativo);

            AtivoDAO.salvarLista(
                    new ArrayList<>(ativos));

            limparCampos();

            mostrarInfo(
                    "Ativo salvo com sucesso!"
            );

        } catch (Exception e) {

            mostrarErro(
                    "Erro ao salvar ativo."
            );
        }
    }

    private void atualizar() {

        Ativo selecionado =
                tabela.getSelectionModel()
                        .getSelectedItem();

        if (selecionado == null) {

            mostrarErro(
                    "Selecione um ativo para atualizar."
            );

            return;
        }

        try {

            selecionado.setNome(
                    campoNome.getText());

            selecionado.setTicker(
                    campoTicker.getText());

            selecionado.setTipo(
                    campoTipo.getText());

            AtivoDAO.salvarLista(
                    new ArrayList<>(ativos));

            tabela.refresh();

            limparCampos();

            mostrarInfo(
                    "Ativo atualizado com sucesso!"
            );

        } catch (Exception e) {

            mostrarErro(
                    "Erro ao atualizar ativo."
            );
        }
    }

    private void excluir() {

        Ativo selecionado =
                tabela.getSelectionModel()
                        .getSelectedItem();

        if (selecionado == null) {

            mostrarErro(
                    "Selecione um ativo para excluir."
            );

            return;
        }

        ativos.remove(selecionado);

        AtivoDAO.salvarLista(
                new ArrayList<>(ativos));

        limparCampos();

        mostrarInfo(
                "Ativo excluído com sucesso!"
        );
    }

    private void preencherCampos(Ativo ativo) {

        campoNome.setText(
                ativo.getNome());

        campoTicker.setText(
                ativo.getTicker());

        campoTipo.setText(
                ativo.getTipo());
    }

    private void limparCampos() {

        campoNome.clear();
        campoTicker.clear();
        campoTipo.clear();

        tabela.getSelectionModel()
                .clearSelection();
    }

    private void mostrarErro(String mensagem) {

        Alert alerta =
                new Alert(Alert.AlertType.ERROR);

        alerta.setTitle("Erro");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);

        alerta.showAndWait();
    }

    private void mostrarInfo(String mensagem) {

        Alert alerta =
                new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle("Sucesso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);

        alerta.showAndWait();
    }
}