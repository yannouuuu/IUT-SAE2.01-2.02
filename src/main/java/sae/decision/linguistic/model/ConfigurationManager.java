package sae.decision.linguistic.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Gère la configuration des poids pour l'algorithme d'appariement.
 * Cette classe conserve les valeurs des pondérations définies dans les réglages.
 */
public class ConfigurationManager {

    private static final Map<String, Double> weights = new HashMap<>();

    static {
        // Valeurs par défaut, synchronisées avec SettingsController
        weights.put("age", 20.0);
        weights.put("gender", 15.0);
        weights.put("hobbies", 30.0);
        weights.put("language", 25.0);
        weights.put("activities", 10.0);
    }

    /**
     * Récupère le poids pour un critère donné.
     * @param key La clé du critère (ex: "age", "gender").
     * @return La valeur du poids.
     */
    public static double getWeight(String key) {
        return weights.getOrDefault(key.toLowerCase(), 0.0);
    }

    /**
     * Récupère tous les poids de la configuration.
     * @return Une copie de la map des poids.
     */
    public static Map<String, Double> getAllWeights() {
        return new HashMap<>(weights);
    }

    /**
     * Définit le poids pour un critère donné.
     * @param key La clé du critère.
     * @param value La nouvelle valeur du poids.
     */
    public static void setWeight(String key, double value) {
        weights.put(key.toLowerCase(), value);
    }
} 