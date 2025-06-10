package sae.decision.linguistic;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import sae.decision.linguistic.model.Adolescent;
import sae.decision.linguistic.model.Affectation;
import sae.decision.linguistic.service.CSVService;
import sae.decision.linguistic.service.ConfigurationService;
import sae.decision.linguistic.service.HistoryService;

/**
 * Classe principale de l'application de gestion des affectations linguistiques.
 * Cette classe orchestre le processus complet d'appariement entre hôtes et visiteurs :
 * - Chargement des données depuis les fichiers CSV
 * - Calcul des affectations optimales
 * - Export des résultats
 * - Gestion de l'historique des affectations
 */
public class Main {
    // Services utilisés
    private final CSVService csvService = new CSVService();
    private final HistoryService historyService = new HistoryService();
    
    // Constantes de configuration
    private static final String HOSTS_CSV_PATH = "src/test/resources/sample_hosts.csv";
    private static final String GUESTS_CSV_PATH = "src/test/resources/sample_guests.csv";
    private static final String EXPORT_CSV_PATH = "src/test/resources/exported_affectations.csv";
    private static final String HISTORY_FILE_PATH = "src/test/resources/affectation_history.dat";
    private static final String CONFIG_FILE_PATH = "src/test/resources/config/affinity_config.properties"; 
    private static final int MAX_DISPLAYED_PAIRS = 10;

    // Format d'affichage du tableau des résultats
    private static final String TABLE_HEADER = "Aff.| Visiteur        | Pays       | Hôte           | Pays";
    private static final String TABLE_SEPARATOR = "----+----------------+-----------+----------------+-----------";
    private static final String PAIR_FORMAT = "%3d | %-15.15s| %-10.10s | %-15.15s| %-10.10s%n";
    private static final String ERROR_PAIR_FORMAT = "ERR | %-15.15s| %-10.10s | %-15.15s| %-10.10s%n";

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

                        // 0. Charger la configuration
            System.out.println("0. Chargement de la configuration...");
            try {
                ConfigurationService.loadConfiguration(CONFIG_FILE_PATH);
                System.out.println("   - Configuration chargée depuis : " + CONFIG_FILE_PATH + "\n");
            } catch (java.io.IOException e) {
                System.err.println("   - Fichier de configuration '" + CONFIG_FILE_PATH + "' non trouvé ou illisible. Utilisation des valeurs par défaut.");
                ConfigurationService.resetToDefaults();
            }

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

            // 1.5. Charger l'historique des affectations
            Map<String, Affectation> previousAffectations = historyService.loadAffectationHistory(HISTORY_FILE_PATH);
            System.out.println(String.format("Historique des affectations chargé (%d entrées).", previousAffectations.size()));
            SetAdolescentsPreviousPartners(hosts, guests, previousAffectations);


            // 2. Créer l'affectation et calculer les paires optimales
            System.out.println("2. Calcul des affectations optimales...");
            Affectation affectation = new Affectation(hosts, guests);
            
            Map<Adolescent, Adolescent> pairings;
            try {
                pairings = affectation.calculatePairing();
                System.out.println("   - " + pairings.size() + " paires formées\n");
            } catch (Exception e) {
                System.err.println("Erreur lors du calcul des affectations: " + e.getMessage());
                System.out.println("Le processus va continuer avec des paires vides pour la démonstration.");
                pairings = new HashMap<>();
            }

            // 3. Afficher les résultats en CSV
            displayPairings(pairings);
            
            System.out.println("3. Export des résultats en csv...");

            // 4. Exporter les résultats
            csvService.exportAffectations(pairings, EXPORT_CSV_PATH);
            System.out.println("   - Résultats exportés vers: " + EXPORT_CSV_PATH);
            
            System.out.println("\n4. Gestion de l'historique...");

