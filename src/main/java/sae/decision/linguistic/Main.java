package sae.decision.linguistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class Main {

    private List<Adolescent> hosts = new ArrayList<>();
    private List<Adolescent> guests = new ArrayList<>();
    private Affectation currentAffectation;
    private Map<String, Affectation> history;

    private CSVService csvService = new CSVService();
    private HistoryService historyService = new HistoryService();
    private static final String HOSTS_CSV_PATH = "data/sample_hosts.csv"; // Create these files
    private static final String GUESTS_CSV_PATH = "data/sample_guests.csv";
    private static final String EXPORT_CSV_PATH = "data/exported_affectations.csv";
    private static final String HISTORY_FILE_PATH = "data/affectation_history.dat";

    public void loadInitialData() {
        System.out.println("Chargement des données initiales...");
        // Ensure the "data" directory exists or adjust paths
        // For this example, create sample_hosts.csv and sample_guests.csv in a "data" subdirectory

        // Create dummy CSV files for demonstration if they don't exist
        createSampleCsvFilesIfNotExists();

        hosts = csvService.importAdolescents(HOSTS_CSV_PATH, true);
        guests = csvService.importAdolescents(GUESTS_CSV_PATH, false);

        System.out.println(hosts.size() + " hôtes chargés.");
        for(int i=0; i<hosts.size(); i++) {
            System.out.println("- " + hosts.get(i));
        }
        System.out.println(guests.size() + " visiteurs chargés.");
        for(int i=0; i<guests.size(); i++) {
            System.out.println("- " + guests.get(i));
        }
    }

    public void launchAssignment() {
        System.out.println("Lancement du calcul de l'affectation...");
        if (hosts.isEmpty() || guests.isEmpty()) {
            System.out.println("Pas assez d'hôtes ou de visiteurs pour former des paires.");
            currentAffectation = new Affectation(hosts, guests); // Still create Affectation object
            // pairs will be empty
        } else {
            currentAffectation = new Affectation(hosts, guests);
            // The calculatePairing method is a placeholder.
            // For demonstration, we'll manually create some dummy pairs if possible.
            Map<Adolescent, Adolescent> dummyPairs = new HashMap<>();
            // Example: Pair the first visitor with the first host if available
            if (!guests.isEmpty() && !hosts.isEmpty()) {
                // This is a conceptual pairing for testing export/history.
                // In a real scenario, Affectation.calculatePairing() would populate its internal pairs.
                // For now, we are directly creating a map to pass to export and history.
                dummyPairs.put(guests.get(0), hosts.get(0));
            }
            // For demonstration, let's assume these pairs are the result
            // In reality, currentAffectation.calculatePairing() would return this map
            // or Affectation would internally store it.
            // For now, we will use this dummyPairs map for export.
            // And we will pass it to a conceptual "setPairs" if Affectation had one, or use it directly.

            // If Affectation needs to store pairs internally for export, and calculatePairing is a placeholder:
            // currentAffectation.setPairs(dummyPairs); // Assuming such a method

            // For export, we just need a Map<Adolescent, Adolescent>, using dummyPairs here
            csvService.exportAffectations(dummyPairs, EXPORT_CSV_PATH);
            System.out.println("Affectations (potentielles) exportées vers: " + EXPORT_CSV_PATH);
        }
    }

    public void manageHistory() {
        System.out.println("Gestion de l'historique...");
        history = historyService.loadAffectationHistory(HISTORY_FILE_PATH);
        System.out.println(history.size() + " affectations chargées depuis l'historique.");

        // Display loaded history (simplified)
        if (!history.isEmpty()) {
            System.out.println("Détails de l'historique chargé:");
            List<Map.Entry<String, Affectation>> entryList = new ArrayList<>(history.entrySet());
            for (int i = 0; i < entryList.size(); i++) {
                Map.Entry<String, Affectation> entry = entryList.get(i);
                System.out.println("Clé Historique: " + entry.getKey());
                Affectation pastAff = entry.getValue();
                System.out.println("  Hôtes: " + pastAff.getHosts().size() + ", Visiteurs: " + pastAff.getVisitors().size());
            }
        }


        // Add current affectation to history (if it exists and has pairs)
        // For this demo, let's use currentAffectation, assuming it might get pairs
        // from a more complete calculatePairing() or by manual setting for a demo.
        if (currentAffectation != null) {
            // And if currentAffectation.getPairs() was populated and not empty:
            // history.put("2024_France_Germany", currentAffectation);

            // For demonstration, let's create a new dummy affectation to save
            // if currentAffectation might not have pairs from the placeholder calculatePairing.
            List<Adolescent> demoHosts = new ArrayList<>(hosts);
            List<Adolescent> demoGuests = new ArrayList<>(guests);
            if (!demoHosts.isEmpty() && !demoGuests.isEmpty()) {
                Affectation demoAffectationToSave = new Affectation(demoHosts, demoGuests);
                // If demoAffectationToSave had its pairs map populated, it would be saved.
                // For now, it saves the lists of hosts/guests.
                history.put("DEMO_2024_FR_DE", demoAffectationToSave);
                System.out.println("Ajout de l'affectation actuelle (démo) à l'historique.");
            }
        } else {
            System.out.println("Aucune affectation actuelle à ajouter à l'historique (currentAffectation est null).");
        }


        historyService.saveAffectationHistory(history, HISTORY_FILE_PATH);
        System.out.println("Historique sauvegardé dans: " + HISTORY_FILE_PATH);
    }

    private void createSampleCsvFilesIfNotExists() {
        // Create data directory if it doesn't exist
        java.io.File dataDir = new java.io.File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        // Sample Hosts CSV
        try {
            java.io.File hostCsv = new java.io.File(HOSTS_CSV_PATH);
            if (!hostCsv.exists()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(hostCsv));
                writer.write("NAME;FORENAME;COUNTRY;BIRTH_DATE;GENDER;HOST_HAS_ANIMAL;HOST_FOOD;HOBBIES;HISTORY;PAIR_GENDER");
                writer.newLine();
                writer.write("Dupont;Jean;France;2007-05-10;male;yes;vegetarian,nonuts;football,jeux video;;male");
                writer.newLine();
                writer.write("Martin;Sophie;France;2008-02-20;female;no;;lecture,musique;same;");
                writer.newLine();
                writer.close();
                System.out.println("Créé fichier hôtes exemples: " + HOSTS_CSV_PATH);
            }
        } catch (IOException e) {
            System.err.println("Impossible de créer le fichier hôtes exemples: " + e.getMessage());
        }

        // Sample Guests CSV
        try {
            java.io.File guestCsv = new java.io.File(GUESTS_CSV_PATH);
            if (!guestCsv.exists()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(guestCsv));
                writer.write("NAME;FORENAME;COUNTRY;BIRTH_DATE;GENDER;GUEST_ANIMAL_ALLERGY;GUEST_FOOD;HOBBIES;HISTORY;PAIR_GENDER");
                writer.newLine();
                writer.write("Schmidt;Hans;Germany;2007-06-15;male;no;vegetarian;jeux video,musique;;female");
                writer.newLine();
                writer.write("Muller;Anna;Germany;2008-03-25;female;yes;nonuts;danse,lecture;other;male");
                writer.newLine();
                writer.close();
                System.out.println("Créé fichier visiteurs exemples: " + GUESTS_CSV_PATH);
            }
        } catch (IOException e) {
            System.err.println("Impossible de créer le fichier visiteurs exemples: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        Main app = new Main();
        app.loadInitialData();
        app.launchAssignment(); // This will also attempt to export dummy pairings
        app.manageHistory();

        System.out.println("\n--- Démonstration terminée ---");
        System.out.println("Veuillez vérifier les fichiers dans le dossier 'data'.");
    }
}