package sae.decision.linguistic.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sae.decision.linguistic.model.Adolescent;
import sae.decision.linguistic.model.Affectation;
import sae.decision.linguistic.model.DataManager;
import sae.decision.linguistic.model.PairingDisplay;
import sae.decision.linguistic.service.AppariementService;

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
    private TableView<PairingDisplay> tableAppariements;

    @FXML
    private TableColumn<PairingDisplay, String> colHote;

    @FXML
    private TableColumn<PairingDisplay, String> colVisiteur;

    @FXML
    private TableColumn<PairingDisplay, Number> colScore;
    
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
    
    // Services et état
    private AppariementService appariementService;
    private Affectation currentAffectation;
    private static final String HISTORY_FILE_PATH = "data/history.dat";

    @FXML
    public void initialize() {
        System.out.println("MainViewController initialisé.");
        this.appariementService = new AppariementService();
        
        // Application du style moderne à tous les boutons et composants
        setupModernStyles();
        
        // Configuration des ComboBoxes
        setupComboBoxes();
        
        // Configuration de la table
        setupTable();

        // Configuration des actions des boutons
        setupButtonActions();
    }
    
    private void setupButtonActions() {
        validerButton.setOnAction(e -> handlePairing());
        sauvegarderButton.setOnAction(e -> handleSaveHistory());
        // Le bouton chargerCSV n'est plus utile ici, l'import se fait dans StudentManager
        chargerCSVButton.setVisible(false);
    }
    
    private void handlePairing() {
        String hostCountry = comboPaysA.getValue();
        String visitorCountry = comboPaysB.getValue();

        if (hostCountry == null || visitorCountry == null || hostCountry.equals(visitorCountry)) {
            statsLabel.setText("Veuillez sélectionner deux pays différents.");
            return;
        }

        statsLabel.setText("Calcul en cours...");

        // Filtrer les adolescents par pays
        List<Adolescent> hosts = DataManager.getHosts().stream()
                .filter(ado -> hostCountry.equals(ado.getCountryOfOrigin()))
                .collect(Collectors.toList());
        List<Adolescent> visitors = DataManager.getVisitors().stream()
                .filter(ado -> visitorCountry.equals(ado.getCountryOfOrigin()))
                .collect(Collectors.toList());

        // Effectuer l'appariement avec les listes filtrées
        Affectation newAffectation = appariementService.effectuerAppariement(
            hosts,
            visitors,
            HISTORY_FILE_PATH
        );

        // Si aucun appariement n'a été généré, informer l'utilisateur et arrêter.
        if (newAffectation.getPairs().isEmpty()) {
            showInfoAlert("Aucun Appariement", "Aucun nouvel appariement n'a pu être généré pour cette sélection. " +
                          "Cela peut être dû à un nombre inégal de participants ou à des contraintes d'historique.");
            return;
        }
        
        // Récupérer l'affectation de la session et la fusionner
        Affectation sessionAffectation = DataManager.getLastAffectation();
        if (sessionAffectation == null) {
            // C'est la première affectation de la session, on la prend telle quelle
            this.currentAffectation = newAffectation;
        } else {
            // On fusionne la nouvelle affectation dans celle de la session
            sessionAffectation.merge(newAffectation);
            this.currentAffectation = sessionAffectation;
        }
        
        // Mettre à jour le DataManager. 
        // On met à null puis on remet la valeur pour forcer la notification des listeners
        // même si la référence de l'objet n'a pas changé après la fusion.
        DataManager.setLastAffectation(null);
        DataManager.setLastAffectation(this.currentAffectation);
        
        // Afficher les résultats sur le tableau de bord
        updateUIAfterPairing();
    }

    private void handleSaveHistory() {
        if (currentAffectation == null || currentAffectation.getPairs().isEmpty()) {
            statsLabel.setText("Aucune affectation à sauvegarder.");
            return;
        }

        String hostCountry = comboPaysA.getValue();
        String visitorCountry = comboPaysB.getValue();
        String year = String.valueOf(LocalDate.now().getYear());

        appariementService.sauvegarderAffectation(currentAffectation, HISTORY_FILE_PATH, year, visitorCountry, hostCountry);
        derniereMajLabel.setText("Historique sauvegardé le " + LocalDate.now());
        statsLabel.setText("Affectation sauvegardée.");
    }

    private void updateUIAfterPairing() {
        if (currentAffectation == null || currentAffectation.getPairs().isEmpty()) {
            statsLabel.setText("Aucune paire trouvée.");
            tableAppariements.getItems().clear();
            return;
        }

        List<PairingDisplay> pairingDisplays = currentAffectation.getPairs().entrySet().stream()
                .map(entry -> {
                    Adolescent visitor = entry.getKey();
                    Adolescent host = entry.getValue();
                    int score = visitor.calculateAffinity(host);
                    return new PairingDisplay(host, visitor, score);
                })
                .collect(Collectors.toList());

        tableAppariements.getItems().setAll(pairingDisplays);
        statsLabel.setText(pairingDisplays.size() + " appariement(s)");
        derniereMajLabel.setText("Dernière mise à jour : " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
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
        // Remplir les ComboBoxes avec les pays disponibles
        Set<String> hostCountries = DataManager.getHosts().stream()
                .map(Adolescent::getCountryOfOrigin)
                .collect(Collectors.toSet());
        Set<String> visitorCountries = DataManager.getVisitors().stream()
                .map(Adolescent::getCountryOfOrigin)
                .collect(Collectors.toSet());

        comboPaysA.getItems().setAll(hostCountries);
        comboPaysB.getItems().setAll(visitorCountries);
    }
    
    private void setupTable() {
        // Configuration des colonnes pour utiliser les propriétés de PairingDisplay
        colHote.setCellValueFactory(cellData -> cellData.getValue().hostNameProperty());
        colVisiteur.setCellValueFactory(cellData -> cellData.getValue().visitorNameProperty());
        colScore.setCellValueFactory(cellData -> cellData.getValue().affinityScoreProperty());

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
    
    /**
     * Affiche une boîte de dialogue d'information.
     * @param title Le titre de la fenêtre.
     * @param message Le message à afficher.
     */
    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateSelection() {
        if (comboPaysA.getValue() == null || comboPaysB.getValue() == null) {
            // ... existing code ...
        }
        return false;
    }
}
