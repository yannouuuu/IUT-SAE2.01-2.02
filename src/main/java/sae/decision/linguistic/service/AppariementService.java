package sae.decision.linguistic.service;

import java.util.List;
import java.util.Map;

import sae.decision.linguistic.model.Adolescent;
import sae.decision.linguistic.model.Affectation;

/**
 * Service d'orchestration pour le processus d'affectation des adolescents.
 * Ce service utilise CSVService pour charger les données des adolescents,
 * HistoryService pour gérer les affectations passées, et le modèle Affectation
 * pour calculer les nouveaux appariements.
 */
public class AppariementService {

    private final CSVService csvService;
    private final HistoryService historyService;

    /**
     * Construit une nouvelle instance de AppariementService.
     */
    public AppariementService() {
        this.csvService = new CSVService();
        this.historyService = new HistoryService();
    }

    /**
     * Exécute le processus complet d'affectation à partir de fichiers CSV.
     *
     * @param hostsFilePath    Chemin vers le fichier CSV des hôtes.
     * @param visitorsFilePath Chemin vers le fichier CSV des visiteurs.
     * @param historyFilePath  Chemin vers le fichier d'historique.
     * @return L'objet Affectation contenant les paires calculées.
     */
    public Affectation effectuerAppariement(String hostsFilePath, String visitorsFilePath, String historyFilePath) {
        // 1. Charger les données
        List<Adolescent> hosts = csvService.importAdolescents(hostsFilePath, true);
        List<Adolescent> visitors = csvService.importAdolescents(visitorsFilePath, false);

        return effectuerAppariement(hosts, visitors, historyFilePath);
    }
    
    /**
     * Exécute le processus d'affectation à partir de listes d'adolescents.
     *
     * @param hosts           Liste des hôtes.
     * @param visitors        Liste des visiteurs.
     * @param historyFilePath Chemin vers le fichier d'historique.
     * @return L'objet Affectation contenant les paires calculées.
     */
    public Affectation effectuerAppariement(List<Adolescent> hosts, List<Adolescent> visitors, String historyFilePath) {
        Map<String, Affectation> history = historyService.loadAffectationHistory(historyFilePath);

        // Si les listes sont vides, on retourne une affectation vide pour éviter un crash
        if (hosts.isEmpty() || visitors.isEmpty()) {
            System.err.println("Attention: La liste des hôtes ou des visiteurs est vide. Impossible de procéder à l'affectation.");
            return new Affectation(hosts, visitors); 
        }

        // 2. Créer l'objet Affectation et calculer les paires
        Affectation affectation = new Affectation(hosts, visitors);
        affectation.calculatePairing(history);

        return affectation;
    }

    /**
     * Sauvegarde une nouvelle affectation dans l'historique.
     *
     * @param affectation       L'affectation à sauvegarder.
     * @param historyFilePath   Chemin vers le fichier d'historique.
     * @param annee             L'année de l'affectation.
     * @param paysOrigine       Le pays d'origine des visiteurs.
     * @param paysDestination   Le pays de destination des hôtes.
     */
    public void sauvegarderAffectation(Affectation affectation, String historyFilePath, String annee, String paysOrigine, String paysDestination) {
        Map<String, Affectation> history = historyService.loadAffectationHistory(historyFilePath);
        String key = String.format("%s_%s_%s", annee, paysOrigine, paysDestination);
        history.put(key, affectation);
        historyService.saveAffectationHistory(history, historyFilePath);
        System.out.println("Affectation sauvegardée dans l'historique avec la clé : " + key);
    }

    /**
     * Exporte les paires d'une affectation vers un fichier CSV.
     *
     * @param affectation       L'affectation contenant les paires.
     * @param outputCsvPath     Chemin du fichier CSV de sortie.
     */
    public void exporterAffectation(Affectation affectation, String outputCsvPath) {
        if (affectation == null || affectation.getPairs().isEmpty()) {
            System.out.println("Aucune affectation à exporter.");
            return;
        }
        csvService.exportAffectations(affectation.getPairs(), outputCsvPath);
        System.out.println("Les affectations ont été exportées avec succès vers : " + outputCsvPath);
    }
}
