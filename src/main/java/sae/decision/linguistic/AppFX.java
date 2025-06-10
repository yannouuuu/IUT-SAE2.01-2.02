package sae.decision.linguistic;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sae.decision.linguistic.controller.MainController;
import sae.decision.linguistic.controller.MainViewController;
import sae.decision.linguistic.controller.SidebarController;

public class AppFX extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL fxmlLocation = getClass().getClassLoader().getResource("sae/decision/linguistic/view/Main.fxml");
        if (fxmlLocation == null) {
            System.err.println("Le fichier Main.fxml est introuvable. Vérifiez le chemin.");
            throw new IOException("Cannot find Main.fxml");
        }
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        MainController mainController = loader.getController();
        SidebarController.setMainController(mainController);
        MainViewController.setMainController(mainController);

        Scene scene = new Scene(root, 1280, 720);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Application d'appariement linguistique");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 