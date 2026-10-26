package fr.univlille.iut.s302.view;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe principale de vue
 * @author Lucas AUER / Issa LE GALL
 */
public class MainApp extends Application {

    /**
     *Démarre l'application et affiche la fenêtre principale
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Graphe de dispersion");

        ViewManager viewManager = new ViewManager();
        viewManager.addView(primaryStage);

        primaryStage.show();
    }

    /**
     * lance l'application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}