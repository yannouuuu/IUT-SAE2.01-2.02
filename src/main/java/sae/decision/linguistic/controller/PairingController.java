package sae.decision.linguistic.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import sae.decision.linguistic.model.Adolescent;
import sae.decision.linguistic.model.Affectation;
import sae.decision.linguistic.model.DataManager;
import sae.decision.linguistic.model.PairingDisplay;
import sae.decision.linguistic.service.AppariementService;

public class PairingController {

    // Boutons du header
    @FXML
    private Button lancerCalculButton;
    
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
    private ComboBox<String> statusFilter;
    
    @FXML
    private Button resetFiltersButton;

    // Table et colonnes
    @FXML
    private TableView<PairingDisplay> appariementsTable;

    @FXML
    private TableColumn<PairingDisplay, String> hoteColumn;

    @FXML
    private TableColumn<PairingDisplay, String> visiteurColumn;

    @FXML
    private TableColumn<PairingDisplay, Number> scoreColumn;

    @FXML
    private TableColumn<PairingDisplay, String> statutColumn;

    @FXML
    private Label tableStatsLabel;

    private AppariementService appariementService;
    private final List<PairingDisplay> allPairings = new ArrayList<>();
    private ObservableList<PairingDisplay> filteredPairings;

    @FXML
    public void initialize() {
        System.out.println("PairingController a été initialisé et est prêt.");
        this.appariementService = new AppariementService();
        this.filteredPairings = FXCollections.observableArrayList();
        appariementsTable.setItems(filteredPairings);
        
        // Application du style moderne à tous les boutons et composants
        setupModernStyles();
        
        // Configuration des filtres et de la recherche
        setupFiltersAndSearch();
        
        // Configuration de la table
        setupTable();
        
        // Remplir la table avec les données actuelles
        populateTable();
        
        // Ajouter un listener pour mettre à jour la table si les données changent
        DataManager.lastAffectationProperty().addListener((_, _, _) -> {
            System.out.println("L'affectation a changé, mise à jour de la table des appariements.");
            populateTable();
        });

        // Cacher le bouton de calcul, qui est maintenant sur la page d'accueil
        lancerCalculButton.setVisible(false);
        
        // Action pour le bouton d'export
        exportButton.setOnAction(_ -> handleExport());
    }
    
