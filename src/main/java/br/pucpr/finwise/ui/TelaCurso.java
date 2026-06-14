package br.pucpr.finwise.ui;

import br.pucpr.finwise.modelo.Curso;
import br.pucpr.finwise.persistencia.CursoDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class TelaCurso {

    private final ObservableList<Curso> cursos = FXCollections.observableArrayList();
    private final TextField campoNome = new TextField();
    private final TextField campoCargaHoraria = new TextField();
    private final TableView<Curso> tabela = new TableView<>();

    public void mostrar() {
        Stage janela = new Stage();
        janela.setTitle("Gestão de Cursos");

        Label titulo = new Label("Cadastro de Cursos");

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

        HBox barraBotoes = new HBox(10, botaoNovo, botaoSalvar, botaoAtualizar, botaoExcluir);
        barraBotoes.setPadding(new Insets(10));

        HBox centro = new HBox(20, formulario, tabela);
        centro.setPadding(new Insets(10));

        BorderPane raiz = new BorderPane();
        raiz.setPadding(new Insets(15));
        raiz.setTop(titulo);
        raiz.setCenter(centro);
        raiz.setBottom(barraBotoes);

        Scene cena = new Scene(raiz, 650, 400);
        janela.setScene(cena);

        carregarDados();
        configurarSelecaoTabela();

        janela.show();
    }

    private GridPane montarFormulario() {
        GridPane grade = new GridPane();
        grade.setHgap(10);
        grade.setVgap(10);

        grade.add(new Label("Nome do Curso:"), 0, 0);
        grade.add(campoNome, 1, 0);

        grade.add(new Label("Carga Horária (h):"), 0, 1);
        grade.add(campoCargaHoraria, 1, 1);

        return grade;
    }

    private void montarTabela() {
        TableColumn<Curso, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Curso, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Curso, Integer> colCarga = new TableColumn<>("Carga Horária");
        colCarga.setCellValueFactory(new PropertyValueFactory<>("cargaHoraria"));

        tabela.getColumns().addAll(colId, colNome, colCarga);
        tabela.setItems(cursos);
    }

    private void configurarSelecaoTabela() {
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                preencherCampos(selecionado);
            }
        });
    }

    private void carregarDados() {
        ArrayList<Curso> lista = CursoDAO.lerLista();
        cursos.setAll(lista);
    }

    private int proximoId() {
        int maior = 0;
        for (Curso c : cursos) {
            if (c.getId() > maior) {
                maior = c.getId();
            }
        }
        return maior + 1;
    }

    private void salvar() {
        try {
            String nome = campoNome.getText();
            int carga = Integer.parseInt(campoCargaHoraria.getText());

            Curso novo = new Curso(proximoId(), nome, carga);
            cursos.add(novo);
            CursoDAO.salvarLista(new ArrayList<>(cursos));

            limparCampos();
            mostrarInfo("Curso salvo com sucesso!");
        } catch (NumberFormatException e) {
            mostrarErro("A carga horária deve ser um número inteiro.");
        }
    }

    private void atualizar() {
        Curso selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarErro("Selecione um curso na tabela para atualizar.");
            return;
        }
        try {
            selecionado.setNome(campoNome.getText());
            selecionado.setCargaHoraria(Integer.parseInt(campoCargaHoraria.getText()));

            CursoDAO.salvarLista(new ArrayList<>(cursos));
            tabela.refresh();

            limparCampos();
            mostrarInfo("Curso atualizado com sucesso!");
        } catch (NumberFormatException e) {
            mostrarErro("A carga horária deve ser um número inteiro.");
        }
    }

    private void excluir() {
        Curso selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarErro("Selecione um curso na tabela para excluir.");
            return;
        }
        cursos.remove(selecionado);
        CursoDAO.salvarLista(new ArrayList<>(cursos));
        limparCampos();
        mostrarInfo("Curso excluído com sucesso!");
    }

    private void preencherCampos(Curso curso) {
        campoNome.setText(curso.getNome());
        campoCargaHoraria.setText(String.valueOf(curso.getCargaHoraria()));
    }

    private void limparCampos() {
        campoNome.clear();
        campoCargaHoraria.clear();
        tabela.getSelectionModel().clearSelection();
    }

    private void mostrarErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR, mensagem);
        alerta.showAndWait();
    }

    private void mostrarInfo(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION, mensagem);
        alerta.showAndWait();
    }
}