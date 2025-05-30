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
    private static final String HOSTS_CSV_PATH = "data/sample_hosts.csv"; // Créer ces fichiers
    private static final String GUESTS_CSV_PATH = "data/sample_guests.csv";
    private static final String EXPORT_CSV_PATH = "data/exported_affectations.csv";
    private static final String HISTORY_FILE_PATH = "data/affectation_history.dat";

    public void loadInitialData() {
        System.out.println("Chargement des données initiales...");
        // S'assurer que le dossier "data" existe ou ajuster les chemins
        // Pour cet exemple, créer sample_hosts.csv et sample_guests.csv dans un sous-dossier "data"

        // Créer des fichiers CSV fictifs pour la démonstration s'ils n'existent pas
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
            currentAffectation = new Affectation(hosts, guests); // Créer quand même l'objet Affectation
            // les paires seront vides
        } else {
            currentAffectation = new Affectation(hosts, guests);
            // La méthode calculatePairing est un placeholder.
            // Pour la démonstration, nous allons créer manuellement quelques paires fictives si possible.
            Map<Adolescent, Adolescent> dummyPairs = new HashMap<>();
            // Exemple : Associer le premier visiteur avec le premier hôte si disponible
            if (!guests.isEmpty() && !hosts.isEmpty()) {
                // C'est un appariement conceptuel pour tester l'export/l'historique.
                // Dans un scénario réel, Affectation.calculatePairing() remplirait ses paires internes.
                // Pour l'instant, nous créons directement une map à passer à l'export et l'historique.
                dummyPairs.put(guests.get(0), hosts.get(0));
            }
            // Pour la démonstration, supposons que ces paires sont le résultat
            // En réalité, currentAffectation.calculatePairing() retournerait cette map
            // ou Affectation la stockerait en interne.
            // Pour l'instant, nous utiliserons cette map dummyPairs pour l'export.
            // Et nous la passerons à un "setPairs" conceptuel si Affectation en avait un, ou l'utiliserons directement.

            // Si Affectation doit stocker les paires en interne pour l'export, et calculatePairing est un placeholder :
            // currentAffectation.setPairs(dummyPairs); // En supposant une telle méthode

            // Pour l'export, nous avons juste besoin d'une Map<Adolescent, Adolescent>, en utilisant dummyPairs ici
            csvService.exportAffectations(dummyPairs, EXPORT_CSV_PATH);
            System.out.println("Affectations (potentielles) exportées vers: " + EXPORT_CSV_PATH);
        }
    }

    public void manageHistory() {
        System.out.println("Gestion de l'historique...");
        history = historyService.loadAffectationHistory(HISTORY_FILE_PATH);
        System.out.println(history.size() + " affectations chargées depuis l'historique.");

        // Afficher l'historique chargé (simplifié)
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

        // Ajouter l'affectation actuelle à l'historique (si elle existe et a des paires)
        // Pour cette démo, utilisons currentAffectation, en supposant qu'elle pourrait obtenir des paires
        // d'un calculatePairing() plus complet ou par définition manuelle pour une démo.
        if (currentAffectation != null) {
            // Et si currentAffectation.getPairs() était rempli et non vide :
            // history.put("2024_France_Germany", currentAffectation);

            // Pour la démonstration, créons une nouvelle affectation fictive à sauvegarder
            // si currentAffectation n'a peut-être pas de paires du calculatePairing placeholder.
            List<Adolescent> demoHosts = new ArrayList<>(hosts);
            List<Adolescent> demoGuests = new ArrayList<>(guests);
            if (!demoHosts.isEmpty() && !demoGuests.isEmpty()) {
                Affectation demoAffectationToSave = new Affectation(demoHosts, demoGuests);
                // Si demoAffectationToSave avait sa map de paires remplie, elle serait sauvegardée.
                // Pour l'instant, elle sauvegarde les listes d'hôtes/visiteurs.
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
        // Créer le dossier data s'il n'existe pas
        java.io.File dataDir = new java.io.File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        // Fichier CSV des hôtes exemple
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

        // Fichier CSV des visiteurs exemple
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
        app.launchAssignment(); // Cela tentera aussi d'exporter des paires fictives
        app.manageHistory();

        System.out.println("\n--- Démonstration terminée ---");
        System.out.println("Veuillez vérifier les fichiers dans le dossier 'data'.");
    }
}