    private void setupModernStyles() {
        // Boutons principaux (noirs)
        setupBlackButton(lancerCalculButton);
        
        // Boutons secondaires (gris)
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
        textField.setOnMouseEntered(_ -> {
            if (!textField.isFocused()) {
                textField.setStyle(baseStyle + 
                    "-fx-border-color: #dee2e6; " +
                    "-fx-background-color: #f8f9fa;");
            }
        });
        
        textField.setOnMouseExited(_ -> {
            if (!textField.isFocused()) {
                textField.setStyle(baseStyle);
            }
        });
        
        textField.focusedProperty().addListener((_, _, isNowFocused) -> {
            if (isNowFocused) {
                textField.setStyle(baseStyle + 
                    "-fx-border-color: #000000; " +
                    "-fx-border-width: 2px;");
            } else {
                textField.setStyle(baseStyle);
            }
        });
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
        comboBox.setOnMouseEntered(_ -> {
            if (!comboBox.isFocused()) {
                comboBox.setStyle(baseStyle + 
                    "-fx-border-color: #dee2e6; " +
                    "-fx-background-color: #f8f9fa;");
            }
        });
        
        comboBox.setOnMouseExited(_ -> {
            if (!comboBox.isFocused()) {
                comboBox.setStyle(baseStyle);
            }
        });
        
        comboBox.focusedProperty().addListener((_, _, isNowFocused) -> {
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
        statusFilter.getItems().addAll("Tous les statuts", "Validé", "En attente", "Score faible");
        statusFilter.setValue("Tous les statuts");

        searchField.textProperty().addListener((_, _, _) -> applyFilters());
        statusFilter.valueProperty().addListener((_, _, _) -> applyFilters());

        resetFiltersButton.setOnAction(_ -> {
            searchField.clear();
            statusFilter.setValue("Tous les statuts");
            applyFilters();
        });
    }
    
    private void setupTable() {
        hoteColumn.setCellValueFactory(new PropertyValueFactory<>("hostName"));
        visiteurColumn.setCellValueFactory(new PropertyValueFactory<>("visitorName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("affinityScore"));
        
        // Affichage du statut avec couleur
        statutColumn.setCellValueFactory(cellData -> {
            int score = cellData.getValue().getAffinityScore();
            String status = getStatusTextFromScore(score);
            return new SimpleStringProperty(status);
        });

        statutColumn.setCellFactory(_ -> new TableCell<PairingDisplay, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    // Récupérer le score pour définir la couleur
                    PairingDisplay pairing = getTableView().getItems().get(getIndex());
                    if (pairing != null) {
                        int score = pairing.getAffinityScore();
                        setStyle("-fx-font-weight: bold; -fx-text-fill: " + getScoreColor(score) + ";");
                    }
                }
            }
        });

        // Style moderne pour la table
        String tableStyle = 
            "-fx-background-color: transparent; " +
            "-fx-table-cell-border-color: #e9ecef; " +
            "-fx-background-radius: 8px;";
        
        appariementsTable.setStyle(tableStyle);
        
        // Mise à jour des stats
        updateTableStats();

        scoreColumn.setCellFactory(_ -> new TableCell<PairingDisplay, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    int score = item.intValue();
                    setText(score + "%");
                    setStyle("-fx-font-weight: bold; -fx-text-fill: " + getScoreColor(score) + ";");
                }
            }
        });
    }
    
    private void populateTable() {
        allPairings.clear();
        Affectation currentAffectation = DataManager.getLastAffectation();

        if (currentAffectation != null && currentAffectation.getPairs() != null) {
            for (Map.Entry<Adolescent, Adolescent> entry : currentAffectation.getPairs().entrySet()) {
                Adolescent visitor = entry.getKey();
                Adolescent host = entry.getValue();
                int score = visitor.calculateAffinity(host);
                allPairings.add(new PairingDisplay(host, visitor, score));
            }
        }
        
        applyFilters();
        updateStats();
    }
    
    private void handleExport() {
        Affectation lastAffectation = DataManager.getLastAffectation();
        if (lastAffectation == null || lastAffectation.getPairs().isEmpty()) {
            // Afficher une alerte ?
            System.out.println("Rien à exporter.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les affectations en CSV");
        fileChooser.setInitialFileName("affectations.csv");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier CSV", "*.csv")
        );
        File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());

        if (file != null) {
            appariementService.exporterAffectation(lastAffectation, file.getAbsolutePath());
            System.out.println("Exportation réussie vers " + file.getAbsolutePath());
        }
    }
    
    private void updateTableStats() {
        if (tableStatsLabel != null) {
            // Exemple de mise à jour - à adapter selon vos données
            int totalItems = appariementsTable.getItems().size();
            tableStatsLabel.setText(totalItems + " résultat" + (totalItems > 1 ? "s" : ""));
        }
    }

    private void applyFilters() {
        List<PairingDisplay> filteredList = new ArrayList<>(allPairings);

        // 1. Filtre par texte de recherche
        String searchText = searchField.getText();
        if (searchText != null && !searchText.isEmpty()) {
            String lowerCaseFilter = searchText.toLowerCase();
            filteredList = filteredList.stream()
                .filter(p -> p.getHostName().toLowerCase().contains(lowerCaseFilter) ||
                             p.getVisitorName().toLowerCase().contains(lowerCaseFilter))
                .collect(Collectors.toList());
        }

        // 2. Filtre par statut
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null && !statusFilterValue.equals("Tous les statuts")) {
            filteredList = filteredList.stream()
                .filter(p -> getStatusTextFromScore(p.getAffinityScore()).equals(statusFilterValue))
                .collect(Collectors.toList());
        }

        filteredPairings.setAll(filteredList);
        tableStatsLabel.setText(filteredList.size() + " résultats");
    }

    private void updateStats() {
        int total = allPairings.size();
        long valides = allPairings.stream().filter(p -> p.getAffinityScore() >= 75).count();
        long scoresFaibles = allPairings.stream().filter(p -> p.getAffinityScore() < 50).count();

        totalAppariementsLabel.setText(String.valueOf(total));
        validesLabel.setText(String.valueOf(valides));
        // L'ID enAttenteLabel correspond à la carte "Scores Faibles" dans le FXML
        enAttenteLabel.setText(String.valueOf(scoresFaibles));
    }

    private String getScoreColor(int score) {
        if (score >= 75) return "#28a745"; // Vert
        if (score >= 50) return "#ffc107"; // Jaune
        return "#dc3545"; // Rouge
    }
    
    private String getStatusTextFromScore(int score) {
        if (score >= 75) return "Validé";
        if (score >= 50) return "En attente";
        return "Score faible";
    }
} 