package sae.decision.linguistic.controller;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML
    private StackPane contentPane;

    @FXML
    public void initialize() {
        // Charge la vue par défaut au démarrage
        loadView("StudentManager.fxml");
    }

    /**
     * Charge une vue FXML dans le contentPane central.
     * @param fxmlFile Le nom du fichier FXML à charger (ex: "MainView.fxml")
     */
    public void loadView(String fxmlFile) {
        try {
            // Le chemin vers les vues est relatif au répertoire 'view'
            URL url = getClass().getResource("/sae/decision/linguistic/view/" + fxmlFile);
            if (url == null) {
                System.err.println("Cannot find FXML file: " + fxmlFile);
                return;
            }
            Node view = FXMLLoader.load(url);
            contentPane.getChildren().setAll(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 