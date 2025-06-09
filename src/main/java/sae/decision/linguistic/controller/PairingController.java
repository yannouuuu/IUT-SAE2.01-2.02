package sae.decision.linguistic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PairingController {

    // Boutons du header
    @FXML
    private Button lancerCalculButton;
    
    @FXML
    private Button appariementManuelButton;
    
    @FXML
    private Button exportButton;

    // Labels de statistiques
    @FXML
    private Label totalAppariementsLabel;

    @FXML
    private Label validesLabel;

    @FXML
    private Label enAttenteLabel;

    // Filtres et recherche
    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<?> statusFilter;
    
    @FXML
    private Button resetFiltersButton;

    // Table et colonnes
    @FXML
    private TableView<?> appariementsTable;

    @FXML
    private TableColumn<?, ?> hoteColumn;

    @FXML
    private TableColumn<?, ?> visiteurColumn;

    @FXML
    private TableColumn<?, ?> scoreColumn;

    @FXML
    private TableColumn<?, ?> statutColumn;

    @FXML
    private TableColumn<?, ?> dateCreationColumn;

    @FXML
    private TableColumn<?, ?> actionsColumn;
    
    @FXML
    private Label tableStatsLabel;

    @FXML
    public void initialize() {
        System.out.println("PairingController a été initialisé et est prêt.");
        
        // Application du style moderne à tous les boutons et composants
        setupModernStyles();
        
        // Configuration des filtres et de la recherche
        setupFiltersAndSearch();
        
        // Configuration de la table
        setupTable();
    }
    
    private void setupModernStyles() {
        // Boutons principaux (noirs)
        setupBlackButton(lancerCalculButton);
        
        // Boutons secondaires (gris)
        setupSecondaryButton(appariementManuelButton);
        setupSecondaryButton(exportButton);
        setupSecondaryButton(resetFiltersButton);
        
        // TextField et ComboBox modernes
        setupModernTextField(searchField);
        setupModernComboBox(statusFilter);
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
    
    private void setupModernTextField(TextField textField) {
        String baseStyle = 
            "-fx-background-color: #ffffff; " +
            "-fx-border-color: #e9ecef; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 8px; " +
            "-fx-background-radius: 8px; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 600; " +
            "-fx-text-fill: #495057; " +
            "-fx-padding: 12px 16px;";
        
        textField.setStyle(baseStyle);
        
        // Effets hover et focus
        textField.setOnMouseEntered(e -> {
            if (!textField.isFocused()) {
                textField.setStyle(baseStyle + 
                    "-fx-border-color: #dee2e6; " +
                    "-fx-background-color: #f8f9fa;");
            }
        });
        
        textField.setOnMouseExited(e -> {
            if (!textField.isFocused()) {
                textField.setStyle(baseStyle);
            }
        });
        
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField.setStyle(baseStyle + 
                    "-fx-border-color: #000000; " +
                    "-fx-border-width: 2px;");
            } else {
                textField.setStyle(baseStyle);
            }
        });
    }
    
    private void setupModernComboBox(ComboBox<?> comboBox) {
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
    
    private void setupFiltersAndSearch() {
        // Configuration des options du filtre de statut
        // statusFilter.getItems().addAll("Tous", "Validé", "En attente", "Rejeté");
        
        // Événements de recherche et filtrage
        // searchField.textProperty().addListener((obs, oldText, newText) -> filterTable());
        // statusFilter.valueProperty().addListener((obs, oldValue, newValue) -> filterTable());
    }
    
    private void setupTable() {
        // Style moderne pour la table
        String tableStyle = 
            "-fx-background-color: transparent; " +
            "-fx-table-cell-border-color: #e9ecef; " +
            "-fx-background-radius: 8px;";
        
        appariementsTable.setStyle(tableStyle);
        
        // Mise à jour des stats
        updateTableStats();
    }
    
    private void updateTableStats() {
        if (tableStatsLabel != null) {
            // Exemple de mise à jour - à adapter selon vos données
            int totalItems = appariementsTable.getItems().size();
            tableStatsLabel.setText(totalItems + " résultat" + (totalItems > 1 ? "s" : ""));
        }
    }
} 