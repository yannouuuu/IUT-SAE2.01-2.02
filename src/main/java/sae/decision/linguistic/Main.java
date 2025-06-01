package sae.decision.linguistic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDate;

/**
 * Classe principale de l'application de gestion des affectations linguistiques.
 * Cette classe orchestre le processus complet : chargement des données, calcul des affectations,
 * export des résultats et gestion de l'historique.
 */
public class Main {

    private CSVService csvService = new CSVService();
    private HistoryService historyService = new HistoryService();
    
    // Chemins des fichiers
    private static final String HOSTS_CSV_PATH = "ressources/sample_hosts.csv";
    private static final String GUESTS_CSV_PATH = "ressources/sample_guests.csv";
    private static final String EXPORT_CSV_PATH = "ressources/exported_affectations.csv";
    private static final String HISTORY_FILE_PATH = "ressources/affectation_history.dat";

    /**
     * Lancement de l'application.
     */
    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    /**
     * Exécute le processus d'affectation optimal.
     */
    public void run() {
        try {
            // 0. Créer les fichiers d'exemple si nécessaire sinon il s'aperçoit qu'ils existent et ne fait rien
            createSampleFilesIfNotExists();

            // 1. Charger les données depuis les fichiers CSV (d'exemples ou réel)
            System.out.println("1. Chargement des données...");
            List<Adolescent> hosts = csvService.importAdolescents(HOSTS_CSV_PATH, true);
            List<Adolescent> guests = csvService.importAdolescents(GUESTS_CSV_PATH, false);

            if (hosts.isEmpty() || guests.isEmpty()) {
                System.err.println("Erreur: Impossible de continuer sans hôtes ou visiteurs.");
                System.out.println("Vérifiez les fichiers CSV et réessayez.");
                return;
            }

            System.out.println("   - " + hosts.size() + " hôtes chargés");
            System.out.println("   - " + guests.size() + " visiteurs chargés\n");

            // 2. Créer l'affectation et calculer les paires optimales
            System.out.println("2. Calcul des affectations optimales...");
            Affectation affectation = new Affectation(hosts, guests);
            
            HashMap<Adolescent, Adolescent> pairings;
            try {
                pairings = affectation.calculatePairing();
                System.out.println("   - " + pairings.size() + " paires formées\n");
            } catch (Exception e) {
                System.err.println("Erreur lors du calcul des affectations: " + e.getMessage());
                System.out.println("Le processus va continuer avec des paires vides pour la démonstration.");
                pairings = new HashMap<>();
            }

            // 3. Afficher les résultats (Temporaire vu que JavaFX plus tard)
            displayPairings(pairings);

            // 5. Exporter les résultats
            System.out.println("3. Export des résultats...");
            try {
                csvService.exportAffectations(pairings, EXPORT_CSV_PATH);
                System.out.println("   - Résultats exportés vers: " + EXPORT_CSV_PATH + "\n");
            } catch (Exception e) {
                System.err.println("Erreur lors de l'export: " + e.getMessage());
                System.out.println("   - Export échoué, mais le processus continue\n");
            }

            // 6. Gérer l'historique
            System.out.println("4. Gestion de l'historique...");
            try {
                saveToHistory(affectation);
                System.out.println("   - Historique mis à jour\n");
            } catch (Exception e) {
                System.err.println("Erreur lors de la sauvegarde de l'historique: " + e.getMessage());
                System.out.println("   - Sauvegarde échouée\n");
            }

            System.out.println("=== Processus terminé ===");

        } catch (Exception e) {
            System.err.println("Erreur critique dans l'application: " + e.getMessage());
            System.err.println("L'application s'arrête.");
            e.printStackTrace();
        }
    }