            // 5. Mettre à jour l'historique
            try {
                saveToHistory(affectation);
                System.out.println("   - Historique mis à jour");
            } catch (Exception e) {
                System.err.println("Erreur lors de la sauvegarde de l'historique: " + e.getMessage());
                System.out.println("   - Sauvegarde échouée");
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
     * Les paires sont triées par affinité décroissante.
     */
    private void displayPairings(Map<Adolescent, Adolescent> pairings) {
        System.out.println("Paires formées (triées par affinité décroissante):");
        System.out.println("--------------------------------------------------");
        
        if (pairings.isEmpty()) {
            System.out.println("Aucune paire formée.");
        } else {
            try {
                // Créer une liste d'entrées à partir de la Map
                List<Map.Entry<Adolescent, Adolescent>> entries = new java.util.ArrayList<>(pairings.entrySet());
                
                // Calculer les affinités une seule fois et les stocker
                Map<Map.Entry<Adolescent, Adolescent>, Integer> affinities = new HashMap<>();
                for (Map.Entry<Adolescent, Adolescent> entry : entries) {
                    try {
                        Adolescent visitor = entry.getKey();
                        Adolescent host = entry.getValue();
                        affinities.put(entry, visitor.calculateAffinity(host));
                    } catch (Exception e) {
                        affinities.put(entry, -999); // Valeur d'erreur
                    }
                }
                
                // Trier par affinité décroissante
                entries.sort((e1, e2) -> Integer.compare(affinities.getOrDefault(e2, 0), affinities.getOrDefault(e1, 0)));
                
                System.out.println("Top " + Math.min(MAX_DISPLAYED_PAIRS, entries.size()) + " paires avec la meilleure affinité:");
                System.out.println();
                
                // Format en tableau
                System.out.println(TABLE_HEADER);
                System.out.println(TABLE_SEPARATOR);
                
                for (int i = 0; i < Math.min(MAX_DISPLAYED_PAIRS, entries.size()); i++) {
                    Map.Entry<Adolescent, Adolescent> entry = entries.get(i);
                    Adolescent visitor = entry.getKey();
                    Adolescent host = entry.getValue();
                    
                    if (visitor == null || host == null) {
                        System.out.println("Paire invalide détectée (valeur null)");
                        continue;
                    }
                    
                    int affinity = affinities.getOrDefault(entry, -999);
                    String visitorFullName = visitor.getFirstName() + " " + visitor.getLastName();
                    String hostFullName = host.getFirstName() + " " + host.getLastName();
                    
                    if (affinity != -999) {
                        System.out.printf(PAIR_FORMAT,
                            affinity,
                            visitorFullName, visitor.getCountryOfOrigin(),
                            hostFullName, host.getCountryOfOrigin());
                    } else {
                        System.out.printf(ERROR_PAIR_FORMAT,
                            visitorFullName, visitor.getCountryOfOrigin(),
                            hostFullName, host.getCountryOfOrigin());
                    }
                }
                
                // Afficher un message si plus de paires existent
                if (entries.size() > MAX_DISPLAYED_PAIRS) {
                        System.out.println("\n[...] Et " + (entries.size() - MAX_DISPLAYED_PAIRS) + " autres paires non affichées.");
                    System.out.println("Toutes les paires sont disponibles dans: " + EXPORT_CSV_PATH);
                    System.out.println("\n--------------------------------------------------\n");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de l'affichage des paires: " + e.getMessage());
            }
        }
        System.out.println();
    }

    /**
     * Enrichit les objets Adolescent avec le nom complet de leur précédent partenaire
     * en se basant sur l'historique des affectations.
     * @param hosts Liste des hôtes.
     * @param guests Liste des visiteurs.
     * @param previousAffectations Historique des affectations.
     */
    private void SetAdolescentsPreviousPartners(List<Adolescent> hosts, List<Adolescent> guests,
                                                      Map<String, Affectation> previousAffectations) {
        // Créer des maps pour un accès rapide aux adolescents par leur nom complet
        Map<String, Adolescent> allAdolescents = new HashMap<>();
        hosts.forEach(h -> allAdolescents.put(h.toString(), h));
        guests.forEach(g -> allAdolescents.put(g.toString(), g));

        // Itérer sur toutes les affectations de l'historique
        for (Affectation aff : previousAffectations.values()) {
            for (Map.Entry<Adolescent, Adolescent> pair : aff.getPairs().entrySet()) {
                Adolescent hostInHistory = pair.getKey();
                Adolescent guestInHistory = pair.getValue();

                // Trouver l'adolescent correspondant dans les listes actuelles et mettre à jour son previousPartnerFullName
                Optional.ofNullable(allAdolescents.get(hostInHistory.toString()))
                        .ifPresent(currentHost -> currentHost.setPreviousPartnerFullName(guestInHistory.toString()));

                Optional.ofNullable(allAdolescents.get(guestInHistory.toString()))
                        .ifPresent(currentGuest -> currentGuest.setPreviousPartnerFullName(hostInHistory.toString()));
            }
        }
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
            throw e; // Relancer pour que l'utilisateur puisse gérer
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
}