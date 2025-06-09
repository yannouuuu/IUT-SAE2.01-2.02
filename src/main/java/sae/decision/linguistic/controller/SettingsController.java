package sae.decision.linguistic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import java.io.File;

public class SettingsController {

    // === Sliders et Labels de Pondération ===
    @FXML private Label ageLabel;
    @FXML private Slider ageSlider;
    @FXML private Label ageValueLabel;
    
    @FXML private Label sexeLabel;
    @FXML private Slider sexeSlider;
    @FXML private Label sexeValueLabel;
    
    @FXML private Label interetLabel;
    @FXML private Slider interetSlider;
    @FXML private Label interetValueLabel;
    
    @FXML private Label langueLabel;
    @FXML private Slider langueSlider;
    @FXML private Label langueValueLabel;
    
    @FXML private Label activitesLabel;
    @FXML private Slider activitesSlider;
    @FXML private Label activitesValueLabel;
    
    @FXML private Label totalLabel;

    // === Boutons Header ===
    @FXML private Button importConfigButton;
    @FXML private Button exportConfigButton;
    @FXML private Button resetWeightsButton;

    // === Préférences Générales ===
    @FXML private ToggleButton autoSaveSwitch;
    @FXML private ToggleButton notificationsSwitch;
    @FXML private ToggleButton darkModeSwitch;

    // === Paramètres Avancés ===
    @FXML private ComboBox<String> algorithmComboBox;
    @FXML private ComboBox<String> defaultLanguageComboBox;
    @FXML private TextField exportPathField;
    @FXML private Button browseButton;

    // === Actions Finales ===
    @FXML private Button resetAllButton;
    @FXML private Button saveButton;

    @FXML
    public void initialize() {
        System.out.println("SettingsController initialisé avec interface moderne.");
        
        // Initialisation des ComboBox
        initializeComboBoxes();
        
        // Configuration des sliders avec mise à jour en temps réel
        setupSliders();
        
        // Configuration des boutons avec style moderne
        setupButtons();
        
        // Configuration des switches avec style moderne
        setupToggleButtons();
        
        // Configuration des ComboBox avec style moderne
        setupComboBoxes();
        
        // Configuration des TextField avec style moderne
        setupTextFields();
        
        // Configuration des Sliders avec style moderne
        setupSlidersStyle();
        
        // Initialisation des valeurs par défaut
        initializeDefaultValues();
    }
    
    private void initializeComboBoxes() {
        // Algorithmes d'appariement
        algorithmComboBox.getItems().addAll(
            "Algorithme Standard",
            "Algorithme Optimisé",
            "Algorithme Équilibré",
            "Algorithme Personnalisé"
        );
        algorithmComboBox.setValue("Algorithme Standard");
        
        // Langues disponibles
        defaultLanguageComboBox.getItems().addAll(
            "Français",
            "English",
            "Deutsch",
            "Español",
            "Italiano"
        );
        defaultLanguageComboBox.setValue("Français");
    }
    
