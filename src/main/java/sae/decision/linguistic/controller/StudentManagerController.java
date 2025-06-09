package sae.decision.linguistic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class StudentManagerController {

    @FXML
    private Button importButton;

    @FXML
    private Button showFormButton;

    @FXML
    private javafx.scene.layout.StackPane switchContainer;

    @FXML
    private Label hotesLabel;

    @FXML
    private Label visiteursLabel;

    @FXML
    private javafx.scene.layout.StackPane switchIndicator;

    @FXML
    private Label activeLabel;

    private boolean isHotesSelected = true;

    @FXML
    private VBox formPane;

    @FXML
    private Label formTitleLabel;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField nomField;

    @FXML
    private DatePicker dateNaissancePicker;

    @FXML
    private ComboBox<?> genreComboBox;

    @FXML
    private TextField paysField;

    @FXML
    private TextField hobbiesField;

    @FXML
    private CheckBox sansNoixCheckBox;

    @FXML
    private CheckBox vegetarienCheckBox;

    @FXML
    private ToggleButton hoteTypeToggle;

    @FXML
    private ToggleGroup typeToggleGroup;

    @FXML
    private ToggleButton visiteurTypeToggle;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label listTitleLabel;

    @FXML
    private ListView<?> elevesListView;

    @FXML
    public void initialize() {
        System.out.println("StudentManagerController initialisé.");
        
        // Configuration du switch moderne
        setupModernSwitch();
        
        // Configuration du style des boutons
        setupModernButtons();
        
        // Mise à jour initiale du bouton d'importation
        updateImportButton();
        
        // Logique pour afficher/cacher le formulaire, gérer le switch, etc.
    }
    
    private void setupModernSwitch() {
        // Positionnement initial de l'indicateur (côté Hôtes)
        updateSwitchPosition();
        updateActiveLabel();
        
        // Gestionnaires de clic sur le conteneur entier
        switchContainer.setOnMouseClicked(e -> {
            // Calculer si le clic est sur la moitié gauche ou droite
            double clickX = e.getX();
            double containerWidth = switchContainer.getWidth();
            boolean clickedLeft = clickX < (containerWidth / 2);
            
            if (clickedLeft && !isHotesSelected) {
                // Clic sur la partie gauche, passage à Hôtes
                isHotesSelected = true;
                animateSwitchToLeft();
                updateActiveLabel();
                updateView();
            } else if (!clickedLeft && isHotesSelected) {
                // Clic sur la partie droite, passage à Visiteurs
                isHotesSelected = false;
                animateSwitchToRight();
                updateActiveLabel();
                updateView();
            }
        });
        
        // Curseur main sur tout le switch
        switchContainer.setStyle(switchContainer.getStyle() + "-fx-cursor: hand;");
    }
    
    private void updateSwitchPosition() {
        // Position initiale de l'indicateur
        // Conteneur: 200px - padding 8px = 192px utiles
        // Indicateur: 96px, donc centré sur moitié gauche/droite = ±48px
        switchIndicator.setTranslateX(isHotesSelected ? -48 : 48);
    }
    
    private void animateSwitchToLeft() {
        // Animation vers la gauche (Hôtes)
        javafx.animation.TranslateTransition transition = new javafx.animation.TranslateTransition();
        transition.setNode(switchIndicator);
        transition.setToX(-48);
        transition.setDuration(javafx.util.Duration.millis(300));
        transition.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
        transition.play();
    }
    
    private void animateSwitchToRight() {
        // Animation vers la droite (Visiteurs)
        javafx.animation.TranslateTransition transition = new javafx.animation.TranslateTransition();
        transition.setNode(switchIndicator);
        transition.setToX(48);
        transition.setDuration(javafx.util.Duration.millis(300));
        transition.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
        transition.play();
    }
    
    private void updateActiveLabel() {
        // Met à jour le texte du label sur l'indicateur blanc
        if (isHotesSelected) {
            activeLabel.setText("Hôtes");
        } else {
            activeLabel.setText("Visiteurs");
        }
    }
    
    private void setupModernButtons() {
        // Bouton "Créer un élève" en noir/blanc
        setupBlackButton(showFormButton);
        
        // Bouton "Créer l'élève" avec palette du switch
        setupSwitchStyleButton(saveButton);
        
        // Styles pour les boutons secondaires avec palette du switch
        setupSecondaryButton(importButton);
        setupSecondaryButton(cancelButton);
        
        // Styles pour les boutons toggle du formulaire
        setupToggleButtons();

        // Action pour le bouton d'import
        importButton.setOnAction(e -> handleImport());
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
    
    private void setupSwitchStyleButton(Button button) {
        String baseStyle = 
            "-fx-background-color: #ffffff; " +
            "-fx-text-fill: #000000; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 700; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-radius: 12px; " +
            "-fx-border-color: #e9ecef; " +
            "-fx-border-width: 2px; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 10px 20px;";
        
        button.setStyle(baseStyle);
        
        // Effets hover
        button.setOnMouseEntered(e -> {
            button.setStyle(baseStyle + 
                "-fx-background-color: #f8f9fa; " +
                "-fx-scale-x: 1.02; " +
                "-fx-scale-y: 1.02;");
        });
        
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
        
        // Effet pressed
        button.setOnMousePressed(e -> {
            button.setStyle(baseStyle + 
                "-fx-scale-x: 0.98; " +
                "-fx-scale-y: 0.98; " +
                "-fx-background-color: #e9ecef;");
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
    
    private void setupToggleButtons() {
        // Style moderne pour les toggle buttons du formulaire
        setupToggleButton(hoteTypeToggle, true);
        setupToggleButton(visiteurTypeToggle, false);
        
        // Gestionnaire pour synchroniser les styles lors du changement
        typeToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            setupToggleButton(hoteTypeToggle, hoteTypeToggle.isSelected());
            setupToggleButton(visiteurTypeToggle, visiteurTypeToggle.isSelected());
        });
    }
    
    private void setupToggleButton(ToggleButton button, boolean isSelected) {
        String activeStyle = 
            "-fx-background-color: #ffffff; " +
            "-fx-text-fill: #000000; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: 700; " +
            "-fx-background-radius: 8px; " +
            "-fx-border-radius: 8px; " +
            "-fx-border-color: #e9ecef; " +
            "-fx-border-width: 2px; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 6px 16px;";
        
        String inactiveStyle = 
            "-fx-background-color: #e9ecef; " +
            "-fx-text-fill: #6c6c6c; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: 700; " +
            "-fx-background-radius: 8px; " +
            "-fx-border-radius: 8px; " +
            "-fx-border-color: transparent; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 6px 16px;";
        
        button.setStyle(isSelected ? activeStyle : inactiveStyle);
        
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
    
    private void updateImportButton() {
        if (isHotesSelected) {
            importButton.setText("Import CSV Hôtes");
        } else {
            importButton.setText("Import CSV Visiteurs");
        }
    }

    private void handleImport() {
        if (isHotesSelected) {
            System.out.println("Lancement de l'importation CSV pour les Hôtes...");
            // Logique d'importation pour les hôtes
        } else {
            System.out.println("Lancement de l'importation CSV pour les Visiteurs...");
            // Logique d'importation pour les visiteurs
        }
    }
    
    private void updateView() {
        // Mettre à jour la vue principale (titre de la liste, contenu, etc.)
        updateListTitle();
        updateImportButton();
    }
    
    private void updateListTitle() {
        if (isHotesSelected) {
            listTitleLabel.setText("Hôtes");
            System.out.println("Vue Hôtes sélectionnée");
            // Logique pour afficher les hôtes
        } else {
            listTitleLabel.setText("Visiteurs");
            System.out.println("Vue Visiteurs sélectionnée");
            // Logique pour afficher les visiteurs
        }
    }
} 