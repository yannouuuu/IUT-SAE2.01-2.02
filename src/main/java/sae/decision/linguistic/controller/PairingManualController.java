package sae.decision.linguistic.controller;

import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import sae.decision.linguistic.model.Adolescent;
import sae.decision.linguistic.model.Affectation;
import sae.decision.linguistic.model.AffinityBreakdown;
import sae.decision.linguistic.model.Criteria;
import sae.decision.linguistic.model.DataManager;

public class PairingManualController {

    // Boutons de navigation
    @FXML
    private Button retourListeButton;

    @FXML
    private Button historiqueButton;

    // ComboBoxes de sélection
    @FXML
    private ComboBox<Adolescent> hoteComboBox;

    @FXML
    private ComboBox<Adolescent> visiteurComboBox;

    // Labels d'information
    @FXML
    private Label hoteInfoLabel;
    
    @FXML
    private Label visiteurInfoLabel;

    // Éléments de la carte de prévisualisation
    @FXML
    private VBox previewCard;
    
    @FXML
    private Label scorePreviewLabel;
    
    @FXML
    private Label compatibilityLabel;
    
    @FXML
    private Label ageScoreLabel;
    
    @FXML
    private Label genderScoreLabel;
    
    @FXML
    private Label hobbiesScoreLabel;
    
    @FXML
    private Label dietCheckLabel;
    
    @FXML
    private Label animalCheckLabel;
    
    @FXML
    private Label historyCheckLabel;

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
        hoteComboBox.getItems().setAll(DataManager.getHosts());
        visiteurComboBox.getItems().setAll(DataManager.getVisitors());
        
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
    
    private void updateHoteInfo(Adolescent selectedHote) {
        if (selectedHote != null && hoteInfoLabel != null) {
            hoteInfoLabel.setText(String.format("🏠 %s • %d ans • Hobbies: %s",
                selectedHote.getCountryOfOrigin(),
                selectedHote.getAge(),
                selectedHote.getCriterion(Criteria.HOBBIES) != null ? selectedHote.getCriterion(Criteria.HOBBIES) : "N/A"
            ));
        } else if (hoteInfoLabel != null) {
            hoteInfoLabel.setText("");
        }
    }
    
    private void updateVisiteurInfo(Adolescent selectedVisiteur) {
        if (selectedVisiteur != null && visiteurInfoLabel != null) {
            visiteurInfoLabel.setText(String.format("🌍 %s • %d ans • Hobbies: %s",
                selectedVisiteur.getCountryOfOrigin(),
                selectedVisiteur.getAge(),
                selectedVisiteur.getCriterion(Criteria.HOBBIES) != null ? selectedVisiteur.getCriterion(Criteria.HOBBIES) : "N/A"
            ));
        } else if (visiteurInfoLabel != null) {
            visiteurInfoLabel.setText("");
        }
    }
    
    private void updatePreview() {
        Adolescent host = hoteComboBox.getValue();
        Adolescent visitor = visiteurComboBox.getValue();
        boolean bothSelected = host != null && visitor != null;
        
        if (previewCard != null) {
            previewCard.setVisible(bothSelected);
            previewCard.setManaged(bothSelected);
        }
        
        if (createButton != null) {
            createButton.setDisable(!bothSelected);
        }

        if (bothSelected) {
            AffinityBreakdown details = visitor.calculateAffinityDetails(host);
            updateAffinityScoreUI(details);
        }
    }
    
    private void updateAffinityScoreUI(AffinityBreakdown details) {
        int score = details.getFinalScore();
        Map<String, Double> componentScores = details.getComponentScores();
        Map<String, Boolean> compatibilityChecks = details.getCompatibilityChecks();

        // Mise à jour du score final et de la compatibilité globale
        scorePreviewLabel.setText(score + "%");
        String scoreColor = getScoreColor(score);
        scorePreviewLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: 700; -fx-text-fill: " + scoreColor + ";");

        String compatibilityText = getCompatibilityText(score);
        compatibilityLabel.setText(compatibilityText);
        compatibilityLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 600; -fx-text-fill: " + scoreColor + ";");
        
        // Mise à jour des détails du score
        ageScoreLabel.setText(String.format("%.0f / 100", componentScores.get("age")));
        genderScoreLabel.setText(String.format("%.0f / 100", componentScores.get("gender")));
        double hobbiesScore = componentScores.getOrDefault("hobbies", 0.0);
        int commonHobbiesCount = componentScores.getOrDefault("commonHobbiesCount", 0.0).intValue();
        hobbiesScoreLabel.setText(String.format("%.0f / 100 (%d commun(s))", hobbiesScore, commonHobbiesCount));

        // Mise à jour des vérifications de compatibilité
        updateCheckLabel(dietCheckLabel, compatibilityChecks.get("diet"), "Compatible", "Non Compatible");
        updateCheckLabel(animalCheckLabel, compatibilityChecks.get("animals"), "Compatible", "Risque d'allergie");
        updateCheckLabel(historyCheckLabel, compatibilityChecks.get("history"), "Jamais appariés", "Déjà appariés !");
    }

    private String getScoreColor(int score) {
        if (score >= 75) return "#28a745"; // Vert
        if (score >= 50) return "#ffc107"; // Jaune
        return "#dc3545"; // Rouge
    }

    private String getCompatibilityText(int score) {
        if (score == 0) return "Incompatible";
        if (score >= 75) return "Excellente";
        if (score >= 50) return "Bonne";
        return "Moyenne";
    }
    
    private void updateCheckLabel(Label label, boolean isOk, String okText, String failText) {
        if (isOk) {
            label.setText("✅ " + okText);
            label.setStyle("-fx-text-fill: #28a745; -fx-font-weight: 600;");
        } else {
            label.setText("❌ " + failText);
            label.setStyle("-fx-text-fill: #dc3545; -fx-font-weight: 600;");
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
        Adolescent host = hoteComboBox.getValue();
        Adolescent visitor = visiteurComboBox.getValue();
        
        if (host != null && visitor != null) {
            System.out.println("Création de l'appariement entre " + visitor + " et " + host);
            
            Affectation lastAffectation = DataManager.getLastAffectation();
            if (lastAffectation == null) {
                // S'il n'y a pas d'affectation en cours, on en crée une nouvelle vide
                // avec tous les élèves actuels pour être cohérent.
                lastAffectation = new Affectation(DataManager.getHosts(), DataManager.getVisitors());
            }

            // Ajoute ou remplace la paire manuelle dans l'affectation
            lastAffectation.getPairs().put(visitor, host);
            
            // Met à jour le DataManager avec l'affectation modifiée
            DataManager.setLastAffectation(lastAffectation);

            // Affiche une confirmation
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Appariement Créé");
            alert.setHeaderText(null);
            alert.setContentText("L'appariement manuel entre " + visitor.getFirstName() + " et " + host.getFirstName() + " a été sauvegardé.");
            alert.showAndWait();
            
            // Navigue vers la liste des appariements pour voir le résultat
            SidebarController sidebar = SidebarController.getInstance();
            if (sidebar != null) {
                sidebar.goToListeAppariements(null);
            }
        }
    }
} 