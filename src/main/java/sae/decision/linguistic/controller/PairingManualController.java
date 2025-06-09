package sae.decision.linguistic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PairingManualController {

    // Boutons de navigation
    @FXML
    private Button retourListeButton;

    @FXML
    private Button historiqueButton;

    // ComboBoxes de sélection
    @FXML
    private ComboBox<?> hoteComboBox;

    @FXML
    private ComboBox<?> visiteurComboBox;

    // Labels d'information
    @FXML
    private Label hoteInfoLabel;
    
    @FXML
    private Label visiteurInfoLabel;

    // Card d'aperçu
    @FXML
    private VBox previewCard;
    
    @FXML
    private Label scorePreviewLabel;
    
    @FXML
    private Label compatibilityLabel;
    
    @FXML
    private Label commonInterestsLabel;

    // Boutons d'action
    @FXML
    private Button resetButton;

    @FXML
    private Button createButton;

    @FXML
    public void initialize() {
        System.out.println("PairingManualController initialisé.");
        
        // Application du style moderne
        setupModernStyles();
        
        // Configuration des ComboBoxes
        setupComboBoxes();
        
        // Configuration des événements
        setupEventHandlers();
    }
    
    private void setupModernStyles() {
        // Boutons de navigation (secondaires)
        setupSecondaryButton(retourListeButton);
        setupSecondaryButton(historiqueButton);
        setupSecondaryButton(resetButton);
        
        // Bouton principal (noir)
        setupBlackButton(createButton);
        
        // ComboBoxes modernes
        setupModernComboBox(hoteComboBox);
        setupModernComboBox(visiteurComboBox);
    }
    
    private void setupBlackButton(Button button) {
        String baseStyle = 
            "-fx-background-color: #000000; " +
            "-fx-text-fill: #ffffff; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: 700; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-radius: 12px; " +
            "-fx-border-color: transparent; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 16px 32px;";
        
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
            "-fx-background-radius: 10px; " +
            "-fx-border-radius: 10px; " +
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
    
    private void setupModernComboBox(ComboBox<?> comboBox) {
        String baseStyle = 
            "-fx-background-color: #ffffff; " +
            "-fx-border-color: #e9ecef; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 10px; " +
            "-fx-background-radius: 10px; " +
            "-fx-font-size: 15px; " +
            "-fx-font-weight: 600; " +
            "-fx-text-fill: #495057; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 14px 16px;";
        
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
        // Configuration initiale des ComboBoxes
        // hoteComboBox.getItems().addAll(/* liste des hôtes */);
        // visiteurComboBox.getItems().addAll(/* liste des visiteurs */);
        
        // Style des placeholders
        hoteComboBox.setPromptText("Rechercher un hôte...");
        visiteurComboBox.setPromptText("Rechercher un visiteur...");
    }
    
    private void setupEventHandlers() {
        // Événements de sélection des ComboBoxes
        hoteComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            updateHoteInfo(newValue);
            updatePreview();
        });
        
        visiteurComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            updateVisiteurInfo(newValue);
            updatePreview();
        });
        
        // Événement du bouton reset
        if (resetButton != null) {
            resetButton.setOnAction(e -> resetForm());
        }
        
        // Événement du bouton créer
        if (createButton != null) {
            createButton.setOnAction(e -> createPairing());
        }
    }
    
    private void updateHoteInfo(Object selectedHote) {
        if (selectedHote != null && hoteInfoLabel != null) {
            // Exemple d'information - à adapter selon vos données
            hoteInfoLabel.setText("🏠 France • 16 ans • Hobbies: Football, Musique");
        } else if (hoteInfoLabel != null) {
            hoteInfoLabel.setText("");
        }
    }
    
    private void updateVisiteurInfo(Object selectedVisiteur) {
        if (selectedVisiteur != null && visiteurInfoLabel != null) {
            // Exemple d'information - à adapter selon vos données
            visiteurInfoLabel.setText("🌍 Allemagne • 15 ans • Hobbies: Football, Lecture");
        } else if (visiteurInfoLabel != null) {
            visiteurInfoLabel.setText("");
        }
    }
    
    private void updatePreview() {
        boolean bothSelected = hoteComboBox.getValue() != null && visiteurComboBox.getValue() != null;
        
        if (previewCard != null) {
            previewCard.setVisible(bothSelected);
            previewCard.setManaged(bothSelected);
        }
        
        if (bothSelected && createButton != null) {
            // Calcul du score d'affinité (exemple)
            updateAffinityScore(85); // Score exemple
            
            // Activation du bouton créer
            createButton.setDisable(false);
        } else if (createButton != null) {
            createButton.setDisable(true);
        }
    }
    
    private void updateAffinityScore(int score) {
        if (scorePreviewLabel != null) {
            scorePreviewLabel.setText(score + "%");
            
            // Couleur selon le score
            String color = score >= 80 ? "#28a745" : score >= 60 ? "#ffc107" : "#dc3545";
            scorePreviewLabel.setStyle(scorePreviewLabel.getStyle().replaceAll("-fx-text-fill: [^;]*", "-fx-text-fill: " + color));
        }
        
        if (compatibilityLabel != null) {
            String compatibility;
            String color;
            if (score >= 80) {
                compatibility = "Excellente";
                color = "#28a745";
            } else if (score >= 60) {
                compatibility = "Bonne";
                color = "#ffc107";
            } else {
                compatibility = "Moyenne";
                color = "#dc3545";
            }
            
            compatibilityLabel.setText(compatibility);
            compatibilityLabel.setStyle(compatibilityLabel.getStyle().replaceAll("-fx-text-fill: [^;]*", "-fx-text-fill: " + color));
        }
        
        if (commonInterestsLabel != null) {
            // Exemple de centres d'intérêt communs
            commonInterestsLabel.setText("Football, Sports");
        }
    }
    
    private void resetForm() {
        hoteComboBox.setValue(null);
        visiteurComboBox.setValue(null);
        
        if (hoteInfoLabel != null) hoteInfoLabel.setText("");
        if (visiteurInfoLabel != null) visiteurInfoLabel.setText("");
        
        if (previewCard != null) {
            previewCard.setVisible(false);
            previewCard.setManaged(false);
        }
        
        if (createButton != null) {
            createButton.setDisable(true);
        }
    }
    
    private void createPairing() {
        // Logique de création de l'appariement
        Object selectedHote = hoteComboBox.getValue();
        Object selectedVisiteur = visiteurComboBox.getValue();
        
        if (selectedHote != null && selectedVisiteur != null) {
            System.out.println("Création de l'appariement entre " + selectedHote + " et " + selectedVisiteur);
            
            // Ici vous pourriez ajouter:
            // - Validation des données
            // - Sauvegarde en base de données
            // - Navigation vers la liste des appariements
            // - Affichage d'un message de confirmation
        }
    }
} 