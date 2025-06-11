package sae.decision.linguistic;

import sae.decision.linguistic.model.Adolescent;
import sae.decision.linguistic.model.AffinityBreakdown;
import sae.decision.linguistic.model.Affectation;
import sae.decision.linguistic.service.AppariementService;
import sae.decision.linguistic.service.CSVService;
import sae.decision.linguistic.service.ConfigurationService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Classe principale pour l'utilisation non-graphique (terminal) de l'application.
 * Fournit un menu interactif avancé pour une expérience utilisateur complète.
 */
public class TerminalApp {

    // --- Utilitaires pour l'esthétique ---
    private record AnsiColors(String code) {
        public static final AnsiColors RESET = new AnsiColors("\u001B[0m");
        public static final AnsiColors RED = new AnsiColors("\u001B[31m");
        public static final AnsiColors GREEN = new AnsiColors("\u001B[32m");
        public static final AnsiColors YELLOW = new AnsiColors("\u001B[33m");
        public static final AnsiColors CYAN_BOLD = new AnsiColors("\u001B[1;36m");
        public static final AnsiColors MAGENTA = new AnsiColors("\u001B[35m");
    }

    private final Scanner scanner = new Scanner(System.in);
    private final AppariementService appariementService = new AppariementService();
    private final CSVService csvService = new CSVService();

    private List<Adolescent> loadedHosts;
    private List<Adolescent> loadedVisitors;
    private Affectation lastAffectation;
    private String resourceBasePath = "src/test/resources/";

    public static void main(String[] args) {
        TerminalApp app = new TerminalApp();
        app.run();
    }

    public void run() {
        clearScreen();
        printBoxedTitle("Application d'Appariement Linguistique");
        loadConfiguration();
        promptForBasePath();
        
        System.out.println("\nChargement initial des données...");
        handleChangeDataFiles(true);

        while (true) {
            displayMenu();
            System.out.print(AnsiColors.YELLOW.code + "Votre choix : " + AnsiColors.RESET.code);
            String choice = scanner.nextLine();

            clearScreen();
            switch (choice) {
                case "1" -> handleFullPairingProcess();
                case "2" -> handleSpecificPairing();
                case "3" -> handleManualPairing();
                case "4" -> handleDetailedComparison();
                case "5" -> handleChangeDataFiles(false);
                case "6" -> handleViewConfiguration();
                case "7" -> handleModifyConfiguration();
                case "8" -> {
                    System.out.println(AnsiColors.CYAN_BOLD.code + "Au revoir !"+ AnsiColors.RESET.code);
                    return;
                }
                default -> System.out.println(AnsiColors.RED.code + "❌ Choix invalide. Veuillez réessayer." + AnsiColors.RESET.code);
            }
            if (!choice.equals("8")) {
                 System.out.print("\nAppuyez sur Entrée pour continuer...");
                 scanner.nextLine();
            }
        }
    }

    private void displayMenu() {
        clearScreen();
        printBoxedTitle("MENU PRINCIPAL");
        System.out.println("1. Lancer l'appariement global");
        System.out.println("2. Lancer un appariement spécifique (par pays)");
        System.out.println("3. " + AnsiColors.MAGENTA.code + "Gérer les appariements (Forcer/Supprimer)" + AnsiColors.RESET.code);
        System.out.println("4. Comparer en détail deux adolescents");
        System.out.println("5. Changer les fichiers de données (Hôtes/Visiteurs)");
        System.out.println("6. Afficher la configuration actuelle");
        System.out.println("7. Modifier un paramètre de configuration");
        System.out.println("8. Quitter");
    }
    
