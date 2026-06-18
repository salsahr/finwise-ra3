package br.pucpr.finwise.ui;

import br.pucpr.finwise.modelo.Ativo;
import br.pucpr.finwise.modelo.Carteira;
import br.pucpr.finwise.modelo.Transacao;
import br.pucpr.finwise.persistencia.AtivoDAO;
import br.pucpr.finwise.persistencia.CarteiraDAO;
import br.pucpr.finwise.persistencia.TransacaoDAO;

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

public class TelaTransacao {

    private final ObservableList<Transacao> transacoes =
            FXCollections.observableArrayList();

    private final ComboBox<Carteira> campoCarteira =
            new ComboBox<>();

    private final ComboBox<Ativo> campoAtivo =
            new ComboBox<>();

    private final ComboBox<String> campoTipoOperacao =
            new ComboBox<>();

    private final TextField campoValor =
            new TextField();

    private final TableView<Transacao> tabela =
            new TableView<>();

    public void mostrar() {

        Stage janela = new Stage();
        janela.setTitle("Finwise - Cadastro de Transações");

        Label titulo =
                new Label("Gerenciamento de Transações");

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

        Scene cena = new Scene(raiz, 950, 500);

        janela.setScene(cena);

        carregarCarteiras();
        carregarAtivos();
        carregarDados();
        configurarSelecaoTabela();

        janela.show();
    }

    private GridPane montarFormulario() {

        campoCarteira.setConverter(
                new StringConverter<Carteira>() {

                    @Override
                    public String toString(Carteira carteira) {

                        if (carteira == null) {
                            return "";
                        }

                        return carteira.getNome();
                    }

                    @Override
                    public Carteira fromString(String texto) {
                        return null;
                    }
                });

        campoAtivo.setConverter(
                new StringConverter<Ativo>() {

                    @Override
                    public String toString(Ativo ativo) {

                        if (ativo == null) {
                            return "";
                        }

                        return ativo.getNome();
                    }

                    @Override
                    public Ativo fromString(String texto) {
                        return null;
                    }
                });

        campoTipoOperacao.getItems().addAll(
                "Compra",
                "Venda"
        );

        GridPane grade = new GridPane();

        grade.setHgap(10);
        grade.setVgap(10);

        grade.add(new Label("Carteira:"), 0, 0);
        grade.add(campoCarteira, 1, 0);

        grade.add(new Label("Ativo:"), 0, 1);
        grade.add(campoAtivo, 1, 1);

        grade.add(new Label("Tipo Operação:"), 0, 2);
        grade.add(campoTipoOperacao, 1, 2);

        grade.add(new Label("Valor:"), 0, 3);
        grade.add(campoValor, 1, 3);

        return grade;
    }