    /**
     * Affiche les paires formées de manière lisible (Jusqu'à JavaFX).
     */
    private void displayPairings(Map<Adolescent, Adolescent> pairings) {
        System.out.println("Paires formées:");
        System.out.println("---------------");
        
        if (pairings.isEmpty()) {
            System.out.println("Aucune paire formée.");
        } else {
            try {
                for (Map.Entry<Adolescent, Adolescent> entry : pairings.entrySet()) {
                    Adolescent visitor = entry.getKey();
                    Adolescent host = entry.getValue();
                    
                    if (visitor == null || host == null) {
                        System.out.println("Paire invalide détectée (valeur null)");
                        continue;
                    }
                    
                    try {
                        int affinity = visitor.calculateAffinity(host);
                        System.out.printf("Visiteur: %s %s (%s) -> Hôte: %s %s (%s) [Affinité: %d]%n",
                            visitor.getFirstName(), visitor.getLastName(), visitor.getCountryOfOrigin(),
                            host.getFirstName(), host.getLastName(), host.getCountryOfOrigin(),
                            affinity);
                    } catch (Exception e) {
                        System.out.printf("Visiteur: %s %s (%s) -> Hôte: %s %s (%s) [Affinité: erreur de calcul]%n",
                            visitor.getFirstName(), visitor.getLastName(), visitor.getCountryOfOrigin(),
                            host.getFirstName(), host.getLastName(), host.getCountryOfOrigin());
                    }
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de l'affichage des paires: " + e.getMessage());
            }
        }
        System.out.println();
    }

    /**
     * Sauvegarde l'affectation dans l'historique.
     */
    private void saveToHistory(Affectation affectation) {
        try {
            // Charger l'historique existant
            Map<String, Affectation> history = historyService.loadAffectationHistory(HISTORY_FILE_PATH);
            
            // Créer une clé unique basée sur la date et les pays impliqués
            String historyKey = generateHistoryKey(affectation);
            
            // Ajouter l'affectation actuelle
            history.put(historyKey, affectation);
            
            // Sauvegarder l'historique mis à jour
            historyService.saveAffectationHistory(history, HISTORY_FILE_PATH);
            
            System.out.println("   - Affectation sauvegardée avec la clé: " + historyKey);
            System.out.println("   - Total d'affectations en historique: " + history.size());
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la sauvegarde dans l'historique: " + e.getMessage());
            throw e; // Relancer pour que l'appelant puisse gérer
        }
    }

    /**
     * Génère une clé unique pour l'historique basée sur la date et les pays.
     */
    private String generateHistoryKey(Affectation affectation) {
        try {
            String currentDate = LocalDate.now().toString();
            
            // Extraire les pays uniques des hôtes et visiteurs
            String hostCountries = affectation.getHosts().stream()
                .map(Adolescent::getCountryOfOrigin)
                .distinct()
                .sorted()
                .reduce((a, b) -> a + "_" + b)
                .orElse("Unknown");
                
            String visitorCountries = affectation.getVisitors().stream()
                .map(Adolescent::getCountryOfOrigin)
                .distinct()
                .sorted()
                .reduce((a, b) -> a + "_" + b)
                .orElse("Unknown");
            
            return currentDate + "_" + visitorCountries + "_to_" + hostCountries;
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération de la clé d'historique: " + e.getMessage());
            // Retourner une clé de fallback
            return LocalDate.now().toString() + "_ERROR_KEY_" + System.currentTimeMillis();
        }
    }

    /**
     * Crée les fichiers CSV d'exemple si ils n'existent pas.
     */
    private void createSampleFilesIfNotExists() {
        java.io.File dataDir = new java.io.File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        createHostsFile();
        createGuestsFile();
    }

    /**
     * Crée le fichier des hôtes d'exemple.
     */
    private void createHostsFile() {
        java.io.File hostCsv = new java.io.File(HOSTS_CSV_PATH);
        if (hostCsv.exists()) {
            return; // Le fichier existe déjà
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(hostCsv))) {
            writer.write("NAME;FORENAME;COUNTRY;BIRTH_DATE;GENDER;HOST_HAS_ANIMAL;HOST_FOOD;HOBBIES;HISTORY;PAIR_GENDER");
            writer.newLine();
            writer.write("Dupont;Jean;France;2007-05-10;male;yes;vegetarian,nonuts;football,jeux video;;male");
            writer.newLine();
            writer.write("Martin;Sophie;France;2008-02-20;female;no;;lecture,musique;same;female");
            writer.newLine();
            writer.write("Bernard;Lucas;France;2007-08-15;male;no;vegetarian;sport,cinema;;");
            writer.newLine();
            
            System.out.println("Fichier hôtes d'exemple créé: " + HOSTS_CSV_PATH);
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du fichier hôtes: " + e.getMessage());
        }
    }

    /**
     * Crée le fichier des visiteurs d'exemple.
     */
    private void createGuestsFile() {
        java.io.File guestCsv = new java.io.File(GUESTS_CSV_PATH);
        if (guestCsv.exists()) {
            return; // Le fichier existe déjà
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(guestCsv))) {
            writer.write("NAME;FORENAME;COUNTRY;BIRTH_DATE;GENDER;GUEST_ANIMAL_ALLERGY;GUEST_FOOD;HOBBIES;HISTORY;PAIR_GENDER");
            writer.newLine();
            writer.write("Schmidt;Hans;Germany;2007-06-15;male;no;vegetarian;jeux video,musique;;female");
            writer.newLine();
            writer.write("Muller;Anna;Germany;2008-03-25;female;yes;nonuts;danse,lecture;other;male");
            writer.newLine();
            writer.write("Weber;Tom;Germany;2007-12-05;male;no;;sport,cinema;;");
            writer.newLine();
            
            System.out.println("Fichier visiteurs d'exemple créé: " + GUESTS_CSV_PATH);
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du fichier visiteurs: " + e.getMessage());
        }
    }
}