    private void addManualPair() {
        printSectionTitle("Forcer un Appariement Manuel");
    
        // 1. Sélection libre de n'importe quel adolescent
        Adolescent selectedVisitor = selectAdolescent(loadedVisitors, "visiteur");
        Adolescent selectedHost = selectAdolescent(loadedHosts, "hôte");
    
        Map<Adolescent, Adolescent> currentPairs = lastAffectation.getPairs();
        
        // 2. Trouver les partenaires actuels des ados sélectionnés (s'ils existent)
        Adolescent evictedHost = currentPairs.get(selectedVisitor);
        Adolescent evictedVisitor = currentPairs.entrySet().stream()
                .filter(entry -> selectedHost.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);
    
        // 3. Libération : Supprimer les anciennes paires impliquant les ados sélectionnés
        if (evictedHost != null) {
            currentPairs.remove(selectedVisitor);
            System.out.println(AnsiColors.YELLOW.code + "Libération : " + evictedHost + " (précédent hôte de " + selectedVisitor + ") est maintenant disponible." + AnsiColors.RESET.code);
        }
        if (evictedVisitor != null) {
            currentPairs.remove(evictedVisitor);
            System.out.println(AnsiColors.YELLOW.code + "Libération : " + evictedVisitor + " (précédent visiteur de " + selectedHost + ") est maintenant disponible." + AnsiColors.RESET.code);
        }
        
        // 4. Forçage de la nouvelle paire
        currentPairs.put(selectedVisitor, selectedHost);
        System.out.println(AnsiColors.GREEN.code + "\n✅ Paire forcée : " + selectedVisitor + " est maintenant avec " + selectedHost + "." + AnsiColors.RESET.code);
    
        System.out.println("\nLancement de la ré-optimisation pour les adolescents restants...");
        
        // Création des listes d'ados disponibles pour le nouvel appariement
        List<Adolescent> availableVisitors = loadedVisitors.stream()
                .filter(v -> !currentPairs.containsKey(v))
                .collect(Collectors.toList());
        List<Adolescent> availableHosts = loadedHosts.stream()
                .filter(h -> !currentPairs.containsValue(h))
                .collect(Collectors.toList());
    
        if (availableVisitors.isEmpty() || availableHosts.isEmpty()) {
            System.out.println(AnsiColors.YELLOW.code + "Aucun adolescent restant à ré-apparier." + AnsiColors.RESET.code);
        } else {
            System.out.printf("Calcul pour %d visiteurs et %d hôtes disponibles...\n", availableVisitors.size(), availableHosts.size());
            Affectation tempAffectation = new Affectation(availableHosts, availableVisitors);
            Map<Adolescent, Adolescent> newOptimalPairs = tempAffectation.calculatePairing(); //
            
            // Ajout des nouvelles paires optimisées à la liste principale
            currentPairs.putAll(newOptimalPairs);
            System.out.println(AnsiColors.GREEN.code + "✅ Ré-optimisation terminée. " + newOptimalPairs.size() + " nouvelles paires formées." + AnsiColors.RESET.code);
        }
    }
    
    private void handleManualPairing() {
        printSectionTitle("Gestion Manuelle des Appariements");
        if (lastAffectation == null) {
            System.out.println(AnsiColors.YELLOW.code + "⚠️ Veuillez lancer un appariement (option 1) avant de gérer les paires." + AnsiColors.RESET.code);
            return;
        }

        System.out.println("1. " + AnsiColors.MAGENTA.code + "Forcer/Ajouter une paire (avec ré-optimisation)" + AnsiColors.RESET.code);
        System.out.println("2. Supprimer une paire existante");
        System.out.println("3. Retour au menu principal");
        System.out.print(AnsiColors.YELLOW.code + "Votre choix : " + AnsiColors.RESET.code);
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> addManualPair();
            case "2" -> removePair();
            default -> { return; }
        }
        
