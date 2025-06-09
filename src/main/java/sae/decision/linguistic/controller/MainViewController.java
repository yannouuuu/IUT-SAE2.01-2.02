package sae.decision.linguistic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainViewController {

    // Boutons du header
    @FXML
    private Button chargerCSVButton;
    
    @FXML
    private Button sauvegarderButton;
    
    @FXML
    private Button reglagesHeaderButton;

    // ComboBoxes de sélection des pays
    @FXML
    private ComboBox<String> comboPaysA;

    @FXML
    private ComboBox<String> comboPaysB;
    
    @FXML
    private Button validerButton;

    // Table et colonnes
    @FXML
    private TableView<?> tableAppariements;

    @FXML
    private TableColumn<?, ?> colStatut;

    @FXML
    private TableColumn<?, ?> colHote;

    @FXML
    private TableColumn<?, ?> colVisiteur;

    @FXML
    private TableColumn<?, ?> colScore;
    
    @FXML
    private Label statsLabel;

    // Boutons du bas
    @FXML
    private Button voirDetailsButton;
    
    @FXML
    private Button gestionAppariementsButton;
    
    @FXML
    private Button gestionElevesButton;
    
    @FXML
    private Label derniereMajLabel;
    
    @FXML
    public void initialize() {
        System.out.println("MainViewController initialisé.");
        
        // Application du style moderne à tous les boutons et composants
        setupModernStyles();
        
        // Configuration des ComboBoxes
        setupComboBoxes();
        
        // Configuration de la table
        setupTable();
    }
    
    private void setupModernStyles() {
        // Boutons principaux (noirs)
        setupBlackButton(validerButton);
        setupBlackButton(voirDetailsButton);
        
        // Boutons secondaires (blancs)
        setupSecondaryButton(chargerCSVButton);
        setupSecondaryButton(sauvegarderButton);
        setupSecondaryButton(reglagesHeaderButton);
        setupSecondaryButton(gestionAppariementsButton);
        setupSecondaryButton(gestionElevesButton);
        
        // ComboBoxes modernes
        setupModernComboBox(comboPaysA);
        setupModernComboBox(comboPaysB);
    }
    
    private void setupBlackButton(Button button) {
        String baseStyle = 
            "-fx-background-color: #000000; " +
            "-fx-text-fill: #ffffff; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 700; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-radius: 12px; " +
            "-fx-border-color: transparent; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 12px 20px;";
        
        button.setStyle(baseStyle);
        
        // Effets hover
        button.setOnMouseEntered(e -> {
            button.setStyle(baseStyle + 
                "-fx-background-color: #333333; " +
                "-fx-scale-x: 1.02; " +
                "-fx-scale-y: 1.02;");
        });
        
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
        
        // Effet pressed
        button.setOnMousePressed(e -> {
            button.setStyle(baseStyle + 
                "-fx-scale-x: 0.98; " +
                "-fx-scale-y: 0.98; " +
                "-fx-background-color: #1a1a1a;");
        });
        
        button.setOnMouseReleased(e -> button.setStyle(baseStyle));
    }
    
    private void setupSecondaryButton(Button button) {
        String baseStyle = 
            "-fx-background-color: #e9ecef; " +
            "-fx-text-fill: #6c6c6c; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 700; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-radius: 12px; " +
            "-fx-border-color: transparent; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 12px 20px;";
        
        button.setStyle(baseStyle);
        
        // Effets hover
        button.setOnMouseEntered(e -> {
            button.setStyle(baseStyle + 
                "-fx-background-color: #dee2e6; " +
                "-fx-text-fill: #495057; " +
                "-fx-scale-x: 1.02; " +
                "-fx-scale-y: 1.02;");
        });
        
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
        
        // Effet pressed
        button.setOnMousePressed(e -> {
            button.setStyle(baseStyle + 
                "-fx-scale-x: 0.98; " +
                "-fx-scale-y: 0.98; " +
                "-fx-background-color: #ced4da;");
        });
        
        button.setOnMouseReleased(e -> button.setStyle(baseStyle));
    }
    
    private void setupModernComboBox(ComboBox<String> comboBox) {
        String baseStyle = 
            "-fx-background-color: #ffffff; " +
            "-fx-border-color: #e9ecef; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 8px; " +
            "-fx-background-radius: 8px; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 600; " +
            "-fx-text-fill: #495057; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 8px 12px;";
        
        comboBox.setStyle(baseStyle);
        
        // Effets hover et focus
        comboBox.setOnMouseEntered(e -> {
            comboBox.setStyle(baseStyle + 
                "-fx-border-color: #dee2e6; " +
                "-fx-background-color: #f8f9fa;");
        });
        
        comboBox.setOnMouseExited(e -> {
            if (!comboBox.isFocused()) {
                comboBox.setStyle(baseStyle);
            }
        });
        
        comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                comboBox.setStyle(baseStyle + 
                    "-fx-border-color: #000000; " +
                    "-fx-border-width: 2px;");
            } else {
                comboBox.setStyle(baseStyle);
            }
        });
    }
    
    private void setupComboBoxes() {
        // Configuration par défaut si nécessaire
        comboPaysA.getSelectionModel().selectFirst();
        comboPaysB.getSelectionModel().selectLast();
    }
    
    private void setupTable() {
        // Style moderne pour la table
        String tableStyle = 
            "-fx-background-color: transparent; " +
            "-fx-table-cell-border-color: #e9ecef; " +
            "-fx-background-radius: 8px;";
        
        tableAppariements.setStyle(tableStyle);
        
        // Configuration des colonnes si nécessaire
        if (statsLabel != null) {
            statsLabel.setText("Aucun appariement généré");
        }
    }

    
}
