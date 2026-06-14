package br.pucpr.finwise.ui;

import br.pucpr.finwise.modelo.Curso;
import br.pucpr.finwise.modelo.Matricula;
import br.pucpr.finwise.persistencia.CursoDAO;
import br.pucpr.finwise.persistencia.MatriculaDAO;

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
import javafx.util.StringConverter;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TelaMatricula {

    private final MatriculaDAO dao = new MatriculaDAO();
    private final CursoDAO cursoDao = new CursoDAO();

    private final ObservableList<Matricula> matriculas = FXCollections.observableArrayList();

    private final TextField campoNomeAluno = new TextField();
    private final ComboBox<Curso> campoCurso = new ComboBox<>();

    private final TableView<Matricula> tabela = new TableView<>();

    public void mostrar() {
        Stage janela = new Stage();
        janela.setTitle("Finwise - Gestão de Matrículas");

        Label titulo = new Label("Cadastro de Matrículas");

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

        HBox barraBotoes = new HBox(10, botaoVoltar, botaoNovo, botaoSalvar, botaoAtualizar, botaoExcluir);
        barraBotoes.setPadding(new Insets(10));

        HBox centro = new HBox(20, formulario, tabela);
        centro.setPadding(new Insets(10));

        BorderPane raiz = new BorderPane();
        raiz.setPadding(new Insets(15));
        raiz.setTop(titulo);
        raiz.setCenter(centro);
        raiz.setBottom(barraBotoes);

        Scene cena = new Scene(raiz, 800, 450);
        janela.setScene(cena);

        carregarCursos();
        carregarDados();
        configurarSelecaoTabela();

        janela.show();
    }

    private GridPane montarFormulario() {
        campoCurso.setConverter(new StringConverter<Curso>() {
            @Override
            public String toString(Curso curso) {
                if (curso == null) {
                    return "";
                }
                return curso.getNome() + " (" + curso.getCargaHoraria() + "h)";
            }

            @Override
            public Curso fromString(String texto) {
                return null;
            }
        });

        GridPane grade = new GridPane();
        grade.setHgap(10);
        grade.setVgap(10);

        grade.add(new Label("Nome do Aluno:"), 0, 0);
        grade.add(campoNomeAluno, 1, 0);

        grade.add(new Label("Curso:"), 0, 1);
        grade.add(campoCurso, 1, 1);

        return grade;
    }

    private void montarTabela() {
        TableColumn<Matricula, String> colAluno = new TableColumn<>("Aluno");
        colAluno.setCellValueFactory(new PropertyValueFactory<>("nomeAluno"));

        TableColumn<Matricula, Curso> colCurso = new TableColumn<>("Curso");
        colCurso.setCellValueFactory(new PropertyValueFactory<>("curso"));

        TableColumn<Matricula, LocalDate> colData = new TableColumn<>("Data Matrícula");
        colData.setCellValueFactory(new PropertyValueFactory<>("dataMatricula"));

        tabela.getColumns().addAll(colAluno, colCurso, colData);
        tabela.setItems(matriculas);
    }

    private void configurarSelecaoTabela() {
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                preencherCampos(selecionado);
            }
        });
    }
    private void carregarCursos() {
        List<Curso> lista = cursoDao.lerLista();
        campoCurso.getItems().setAll(lista);
    }

    private void carregarDados() {
        ArrayList<Matricula> lista = MatriculaDAO.lerLista();
        matriculas.setAll(lista);
    }

    private void salvar() {
        try {
            String nomeAluno = campoNomeAluno.getText();
            Curso cursoSelecionado = campoCurso.getValue();

            if (nomeAluno.isEmpty() || cursoSelecionado == null) {
                mostrarErro("Preencha o nome do aluno e selecione um curso.");
                return;
            }

            Matricula nova = new Matricula(nomeAluno, cursoSelecionado);

            matriculas.add(nova);
            MatriculaDAO.salvarLista(new ArrayList<>(matriculas));

            limparCampos();
            mostrarInfo("Matrícula salva com sucesso!");
        } catch (Exception e) {
            mostrarErro("Erro ao salvar a matrícula.");
        }
    }

    private void atualizar() {
        Matricula selecionada = tabela.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarErro("Selecione uma matrícula na tabela para atualizar.");
            return;
        }

        String nomeAluno = campoNomeAluno.getText();
        Curso cursoSelecionado = campoCurso.getValue();

        if (nomeAluno.isEmpty() || cursoSelecionado == null) {
            mostrarErro("Preencha o nome do aluno e selecione um curso.");
            return;
        }

        selecionada.setNomeAluno(nomeAluno);
        selecionada.setCurso(cursoSelecionado);

        MatriculaDAO.salvarLista(new ArrayList<>(matriculas));
        tabela.refresh();

        limparCampos();
        mostrarInfo("Matrícula atualizada com sucesso!");
    }

    private void excluir() {
        Matricula selecionada = tabela.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarErro("Selecione uma matrícula na tabela para excluir.");
            return;
        }
        matriculas.remove(selecionada);
        MatriculaDAO.salvarLista(new ArrayList<>(matriculas));
        limparCampos();
        mostrarInfo("Matrícula excluída com sucesso!");
    }

    private void preencherCampos(Matricula matricula) {
        campoNomeAluno.setText(matricula.getNomeAluno());
        selecionarCursoPorId(matricula.getCurso().getId());
    }

    private void selecionarCursoPorId(int cursoId) {
        for (Curso curso : campoCurso.getItems()) {
            if (curso.getId() == cursoId) {
                campoCurso.setValue(curso);
                return;
            }
        }
        campoCurso.setValue(null);
    }

    private void limparCampos() {
        campoNomeAluno.clear();
        campoCurso.setValue(null);
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