        System.out.println("\nÉtat actuel des paires après modification :");
        displayPairingsSummary(lastAffectation.getPairs());
    }

    private void removePair() {
        printSectionTitle("Suppression d'une Paire");
        Map<Adolescent, Adolescent> paired = lastAffectation.getPairs();
        if (paired.isEmpty()) {
            System.out.println(AnsiColors.YELLOW.code + "⚠️ Aucune paire à supprimer." + AnsiColors.RESET.code);
            return;
        }

        List<Map.Entry<Adolescent, Adolescent>> pairList = new ArrayList<>(paired.entrySet());
        System.out.println("Paires actuelles :");
        for (int i = 0; i < pairList.size(); i++) {
            System.out.printf("%d: %s <> %s\n", i, pairList.get(i).getKey(), pairList.get(i).getValue());
        }

        System.out.print(AnsiColors.YELLOW.code + "Choisissez l'index de la paire à supprimer : " + AnsiColors.RESET.code);
        try {
            int index = Integer.parseInt(scanner.nextLine());
            Map.Entry<Adolescent, Adolescent> toRemove = pairList.get(index);
            paired.remove(toRemove.getKey());
            System.out.println(AnsiColors.GREEN.code + "✅ Paire supprimée. " + toRemove.getKey() + " et " + toRemove.getValue() + " sont maintenant disponibles." + AnsiColors.RESET.code);
            System.out.println(AnsiColors.YELLOW.code + "Note : Lancez un appariement global (option 1) pour réintégrer ces adolescents dans une solution optimale." + AnsiColors.RESET.code);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println(AnsiColors.RED.code + "❌ Index invalide." + AnsiColors.RESET.code);
        }
    }

    // --- FONCTIONS EXISTANTES (adaptées) ---
    private void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            // En cas d'erreur, imprimer quelques lignes vides pour simuler
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    private void handleFullPairingProcess() {
        printSectionTitle("Lancement de l'Appariement Global");
        if (loadedHosts == null || loadedVisitors == null) {
            System.out.println(AnsiColors.YELLOW.code + "⚠️ Données non chargées. Veuillez utiliser l'option 5." + AnsiColors.RESET.code);
            return;
        }
        try {
            String historyPath = promptForFilePath("Chemin du fichier d'historique (.dat)", "affectation_history.dat");
            String exportPath = promptForFilePath("Chemin du fichier d'export CSV", "exported_affectations.csv");

            System.out.printf("\n1. Calcul en cours pour %d visiteurs et %d hôtes...\n", loadedVisitors.size(), loadedHosts.size());
            this.lastAffectation = appariementService.effectuerAppariement(loadedHosts, loadedVisitors, historyPath); //

            if (lastAffectation.getPairs().isEmpty()) { //
                System.out.println(AnsiColors.YELLOW.code + "   - Aucune paire n'a pu être formée."+ AnsiColors.RESET.code);
            } else {
                System.out.printf(AnsiColors.GREEN.code + "   - %d paires formées.\n" + AnsiColors.RESET.code, lastAffectation.getPairs().size()); //
                displayPairingsSummary(lastAffectation.getPairs());
            }

            System.out.println("2. Exportation des résultats...");
            appariementService.exporterAffectation(lastAffectation, exportPath); //
            System.out.println(AnsiColors.GREEN.code + "   - Exportation réussie vers " + exportPath + AnsiColors.RESET.code);

            System.out.println("3. Sauvegarde dans l'historique...");
            appariementService.sauvegarderAffectation(lastAffectation, historyPath, "2025", "MULTI", "FRANCE"); //
            System.out.println(AnsiColors.GREEN.code + "   - Historique mis à jour dans " + historyPath + AnsiColors.RESET.code);

            System.out.println(AnsiColors.GREEN.code + "\n✅ Processus terminé avec succès !" + AnsiColors.RESET.code);

        } catch (Exception e) {
            System.err.println(AnsiColors.RED.code + "❌ Une erreur est survenue durant le processus : " + e.getMessage() + AnsiColors.RESET.code);
        }
    }

    private void loadConfiguration() {
        try {
            String configFile = "src/test/resources/config/affinity_config.properties";
            ConfigurationService.loadConfiguration(configFile); //
            System.out.println(AnsiColors.GREEN.code + "✅ Configuration chargée depuis '" + configFile + "'" + AnsiColors.RESET.code);
        } catch (IOException e) {
            System.out.println(AnsiColors.YELLOW.code + "⚠️ Fichier de configuration non trouvé. Utilisation des valeurs par défaut." + AnsiColors.RESET.code);
            ConfigurationService.resetToDefaults(); //
        }
    }

    private void promptForBasePath() {
        System.out.print("\n" + AnsiColors.YELLOW.code + "Entrez le chemin du dossier des ressources [" + this.resourceBasePath + "]: " + AnsiColors.RESET.code);
        String inputPath = scanner.nextLine();
        if (!inputPath.isBlank()) {
            this.resourceBasePath = inputPath.endsWith(File.separator) ? inputPath : inputPath + File.separator;
        }
    }

    private String promptForFilePath(String fileDescription, String defaultFileName) {
        String defaultPath = this.resourceBasePath + defaultFileName;
        System.out.print(AnsiColors.YELLOW.code + fileDescription + " [" + defaultPath + "]: " + AnsiColors.RESET.code);
        String inputPath = scanner.nextLine();
        return inputPath.isBlank() ? defaultPath : inputPath;
    }
    
    private void handleDetailedComparison() {
        printSectionTitle("Comparaison Détaillée d'Affinité");
        if (loadedVisitors == null || loadedHosts == null) {
            System.out.println(AnsiColors.YELLOW.code + "⚠️ Veuillez d'abord lancer un processus d'appariement (option 1) pour charger les données." + AnsiColors.RESET.code);
            return;
        }

        try {
            Adolescent visitor = selectAdolescent(loadedVisitors, "visiteur");
            Adolescent host = selectAdolescent(loadedHosts, "hôte");

            AffinityBreakdown details = visitor.calculateAffinityDetails(host); //

            System.out.println("\n--- Rapport d'Affinité ---");
            System.out.printf("Entre Visiteur: %s ET Hôte: %s\n", visitor, host);
            System.out.println("------------------------------------");
            System.out.printf("Score Final d'Affinité : " + AnsiColors.CYAN_BOLD.code + "%d / 100\n" + AnsiColors.RESET.code, details.getFinalScore());
            System.out.println("------------------------------------");

            System.out.println("\nScores par composant :");
            details.getComponentScores().forEach((key, value) -> //
                System.out.printf("  - %-20s : %.2f\n", key, value));

            System.out.println("\nVérifications de compatibilité stricte :");
            details.getCompatibilityChecks().forEach((key, value) -> //
                System.out.printf("  - %-20s : %s\n", key, value ? AnsiColors.GREEN.code + "✅ Compatible" : AnsiColors.RED.code + "❌ Incompatible" + AnsiColors.RESET.code));
            System.out.println("------------------------------------");

        } catch (Exception e) {
            System.err.println(AnsiColors.RED.code + "❌ Erreur : " + e.getMessage() + AnsiColors.RESET.code);
        }
    }
    
    private Adolescent selectAdolescent(List<Adolescent> list, String type) {
        System.out.println("\nListe des " + type + "s :");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d: %s\n", i, list.get(i).toString());
        }
        while (true) {
            System.out.print(AnsiColors.YELLOW.code + "Choisissez l'index du " + type + " : " + AnsiColors.RESET.code);
            try {
                int index = Integer.parseInt(scanner.nextLine());
                return list.get(index); // Lève une IndexOutOfBoundsException si invalide
            } catch (NumberFormatException e) {
                System.out.println(AnsiColors.RED.code + "Erreur : Veuillez entrer un nombre." + AnsiColors.RESET.code);
            } catch (IndexOutOfBoundsException e) {
                 System.out.println(AnsiColors.RED.code + "Erreur : L'index est invalide." + AnsiColors.RESET.code);
            }
        }
    }

    private void handleViewConfiguration() {
        printSectionTitle("Configuration Actuelle");
        Map<String, Object> config = ConfigurationService.getAllConfiguration(); //
        config.forEach((key, value) -> System.out.printf(AnsiColors.CYAN_BOLD.code + "%-35s" + AnsiColors.RESET.code + " = %s\n", key, value.toString()));
    }
    
    private void handleModifyConfiguration() {
        printSectionTitle("Modification de la Configuration");
        System.out.print(AnsiColors.YELLOW.code + "Entrez la clé de configuration à modifier (ex: weight.hobbies) : " + AnsiColors.RESET.code);
        String key = scanner.nextLine();
    
        Map<String, Object> config = ConfigurationService.getAllConfiguration();
        if (!config.containsKey(key)) {
            System.out.println(AnsiColors.RED.code + "❌ Clé de configuration inconnue." + AnsiColors.RESET.code);
            return;
        }
    
        Object originalValue = config.get(key);
        
        if (originalValue instanceof Double) {
            modifyDoubleValue(key);
        } else if (originalValue instanceof Boolean) {
            modifyBooleanValue(key);
        } else {
            modifyStringValue(key); // Pour les autres types, on les traite comme String
        }
    }

    private void modifyDoubleValue(String key) {
        while (true) {
            System.out.print(AnsiColors.YELLOW.code + "Entrez la nouvelle valeur numérique pour '" + key + "' : " + AnsiColors.RESET.code);
            try {
                double newValue = Double.parseDouble(scanner.nextLine());
                ConfigurationService.setValue(key, newValue); //
                System.out.println(AnsiColors.GREEN.code + "✅ Valeur mise à jour." + AnsiColors.RESET.code);
                break;
            } catch (NumberFormatException e) {
                System.out.println(AnsiColors.RED.code + "Erreur : La valeur doit être un nombre (ex: 1.5 ou 10)." + AnsiColors.RESET.code);
            }
        }
    }
    
    private void modifyBooleanValue(String key) {
        while (true) {
            System.out.print(AnsiColors.YELLOW.code + "Entrez la nouvelle valeur (true/false) pour '" + key + "' : " + AnsiColors.RESET.code);
            String input = scanner.nextLine().toLowerCase().trim();
            if (input.equals("true") || input.equals("false")) {
                ConfigurationService.setValue(key, Boolean.parseBoolean(input)); //
                System.out.println(AnsiColors.GREEN.code + "✅ Valeur mise à jour." + AnsiColors.RESET.code);
                break;
            } else {
                System.out.println(AnsiColors.RED.code + "Erreur : La valeur doit être 'true' ou 'false'." + AnsiColors.RESET.code);
            }
        }
    }

    private void modifyStringValue(String key) {
        System.out.print(AnsiColors.YELLOW.code + "Entrez la nouvelle valeur texte pour '" + key + "' : " + AnsiColors.RESET.code);
        String newValue = scanner.nextLine();
        ConfigurationService.setValue(key, newValue);
        System.out.println(AnsiColors.GREEN.code + "✅ Valeur mise à jour." + AnsiColors.RESET.code);
    }

    private void displayPairingsSummary(Map<Adolescent, Adolescent> pairings) {
        System.out.println("\n--- Résumé des Paires ---");
        String headerFormat = "| %-20s | %-20s | %-8s |\n";
        String rowFormat = "| %-20.20s | %-20.20s | " + AnsiColors.CYAN_BOLD.code + "%-8d" + AnsiColors.RESET.code + " |\n";
        
        System.out.format(headerFormat, "VISITEUR", "HÔTE", "AFFINITÉ");
        System.out.println(new String(new char[58]).replace('\0', '-'));
        
        pairings.entrySet().stream()
            .limit(15)
            .forEach(entry -> {
                int score = entry.getKey().calculateAffinity(entry.getValue()); //
                System.out.format(rowFormat, entry.getKey(), entry.getValue(), score);
            });
        
        if (pairings.size() > 15) {
            System.out.printf("[... et %d autres paires]\n", pairings.size() - 15);
        }
    }

    private void printSectionTitle(String title) {
        System.out.println("\n" + AnsiColors.CYAN_BOLD.code + "--- " + title.toUpperCase() + " ---" + AnsiColors.RESET.code);
    }
    
    private void printBoxedTitle(String title) {
        String border = "═".repeat(title.length() + 4);
        System.out.println(AnsiColors.CYAN_BOLD.code);
        System.out.println("╔" + border + "╗");
        System.out.println("║  " + title + "  ║");
        System.out.println("╚" + border + "╝");
        System.out.println(AnsiColors.RESET.code);
    }
}