    private void setupSliders() {
        // Configuration des sliders avec listeners pour mise à jour temps réel
        ageSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            ageValueLabel.setText(String.format("%.0f%%", newVal.doubleValue()));
            updateTotal();
        });
        
        sexeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            sexeValueLabel.setText(String.format("%.0f%%", newVal.doubleValue()));
            updateTotal();
        });
        
        interetSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            interetValueLabel.setText(String.format("%.0f%%", newVal.doubleValue()));
            updateTotal();
        });
        
        langueSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            langueValueLabel.setText(String.format("%.0f%%", newVal.doubleValue()));
            updateTotal();
        });
        
        activitesSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            activitesValueLabel.setText(String.format("%.0f%%", newVal.doubleValue()));
            updateTotal();
        });
        
        // Initialisation des valeurs par défaut
        ageSlider.setValue(20);
        sexeSlider.setValue(15);
        interetSlider.setValue(30);
        langueSlider.setValue(25);
        activitesSlider.setValue(10);
    }
    
    private void setupButtons() {
        // Header buttons
        importConfigButton.setOnAction(e -> handleImportConfig());
        exportConfigButton.setOnAction(e -> handleExportConfig());
        resetWeightsButton.setOnAction(e -> handleResetWeights());
        
        // Browse button
        browseButton.setOnAction(e -> handleBrowseDirectory());
        
        // Action buttons
        resetAllButton.setOnAction(e -> handleResetAll());
        saveButton.setOnAction(e -> handleSave());
        
        // Application des styles modernes
        setupPrimaryButton(exportConfigButton);
        setupPrimaryButton(saveButton);
        setupSecondaryButton(importConfigButton);
        setupSecondaryButton(resetWeightsButton);
        setupSecondaryButton(browseButton);
        setupSecondaryButton(resetAllButton);
    }
    
    private void setupToggleButtons() {
        // Application du style moderne aux switches
        setupModernToggleButton(autoSaveSwitch);
        setupModernToggleButton(notificationsSwitch);
        setupModernToggleButton(darkModeSwitch);
        
        // Configuration des switches avec texte dynamique
        autoSaveSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            autoSaveSwitch.setText(isSelected ? "ON" : "OFF");
            updateToggleButtonStyle(autoSaveSwitch);
        });
        
        notificationsSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            notificationsSwitch.setText(isSelected ? "ON" : "OFF");
            updateToggleButtonStyle(notificationsSwitch);
        });
        
        darkModeSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            darkModeSwitch.setText(isSelected ? "ON" : "OFF");
            updateToggleButtonStyle(darkModeSwitch);
        });
        
        // Valeurs par défaut
        autoSaveSwitch.setSelected(true);
        notificationsSwitch.setSelected(true);
        darkModeSwitch.setSelected(false);
    }
    
    private void setupComboBoxes() {
        setupModernComboBox(algorithmComboBox);
        setupModernComboBox(defaultLanguageComboBox);
    }
    
    private void setupTextFields() {
        setupModernTextField(exportPathField);
    }
    
    private void setupSlidersStyle() {
        setupModernSlider(ageSlider);
        setupModernSlider(sexeSlider);
        setupModernSlider(interetSlider);
        setupModernSlider(langueSlider);
        setupModernSlider(activitesSlider);
    }
    
    // === Méthodes de Style Moderne ===
    
    private void setupPrimaryButton(Button button) {
        String baseStyle = 
            "-fx-background-color: #000000; " +
            "-fx-text-fill: #ffffff; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 700; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-radius: 12px; " +
            "-fx-border-color: transparent; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 10px 20px;";
        
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
            "-fx-padding: 10px 20px;";
        
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
    
    private void setupModernToggleButton(ToggleButton button) {
        updateToggleButtonStyle(button);
        
        // Gestionnaire pour mettre à jour le style lors du changement
        button.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            updateToggleButtonStyle(button);
        });
    }
    
    private void updateToggleButtonStyle(ToggleButton button) {
        String activeStyle = 
            "-fx-background-color: #ffffff; " +
            "-fx-text-fill: #000000; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: 700; " +
            "-fx-background-radius: 8px; " +
            "-fx-border-radius: 8px; " +
            "-fx-border-color: #000000; " +
            "-fx-border-width: 2px; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 8px 16px;";
        
        String inactiveStyle = 
            "-fx-background-color: #e9ecef; " +
            "-fx-text-fill: #6c6c6c; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: 700; " +
            "-fx-background-radius: 8px; " +
            "-fx-border-radius: 8px; " +
            "-fx-border-color: transparent; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 8px 16px;";
        
        button.setStyle(button.isSelected() ? activeStyle : inactiveStyle);
        
        // Effets hover
        button.setOnMouseEntered(e -> {
            if (button.isSelected()) {
                button.setStyle(activeStyle + "-fx-background-color: #f8f9fa; -fx-scale-x: 1.02; -fx-scale-y: 1.02;");
            } else {
                button.setStyle(inactiveStyle + 
                    "-fx-background-color: #dee2e6; " +
                    "-fx-text-fill: #495057; " +
                    "-fx-scale-x: 1.02; -fx-scale-y: 1.02;");
            }
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(button.isSelected() ? activeStyle : inactiveStyle);
        });
    }
    
    private void setupModernComboBox(ComboBox<String> comboBox) {
        String baseStyle = 
            "-fx-background-color: #ffffff; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 8px; " +
            "-fx-background-radius: 8px; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 500; " +
            "-fx-text-fill: #495057; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 8px 12px;";
        
        comboBox.setStyle(baseStyle);
        
        // Effets focus
        comboBox.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                comboBox.setStyle(baseStyle + 
                    "-fx-border-color: #000000; " +
                    "-fx-background-color: #f8f9fa;");
            } else {
                comboBox.setStyle(baseStyle);
            }
        });
        
        // Effets hover
        comboBox.setOnMouseEntered(e -> {
            if (!comboBox.isFocused()) {
                comboBox.setStyle(baseStyle + 
                    "-fx-border-color: #adb5bd; " +
                    "-fx-background-color: #f8f9fa;");
            }
        });
        
        comboBox.setOnMouseExited(e -> {
            if (!comboBox.isFocused()) {
                comboBox.setStyle(baseStyle);
            }
        });
    }
    
    private void setupModernTextField(TextField textField) {
        String baseStyle = 
            "-fx-background-color: #ffffff; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 8px; " +
            "-fx-background-radius: 8px; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 500; " +
            "-fx-text-fill: #495057; " +
            "-fx-padding: 8px 12px;";
        
        textField.setStyle(baseStyle);
        
        // Effets focus
        textField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                textField.setStyle(baseStyle + 
                    "-fx-border-color: #000000; " +
                    "-fx-background-color: #f8f9fa;");
            } else {
                textField.setStyle(baseStyle);
            }
        });
        
        // Effets hover
        textField.setOnMouseEntered(e -> {
            if (!textField.isFocused()) {
                textField.setStyle(baseStyle + 
                    "-fx-border-color: #adb5bd; " +
                    "-fx-background-color: #f8f9fa;");
            }
        });
        
        textField.setOnMouseExited(e -> {
            if (!textField.isFocused()) {
                textField.setStyle(baseStyle);
            }
        });
    }
    
    private void setupModernSlider(Slider slider) {
        String sliderStyle = 
            "-fx-background-color: #f8f9fa; " +
            "-fx-background-radius: 10px; " +
            "-fx-padding: 4px;";
        
        slider.setStyle(sliderStyle);
        
        // Attendre que le composant soit rendu pour appliquer les styles aux sous-éléments
        slider.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                slider.applyCss();
                slider.layout();
                
                // Style pour la piste
                if (slider.lookup(".track") != null) {
                    slider.lookup(".track").setStyle(
                        "-fx-background-color: #e9ecef; " +
                        "-fx-background-radius: 4px; " +
                        "-fx-background-insets: 0;");
                }
                
                // Style pour le thumb
                if (slider.lookup(".thumb") != null) {
                    slider.lookup(".thumb").setStyle(
                        "-fx-background-color: #000000; " +
                        "-fx-background-radius: 12px; " +
                        "-fx-background-insets: 0; " +
                        "-fx-cursor: hand;");
                }
            }
        });
    }
    
    private void initializeDefaultValues() {
        // Chemin d'export par défaut
        exportPathField.setText(System.getProperty("user.home") + "/Desktop");
    }
    
    private void updateTotal() {
        double total = ageSlider.getValue() + sexeSlider.getValue() + 
                      interetSlider.getValue() + langueSlider.getValue() + 
                      activitesSlider.getValue();
        
        totalLabel.setText(String.format("%.0f%%", total));
        
        // Changement de couleur selon la validité du total
        totalLabel.setStyle(
            total == 100 ? 
            "-fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: #28a745;" :
            "-fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: #dc3545;"
        );
    }
    
    // === Gestionnaires d'événements ===
    
    private void handleImportConfig() {
        System.out.println("Import de configuration demandé");
        // TODO: Logique d'import de fichier de configuration
        showInfoAlert("Import Configuration", "Fonctionnalité d'import bientôt disponible");
    }
    
    private void handleExportConfig() {
        System.out.println("Export de configuration demandé");
        // TODO: Logique d'export de fichier de configuration
        showInfoAlert("Export Configuration", "Configuration exportée avec succès!");
    }
    
    private void handleResetWeights() {
        System.out.println("Réinitialisation des pondérations");
        
        // Réinitialisation aux valeurs par défaut
        ageSlider.setValue(20);
        sexeSlider.setValue(20);
        interetSlider.setValue(20);
        langueSlider.setValue(20);
        activitesSlider.setValue(20);
        
        showInfoAlert("Pondérations", "Pondérations réinitialisées aux valeurs par défaut");
    }
    
    private void handleBrowseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Sélectionner le dossier d'export");
        
        // Dossier initial
        String currentPath = exportPathField.getText();
        if (!currentPath.isEmpty()) {
            File currentDir = new File(currentPath);
            if (currentDir.exists()) {
                directoryChooser.setInitialDirectory(currentDir);
            }
        }
        
        File selectedDirectory = directoryChooser.showDialog(browseButton.getScene().getWindow());
        if (selectedDirectory != null) {
            exportPathField.setText(selectedDirectory.getAbsolutePath());
        }
    }
    
    private void handleResetAll() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Réinitialisation Complète");
        alert.setHeaderText("Réinitialiser tous les paramètres?");
        alert.setContentText("Cette action remettra tous les réglages aux valeurs par défaut.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Réinitialisation complète
                handleResetWeights();
                autoSaveSwitch.setSelected(true);
                notificationsSwitch.setSelected(true);
                darkModeSwitch.setSelected(false);
                algorithmComboBox.setValue("Algorithme Standard");
                defaultLanguageComboBox.setValue("Français");
                exportPathField.setText(System.getProperty("user.home") + "/Desktop");
                
                showInfoAlert("Réinitialisation", "Tous les paramètres ont été réinitialisés!");
            }
        });
    }
    
    private void handleSave() {
        System.out.println("Sauvegarde des paramètres");
        
        // Validation du total des pondérations
        double total = ageSlider.getValue() + sexeSlider.getValue() + 
                      interetSlider.getValue() + langueSlider.getValue() + 
                      activitesSlider.getValue();
        
        if (Math.abs(total - 100) > 0.1) {
            showErrorAlert("Erreur de Validation", 
                          "Le total des pondérations doit être égal à 100%.\n" +
                          "Total actuel: " + String.format("%.1f%%", total));
            return;
        }
        
        // TODO: Logique de sauvegarde des paramètres
        showInfoAlert("Sauvegarde", "Paramètres sauvegardés avec succès!");
    }
    
    // === Utilitaires ===
    
    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 