    private void montarTabela() {

        TableColumn<Transacao, Integer> colId =
                new TableColumn<>("ID");

        colId.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        TableColumn<Transacao, Integer> colCarteira =
                new TableColumn<>("ID Carteira");

        colCarteira.setCellValueFactory(
                new PropertyValueFactory<>("carteiraId"));

        TableColumn<Transacao, Integer> colAtivo =
                new TableColumn<>("ID Ativo");

        colAtivo.setCellValueFactory(
                new PropertyValueFactory<>("ativoId"));

        TableColumn<Transacao, String> colTipo =
                new TableColumn<>("Operação");

        colTipo.setCellValueFactory(
                new PropertyValueFactory<>("tipoOperacao"));

        TableColumn<Transacao, Double> colValor =
                new TableColumn<>("Valor");

        colValor.setCellValueFactory(
                new PropertyValueFactory<>("valor"));

        tabela.getColumns().add(colId);
        tabela.getColumns().add(colCarteira);
        tabela.getColumns().add(colAtivo);
        tabela.getColumns().add(colTipo);
        tabela.getColumns().add(colValor);

        tabela.setItems(transacoes);
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

    private void carregarCarteiras() {

        ArrayList<Carteira> lista =
                CarteiraDAO.lerLista();

        campoCarteira.getItems().setAll(lista);
    }

    private void carregarAtivos() {

        ArrayList<Ativo> lista =
                AtivoDAO.lerLista();

        campoAtivo.getItems().setAll(lista);
    }

    private void carregarDados() {

        ArrayList<Transacao> lista =
                TransacaoDAO.lerLista();

        transacoes.setAll(lista);
    }

    private int proximoId() {

        int maior = 0;

        for (Transacao transacao : transacoes) {

            if (transacao.getId() > maior) {
                maior = transacao.getId();
            }
        }

        return maior + 1;
    }

    private void salvar() {

        try {

            Carteira carteira =
                    campoCarteira.getValue();

            Ativo ativo =
                    campoAtivo.getValue();

            String tipo =
                    campoTipoOperacao.getValue();

            double valor =
                    Double.parseDouble(
                            campoValor.getText());

            int id = proximoId();

            Transacao nova =
                    new Transacao(
                            id,
                            carteira.getId(),
                            ativo.getId(),
                            tipo,
                            valor
                    );

            transacoes.add(nova);

            TransacaoDAO.salvarLista(
                    new ArrayList<>(transacoes));

            limparCampos();

            mostrarInfo(
                    "Transação salva com sucesso!"
            );

        } catch (NumberFormatException e) {

            mostrarErro(
                    "Valor deve ser numérico."
            );

        } catch (NullPointerException e) {

            mostrarErro(
                    "Preencha todos os campos."
            );
        }
    }

    private void atualizar() {

        Transacao selecionada =
                tabela.getSelectionModel()
                        .getSelectedItem();

        if (selecionada == null) {

            mostrarErro(
                    "Selecione uma transação para atualizar."
            );

            return;
        }

        try {

            Carteira carteira =
                    campoCarteira.getValue();

            Ativo ativo =
                    campoAtivo.getValue();

            selecionada.setCarteiraId(
                    carteira.getId());

            selecionada.setAtivoId(
                    ativo.getId());

            selecionada.setTipoOperacao(
                    campoTipoOperacao.getValue());

            selecionada.setValor(
                    Double.parseDouble(
                            campoValor.getText()));

            TransacaoDAO.salvarLista(
                    new ArrayList<>(transacoes));

            tabela.refresh();

            limparCampos();

            mostrarInfo(
                    "Transação atualizada com sucesso!"
            );

        } catch (NumberFormatException e) {

            mostrarErro(
                    "Valor deve ser numérico."
            );

        } catch (NullPointerException e) {

            mostrarErro(
                    "Preencha todos os campos."
            );
        }
    }

    private void excluir() {

        Transacao selecionada =
                tabela.getSelectionModel()
                        .getSelectedItem();

        if (selecionada == null) {

            mostrarErro(
                    "Selecione uma transação para excluir."
            );

            return;
        }

        transacoes.remove(selecionada);

        TransacaoDAO.salvarLista(
                new ArrayList<>(transacoes));

        limparCampos();

        mostrarInfo(
                "Transação excluída com sucesso!"
        );
    }

    private void preencherCampos(
            Transacao transacao) {

        selecionarCarteiraPorId(
                transacao.getCarteiraId());

        selecionarAtivoPorId(
                transacao.getAtivoId());

        campoTipoOperacao.setValue(
                transacao.getTipoOperacao());

        campoValor.setText(
                String.valueOf(
                        transacao.getValor()));
    }

    private void selecionarCarteiraPorId(
            int carteiraId) {

        for (Carteira carteira :
                campoCarteira.getItems()) {

            if (carteira.getId()
                    == carteiraId) {

                campoCarteira.setValue(
                        carteira);

                return;
            }
        }

        campoCarteira.setValue(null);
    }

    private void selecionarAtivoPorId(
            int ativoId) {

        for (Ativo ativo :
                campoAtivo.getItems()) {

            if (ativo.getId()
                    == ativoId) {

                campoAtivo.setValue(
                        ativo);

                return;
            }
        }

        campoAtivo.setValue(null);
    }

    private void limparCampos() {

        campoCarteira.setValue(null);
        campoAtivo.setValue(null);
        campoTipoOperacao.setValue(null);
        campoValor.clear();

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