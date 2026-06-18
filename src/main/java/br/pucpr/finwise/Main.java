package br.pucpr.finwise;

import br.pucpr.finwise.ui.TelaInvestidor;
import br.pucpr.finwise.ui.TelaCarteira;
import br.pucpr.finwise.ui.TelaCurso;
import br.pucpr.finwise.ui.TelaMatricula;
import br.pucpr.finwise.ui.TelaAtivo;
import br.pucpr.finwise.ui.TelaTransacao;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage janelaPrincipal) {
        Label titulo = new Label("Finwise - Menu Principal");

        Button botaoInvestidor = new Button("Gerenciar Investidores");
        Button botaoCarteira = new Button("Gerenciar Carteiras");
        Button botaoCurso = new Button("Gerenciar Cursos");
        Button botaoMatricula = new Button("Gerenciar Matrículas");
        Button botaoAtivo = new Button("Gerenciar Ativos");
        Button botaoTransacao = new Button("Gerenciar Transações");

        VBox layoutPrincipal = new VBox();
        layoutPrincipal.setSpacing(15);
        layoutPrincipal.setPadding(new Insets(30));
        layoutPrincipal.setAlignment(Pos.CENTER);

        layoutPrincipal.getChildren().add(titulo);
        layoutPrincipal.getChildren().add(botaoInvestidor);
        layoutPrincipal.getChildren().add(botaoCarteira);
        layoutPrincipal.getChildren().add(botaoCurso);
        layoutPrincipal.getChildren().add(botaoMatricula);
        layoutPrincipal.getChildren().add(botaoAtivo);
        layoutPrincipal.getChildren().add(botaoTransacao);

        Scene cena = new Scene(layoutPrincipal, 400, 350);

        janelaPrincipal.setTitle("Finwise");
        janelaPrincipal.setScene(cena);
        janelaPrincipal.show();

        botaoInvestidor.setOnAction(e -> {
            TelaInvestidor tela = new TelaInvestidor();
            tela.mostrar();
        });

        botaoCarteira.setOnAction(e -> {
            TelaCarteira tela = new TelaCarteira();
            tela.mostrar();
        });

        botaoCurso.setOnAction(e -> {
            TelaCurso tela = new TelaCurso();
            tela.mostrar();
        });

        botaoMatricula.setOnAction(e -> {
            TelaMatricula tela = new TelaMatricula();
            tela.mostrar();
        });

        botaoAtivo.setOnAction(e -> {
            TelaAtivo tela = new TelaAtivo();
            tela.mostrar();
        });

        botaoTransacao.setOnAction(e -> {
            TelaTransacao tela = new TelaTransacao();
            tela.mostrar();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}