package sae.decision.linguistic.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SidebarController {

    private static MainController mainController;
    private static SidebarController instance;
    
    @FXML
    private Label mainTitle;
    
    @FXML
    private Label subtitle;
    
    @FXML
    private Button accueilButton;
    
    @FXML
    private Button listeAppariementsButton;
    
    @FXML
    private Button appariementManuelButton;
    
    @FXML
    private Button gestionElevesButton;
    
    @FXML
    private Button reglagesButton;

    public static void setMainController(MainController controller) {
        mainController = controller;
    }
    
    public static SidebarController getInstance() {
        return instance;
    }
    
    @FXML
    public void initialize() {
        instance = this;
        System.out.println("SidebarController initialisé.");
        
        // Application du style moderne à tous les boutons
        setupModernSidebarButtons();
        
        // Activer le bouton Accueil par défaut
        setActiveButton("MainView.fxml");
    }
    
    private void setupModernSidebarButtons() {
        // Style pour les boutons de la sidebar
        setupSidebarButton(accueilButton);
        setupSidebarButton(listeAppariementsButton);
        setupSidebarButton(appariementManuelButton);
        setupSidebarButton(gestionElevesButton);
        setupSidebarButton(reglagesButton);
    }
    
    private void setupSidebarButton(Button button) {
        String baseStyle = 
            "-fx-background-color: #ffffff; " +
            "-fx-text-fill: #495057; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 600; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-radius: 12px; " +
            "-fx-border-color: #e9ecef; " +
            "-fx-border-width: 1.5px; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 12px 16px; " +
            "-fx-alignment: CENTER_LEFT; " +
            "-fx-text-alignment: LEFT;";
        
        button.setStyle(baseStyle);
        
        // Effets hover - seulement si ce n'est pas le bouton actif
        button.setOnMouseEntered(e -> {
            if (button != activeButton) {
                button.setStyle(baseStyle + 
                    "-fx-background-color: #f8f9fa; " +
                    "-fx-border-color: #dee2e6; " +
                    "-fx-scale-x: 1.01; " +
                    "-fx-scale-y: 1.01;");
            }
        });
        
        button.setOnMouseExited(e -> {
            if (button != activeButton) {
                button.setStyle(baseStyle);
            }
        });
        
        // Pas d'effet pressed car on gère l'état actif directement
        button.setOnMousePressed(e -> {
            // Ne rien faire - l'état sera géré par le click
        });
        
        button.setOnMouseReleased(e -> {
            // Ne rien faire - l'état sera géré par le click
        });
    }

    private Button activeButton = null;
    
    public void setActiveButton(String viewName) {
        // Reset le bouton actif
        activeButton = null;
        
        // Reset tous les boutons
        resetAllButtons();
        
        // Activer le bouton correspondant
        Button buttonToActivate = null;
        switch (viewName) {
            case "MainView.fxml":
                buttonToActivate = accueilButton;
                break;
            case "Pairing.fxml":
                buttonToActivate = listeAppariementsButton;
                break;
            case "PairingManual.fxml":
                buttonToActivate = appariementManuelButton;
                break;
            case "StudentManager.fxml":
                buttonToActivate = gestionElevesButton;
                break;
            case "Settings.fxml":
                buttonToActivate = reglagesButton;
                break;
        }
        
        if (buttonToActivate != null) {
            activeButton = buttonToActivate;
            activateButton(buttonToActivate);
        }
    }
    
    private void resetAllButtons() {
        setupSidebarButton(accueilButton);
        setupSidebarButton(listeAppariementsButton);
        setupSidebarButton(appariementManuelButton);
        setupSidebarButton(gestionElevesButton);
        setupSidebarButton(reglagesButton);
    }
    
    private void activateButton(Button button) {
        String activeStyle = 
            "-fx-background-color: #000000; " +
            "-fx-text-fill: #ffffff; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 700; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-radius: 12px; " +
            "-fx-border-color: transparent; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 12px 16px; " +
            "-fx-alignment: CENTER_LEFT; " +
            "-fx-text-alignment: LEFT;";
        
        button.setStyle(activeStyle);
        
        // Override les événements hover pour le bouton actif
        button.setOnMouseEntered(e -> {
            // Hover plus subtil pour le bouton actif
            button.setStyle(activeStyle + 
                "-fx-background-color: #1a1a1a; " +
                "-fx-scale-x: 1.005; " +
                "-fx-scale-y: 1.005;");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(activeStyle);
        });
        
        // Pas d'effet pressed pour le bouton actif
        button.setOnMousePressed(e -> {
            // Ne rien faire
        });
        
        button.setOnMouseReleased(e -> {
            // Ne rien faire
        });
    }

    @FXML
    public void goToMainInterface(ActionEvent event) {
        if (mainController != null) {
            mainController.loadView("MainView.fxml");
            setActiveButton("MainView.fxml");
        }
    }

    @FXML
    public void goToListeAppariements(ActionEvent event) {
        if (mainController != null) {
            mainController.loadView("Pairing.fxml");
            setActiveButton("Pairing.fxml");
        }
    }

    @FXML
    public void goToAppariementManuel(ActionEvent event) {
        if (mainController != null) {
            mainController.loadView("PairingManual.fxml");
            setActiveButton("PairingManual.fxml");
        }
    }

    @FXML
    public void goToGestionEleves(ActionEvent event) {
        if (mainController != null) {
            mainController.loadView("StudentManager.fxml");
            setActiveButton("StudentManager.fxml");
        }
    }

    @FXML
    public void goToReglages(ActionEvent event) {
        if (mainController != null) {
            mainController.loadView("Settings.fxml");
            setActiveButton("Settings.fxml");
        }
    }
}