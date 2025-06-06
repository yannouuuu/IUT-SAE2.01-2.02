package sae.decision.linguistic.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SidebarController {

    private static MainController mainController;

    public static void setMainController(MainController controller) {
        mainController = controller;
    }

    @FXML
    private void goToDashboard(ActionEvent event) {
        System.out.println("Naviguer vers Dashboard (vue non implémentée)");
        // if (mainController != null) mainController.loadView("Dashboard.fxml");
    }

    @FXML
    private void goToMainInterface(ActionEvent event) {
        if (mainController != null) {
            mainController.loadView("MainView.fxml");
        }
    }

    @FXML
    private void goToListeAppariements(ActionEvent event) {
        if (mainController != null) {
            mainController.loadView("Pairing.fxml");
        }
    }

    @FXML
    private void goToAppariementManuel(ActionEvent event) {
        if (mainController != null) {
            mainController.loadView("PairingManual.fxml");
        }
    }

    @FXML
    private void goToGestionEleves(ActionEvent event) {
        if (mainController != null) {
            mainController.loadView("StudentManager.fxml");
        }
    }

    @FXML
    private void goToReglages(ActionEvent event) {
        if (mainController != null) {
            mainController.loadView("Settings.fxml");
        }
    }
}