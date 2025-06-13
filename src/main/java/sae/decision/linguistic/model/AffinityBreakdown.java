package sae.decision.linguistic.model;

import java.util.Map;

/**
 * Contient les résultats détaillés du calcul d'affinité entre deux adolescents.
 * Cette classe permet de comprendre la composition du score final.
 */
public class AffinityBreakdown {

    private final int finalScore;
    private final Map<String, Double> componentScores;
    private final Map<String, Boolean> compatibilityChecks;

    /**
     * Construit une nouvelle instance de l'analyse d'affinité.
     *
     * @param finalScore Le score final calculé (0-100).
     * @param componentScores Une map des scores pour chaque critère (ex: "age" -> 90.0).
     * @param compatibilityChecks Une map des vérifications de compatibilité (ex: "diet" -> true).
     */
    public AffinityBreakdown(int finalScore, Map<String, Double> componentScores, Map<String, Boolean> compatibilityChecks) {
        this.finalScore = finalScore;
        this.componentScores = componentScores;
        this.compatibilityChecks = compatibilityChecks;
    }

    /**
     * Récupère le score d'affinité final.
     * @return Le score final.
     */
    public int getFinalScore() {
        return finalScore;
    }

    /**
     * Récupère la map des scores des composants.
     * @return La map des scores.
     */
    public Map<String, Double> getComponentScores() {
        return componentScores;
    }

    /**
     * Récupère la map des vérifications de compatibilité.
     * @return La map des compatibilités.
     */
    public Map<String, Boolean> getCompatibilityChecks() {
        return compatibilityChecks;
    }
} 