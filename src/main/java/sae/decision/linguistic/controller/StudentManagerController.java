package sae.decision.linguistic.controller;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
import javafx.stage.FileChooser;
import javafx.util.Duration;
import sae.decision.linguistic.model.Adolescent;
import sae.decision.linguistic.model.Criteria;
import sae.decision.linguistic.model.DataManager;
import sae.decision.linguistic.service.CSVService;

public class StudentManagerController {

    @FXML
    private Button importButton;

    @FXML
    private Button showFormButton;

    @FXML
    private javafx.scene.layout.StackPane switchContainer;


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
    private ComboBox<String> genreComboBox;

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
    private ListView<String> elevesListView;

    private final CSVService csvService = new CSVService();

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
        updateView();

        // Configuration des actions du formulaire
        setupFormActions();

        // Remplir la ComboBox de genre
        genreComboBox.getItems().addAll("male", "female", "other");
    }
    
    private void setupModernSwitch() {
        // Positionnement initial de l'indicateur (côté Hôtes)
        updateSwitchPosition();
        updateActiveLabel();
        
        // Gestionnaires de clic sur le conteneur entier
        switchContainer.setOnMouseClicked(event -> {
            // Calculer si le clic est sur la moitié gauche ou droite
            double clickX = event.getX();
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
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(switchIndicator);
        transition.setToX(-48);
        transition.setDuration(Duration.millis(300));
        transition.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
        transition.play();
    }
    
    private void animateSwitchToRight() {
        // Animation vers la droite (Visiteurs)
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(switchIndicator);
        transition.setToX(48);
        transition.setDuration(Duration.millis(300));
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
        importButton.setOnAction(_ -> handleImport());
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
        button.setOnMouseEntered(_ -> {
            button.setStyle(baseStyle + 
                "-fx-background-color: #333333; " +
                "-fx-scale-x: 1.02; " +
                "-fx-scale-y: 1.02;");
        });
        
        button.setOnMouseExited(_ -> button.setStyle(baseStyle));
        
        // Effet pressed
        button.setOnMousePressed(_ -> {
            button.setStyle(baseStyle + 
                "-fx-scale-x: 0.98; " +
                "-fx-scale-y: 0.98; " +
                "-fx-background-color: #1a1a1a;");
        });
        
        button.setOnMouseReleased(_ -> button.setStyle(baseStyle));
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
        button.setOnMouseEntered(_ -> {
            button.setStyle(baseStyle + 
                "-fx-background-color: #3b5bdb; " +
                "-fx-scale-x: 1.02; " +
                "-fx-scale-y: 1.02;");
        });
        
        button.setOnMouseExited(_ -> button.setStyle(baseStyle));
        
        // Effet pressed
        button.setOnMousePressed(_ -> {
            button.setStyle(baseStyle + 
                "-fx-scale-x: 0.98; " +
                "-fx-scale-y: 0.98; " +
                "-fx-background-color: #2c44a8;");
        });
        
        button.setOnMouseReleased(_ -> button.setStyle(baseStyle));
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
        button.setOnMouseEntered(_ -> {
            button.setStyle(baseStyle + 
                "-fx-background-color: #dee2e6; " +
                "-fx-text-fill: #495057; " +
                "-fx-scale-x: 1.02; " +
                "-fx-scale-y: 1.02;");
        });
        
        button.setOnMouseExited(_ -> button.setStyle(baseStyle));
        
        // Effet pressed
        button.setOnMousePressed(_ -> {
            button.setStyle(baseStyle + 
                "-fx-scale-x: 0.98; " +
                "-fx-scale-y: 0.98; " +
                "-fx-background-color: #ced4da;");
        });
        
        button.setOnMouseReleased(_ -> button.setStyle(baseStyle));
    }
    
    private void setupToggleButtons() {
        // Style moderne pour les toggle buttons du formulaire
        setupToggleButton(hoteTypeToggle, true);
        setupToggleButton(visiteurTypeToggle, false);
        
        // Gestionnaire pour synchroniser les styles lors du changement
        typeToggleGroup.selectedToggleProperty().addListener((_, _, _) -> {
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
        button.setOnMouseEntered(_ -> {
            if (button.isSelected()) {
                button.setStyle(activeStyle + "-fx-background-color: #f8f9fa; -fx-scale-x: 1.02; -fx-scale-y: 1.02;");
            } else {
                button.setStyle(inactiveStyle + 
                    "-fx-background-color: #dee2e6; " +
                    "-fx-text-fill: #495057; " +
                    "-fx-scale-x: 1.02; -fx-scale-y: 1.02;");
            }
        });
        
        button.setOnMouseExited(_ -> {
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
            File file = chooseCsvFile();
            if (file != null) {
                List<Adolescent> hosts = csvService.importAdolescents(file.getAbsolutePath(), true);
                DataManager.addAdolescents(hosts);
                updateListView();
            }
        } else {
            System.out.println("Lancement de l'importation CSV pour les Visiteurs...");
            File file = chooseCsvFile();
            if (file != null) {
                List<Adolescent> visitors = csvService.importAdolescents(file.getAbsolutePath(), false);
                DataManager.addAdolescents(visitors);
                updateListView();
            }
        }
    }
    
    private File chooseCsvFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier CSV");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv")
        );
        return fileChooser.showOpenDialog(importButton.getScene().getWindow());
    }
    
    private void updateView() {
        // Mettre à jour la vue principale (titre de la liste, contenu, etc.)
        updateListTitle();
        updateImportButton();
        updateListView();
    }
    
    private void updateListView() {
        List<Adolescent> listToShow;
        if (isHotesSelected) {
            listToShow = DataManager.getHosts();
        } else {
            listToShow = DataManager.getVisitors();
        }

        elevesListView.getItems().setAll(
                listToShow.stream()
                        .map(ado -> ado.getFirstName() + " " + ado.getLastName())
                        .collect(Collectors.toList())
        );

        updateListTitle();
    }
    
    private void updateListTitle() {
        if (isHotesSelected) {
            int count = DataManager.getHosts().size();
            listTitleLabel.setText("Hôtes (" + count + ")");
        } else {
            int count = DataManager.getVisitors().size();
            listTitleLabel.setText("Visiteurs (" + count + ")");
        }
    }

    private void setupFormActions() {
        showFormButton.setOnAction(_ -> showCreateForm());
        cancelButton.setOnAction(_ -> hideForm());
        saveButton.setOnAction(_ -> handleSave());
    }

    private void showCreateForm() {
        formTitleLabel.setText("Créer un nouvel élève");
        saveButton.setText("Créer l'élève");
        formPane.setVisible(true);
        formPane.setManaged(true);
        clearForm();
    }

    private void hideForm() {
        formPane.setVisible(false);
        formPane.setManaged(false);
        clearForm();
    }

    private void clearForm() {
        prenomField.clear();
        nomField.clear();
        dateNaissancePicker.setValue(null);
        genreComboBox.setValue(null);
        paysField.clear();
        hobbiesField.clear();
        sansNoixCheckBox.setSelected(false);
        vegetarienCheckBox.setSelected(false);
        hoteTypeToggle.setSelected(true);
    }
    
    private void handleSave() {
        // Validation
        if (prenomField.getText().isEmpty() || nomField.getText().isEmpty() || dateNaissancePicker.getValue() == null || genreComboBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de validation");
            alert.setHeaderText(null);
            alert.setContentText("Les champs Prénom, Nom, Date de naissance et Genre sont obligatoires.");
            alert.showAndWait();
            return;
        }

        // Gather data
        String prenom = prenomField.getText();
        String nom = nomField.getText();
        LocalDate dateNaissance = dateNaissancePicker.getValue();
        String genre = genreComboBox.getValue();
        String pays = paysField.getText();
        boolean isHost = hoteTypeToggle.isSelected();

        Map<Criteria, String> criteria = new HashMap<>();
        
        // Hobbies
        if (!hobbiesField.getText().trim().isEmpty()) {
            criteria.put(Criteria.HOBBIES, hobbiesField.getText().trim());
        }

        // Food
        List<String> foodList = new ArrayList<>();
        if (vegetarienCheckBox.isSelected()) {
            foodList.add("vegetarian");
        }
        if (sansNoixCheckBox.isSelected()) {
            foodList.add("no-nuts");
        }
        if (!foodList.isEmpty()) {
            String foodString = String.join(",", foodList);
            criteria.put(isHost ? Criteria.HOST_FOOD : Criteria.GUEST_FOOD, foodString);
        }

        // Create Adolescent
        Adolescent newAdo = new Adolescent(nom, prenom, genre, pays, criteria, dateNaissance, isHost);

        // Add to DataManager
        DataManager.addAdolescents(List.of(newAdo));
        
        System.out.println("Nouvel élève créé : " + newAdo);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("L'élève " + prenom + " " + nom + " a été ajouté avec succès.");
        alert.showAndWait();

        // Update UI
        hideForm();
        updateListView();
    }
} 