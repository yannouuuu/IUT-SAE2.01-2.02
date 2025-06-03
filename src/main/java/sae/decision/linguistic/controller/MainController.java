package sae.decision.linguistic.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sae.decision.linguistic.model.Adolescent;
import sae.decision.linguistic.model.Affectation; // Pourra être utilisé plus tard faut check la doc
import sae.decision.linguistic.service.CSVService;
import sae.decision.linguistic.view.AppFX.PairingDisplayEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MainController {

    private CSVService csvService;
    // Références aux UI Elements qui seront passées depuis AppFX ou initialisées
    private TableView<PairingDisplayEntry> tableView;
    private ComboBox<String> comboPaysA;
    private ComboBox<String> comboPaysB;
    private Button btnChargerCsv;
    private Button btnValiderPays;

    private Stage primaryStage; // Nécessaire pour le FileChooser (oui, c'est un peu bizarre)

    private ObservableList<Adolescent> hostsList = FXCollections.observableArrayList();
    private ObservableList<Adolescent> guestsList = FXCollections.observableArrayList();
    private ObservableList<PairingDisplayEntry> pairingItemsList = FXCollections.observableArrayList();

    public MainController(Stage primaryStage) {
        this.csvService = new CSVService();
        this.primaryStage = primaryStage;
    }

    // Méthode pour initialiser les références aux contrôles de l'UI
    public void initializeControls(
            TableView<PairingDisplayEntry> tableView,
            ComboBox<String> comboPaysA, ComboBox<String> comboPaysB,
            Button btnChargerCsv, Button btnValiderPays
            // Ajouter d'autres boutons/contrôles ici si nécessaire dans le futur
    ) {
        this.tableView = tableView;
        this.tableView.setItems(pairingItemsList); // Lier la liste observable au TableView

        this.comboPaysA = comboPaysA;
        this.comboPaysB = comboPaysB;
        this.btnChargerCsv = btnChargerCsv;
        this.btnValiderPays = btnValiderPays;

        this.btnChargerCsv.setOnAction(event -> handleChargerCsv());
        this.btnValiderPays.setOnAction(event -> handleValiderOuApparier());
        // Initialiser les actions des autres boutons ici
    }

    private void handleChargerCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner les fichiers CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv"));

        // Charger le fichier des hôtes
        File hostsFile = fileChooser.showOpenDialog(primaryStage);
        if (hostsFile != null) {
            try {
                List<Adolescent> loadedHosts = csvService.importAdolescents(hostsFile.getAbsolutePath(), true);
                hostsList.setAll(loadedHosts);
                System.out.println(hostsList.size() + " hôtes chargés.");
                updateCountryComboBoxes(); // Mettre à jour les combobox avec les pays des hôtes
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement du fichier des hôtes: " + e.getMessage());
            }
        }

        // Charger le fichier des visiteurs
        File guestsFile = fileChooser.showOpenDialog(primaryStage);
        if (guestsFile != null) {
            try {
                List<Adolescent> loadedGuests = csvService.importAdolescents(guestsFile.getAbsolutePath(), false);
                guestsList.setAll(loadedGuests);
                System.out.println(guestsList.size() + " visiteurs chargés.");
                updateCountryComboBoxes(); // Mettre à jour aussi avec les pays des visiteurs
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement du fichier des visiteurs: " + e.getMessage());
            }
        }
        // Optionnel: Déclencher l'appariement automatiquement ou attendre le bouton Valider/Apparier
        if (!hostsList.isEmpty() && !guestsList.isEmpty()) {
            // handleValiderOuApparier(); // A LAISSER COMMENTÉ POUR L'INSTANT
        }
    }
    
    private void updateCountryComboBoxes() {
        Set<String> hostCountries = hostsList.stream()
                                            .map(Adolescent::getCountryOfOrigin)
                                            .collect(Collectors.toSet());
        Set<String> guestCountries = guestsList.stream()
                                            .map(Adolescent::getCountryOfOrigin)
                                            .collect(Collectors.toSet());

        Set<String> allCountries = new java.util.HashSet<>(hostCountries);
        allCountries.addAll(guestCountries);
        
        ObservableList<String> countriesObservable = FXCollections.observableArrayList(allCountries);
        FXCollections.sort(countriesObservable);

        String currentValA = comboPaysA.getValue();
        String currentValB = comboPaysB.getValue();

        comboPaysA.setItems(countriesObservable);
        comboPaysB.setItems(countriesObservable);

        if (currentValA != null && allCountries.contains(currentValA)) {
            comboPaysA.setValue(currentValA);
        } else if (!countriesObservable.isEmpty()) {
            comboPaysA.setValue(countriesObservable.get(0));
        }

        if (currentValB != null && allCountries.contains(currentValB)) {
            comboPaysB.setValue(currentValB);
        } else if (countriesObservable.size() > 1) {
            comboPaysB.setValue(countriesObservable.get(1));
        } else if (!countriesObservable.isEmpty()){
            comboPaysB.setValue(countriesObservable.get(0));
        }
    }

    private void handleValiderOuApparier() {
        if (hostsList.isEmpty() || guestsList.isEmpty()) {
            System.out.println("Veuillez d'abord charger les fichiers des hôtes et des visiteurs.");
            return;
        }

        Affectation affectation = new Affectation(new ArrayList<>(hostsList), new ArrayList<>(guestsList)); // Utiliser les listes filtrées si implémenté
        try {
            Map<Adolescent, Adolescent> pairings = affectation.calculatePairing();
            pairingItemsList.clear();
            for (Map.Entry<Adolescent, Adolescent> entry : pairings.entrySet()) {
                Adolescent guest = entry.getKey();
                Adolescent host = entry.getValue();
                int score = guest.calculateAffinity(host);
                // Pour le statut,  "✓" par défaut pour l'instant
                // la logique isCompatible est déjà dans calculateAffinity (qui retourne 0 si pas compatible)
                // Une meilleure gestion du statut pourrait être ajoutée pour le coup.
                String status = score > 0 ? "✓" : "✗"; 
                pairingItemsList.add(new PairingDisplayEntry(status, host.toString(), guest.toString(), String.valueOf(score)));
            }
            System.out.println(pairingItemsList.size() + " paires affichées.");
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul ou de l'affichage des affectations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Implémenter les handlers pour les autres boutons (Sauvegarder, Réglages, etc.)
    // public void handleSauvegarderResultat() { ... }
    // public void handleReglages() { ... }
    // public void handleVoirDetails() { ... }
    // public void handleGestionAppariements() { ... }
} 