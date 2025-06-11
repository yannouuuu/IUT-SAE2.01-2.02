package sae.decision.linguistic.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Gestionnaire de configuration pour le système d'appariement d'adolescents.
 * Permet de charger et modifier les paramètres de calcul d'affinité depuis un fichier de configuration.
 */
public class ConfigurationService {
    private static final String DEFAULT_CONFIG_FILE = "affinity_config.properties";
    
    // Valeurs par défaut du système
    private static final Map<String, Object> DEFAULT_VALUES = new HashMap<>();
    
    // Configuration actuelle
    private static final Map<String, Object> currentConfig = new HashMap<>();
    
    static {
        initializeDefaultValues();
        loadDefaultConfiguration();
    }
    
    /**
     * Initialise les valeurs par défaut du système basées sur l'analyse du code existant.
     */
    private static void initializeDefaultValues() {
        // Pondérations pour le calcul du score final
        DEFAULT_VALUES.put("weight.age", 1.0);
        DEFAULT_VALUES.put("weight.gender", 1.0);
        DEFAULT_VALUES.put("weight.hobbies", 1.0);
        
        // Paramètres de calcul du score d'âge
        DEFAULT_VALUES.put("age.base_score", 10.0);
        DEFAULT_VALUES.put("age.multiplier", 6.67);
        
        // Paramètres de calcul du score des hobbies
        DEFAULT_VALUES.put("hobbies.points_per_common", 15.0);
        DEFAULT_VALUES.put("hobbies.max_score", 100.0);
        
        // Pénalités et bonus
        DEFAULT_VALUES.put("bonus.history_same", 10.0);
        DEFAULT_VALUES.put("bonus.one_history_same", 5.0);
        DEFAULT_VALUES.put("penalty.history_incompatible", -10.0);
        DEFAULT_VALUES.put("penalty.diet_incompatible", -10.0);
        DEFAULT_VALUES.put("penalty.animal_allergy", -25.0);
        
        // Limites du système
        DEFAULT_VALUES.put("score.min", 0.0);
        DEFAULT_VALUES.put("score.max", 100.0);
        
        // Compatibilité stricte (true = critères mutuellement exclusifs)
        DEFAULT_VALUES.put("strict.diet_compatibility", true);
        DEFAULT_VALUES.put("strict.animal_compatibility", true);
        DEFAULT_VALUES.put("strict.history_compatibility", true);
        DEFAULT_VALUES.put("strict.host_guest_criteria", true);
        DEFAULT_VALUES.put("strict.french_compatibility", true);
        
        // Seuils de compatibilité
        DEFAULT_VALUES.put("threshold.min_score_for_compatibility", 0.0);
    }
    
    /**
     * Charge la configuration par défaut.
     */
    private static void loadDefaultConfiguration() {
        currentConfig.putAll(DEFAULT_VALUES);
    }
    
    /**
     * Charge la configuration depuis un fichier.
     * @param configFile Chemin vers le fichier de configuration
     * @throws IOException Si le fichier ne peut pas être lu
     */
    public static void loadConfiguration(String configFile) throws IOException {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(configFile)) {
            props.load(input);
            
            // Convertir les propriétés en types appropriés
            for (String key : props.stringPropertyNames()) {
                String value = props.getProperty(key);
                try {
                    if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                        currentConfig.put(key, value.equalsIgnoreCase("true"));
                    } else {
                        currentConfig.put(key, Double.valueOf(value));
                    }
                } catch (NumberFormatException e) {
                    // Si ce n'est pas un nombre ni un booléen, garder comme string
                    currentConfig.put(key, value);
                }
            }
        }
    }
    
    /**
     * Charge la configuration depuis le fichier par défaut.
     * @throws IOException Si le fichier ne peut pas être lu
     */
    public static void loadConfiguration() throws IOException {
        loadConfiguration(DEFAULT_CONFIG_FILE);
    }
    
    /**
     * Sauvegarde la configuration actuelle dans un fichier.
     * @param configFile Chemin vers le fichier de configuration
     * @throws IOException Si le fichier ne peut pas être écrit
     */
    public static void saveConfiguration(String configFile) throws IOException {
        Properties props = new Properties();
        
        // Convertir la configuration actuelle en propriétés
        for (Map.Entry<String, Object> entry : currentConfig.entrySet()) {
            props.setProperty(entry.getKey(), entry.getValue().toString());
        }
        
        try (OutputStream output = new FileOutputStream(configFile)) {
            props.store(output, "Configuration d'affinité - Générée automatiquement");
        }
    }
    
    /**
     * Récupère une valeur de configuration sous forme de double.
     * @param key Clé de configuration
     * @return Valeur de configuration ou valeur par défaut si non trouvée
     */
    public static double getDouble(String key) {
        Object value = currentConfig.get(key);
        if (value != null) {
            switch (value) {
                case Double d -> {
                    return d;
                }
                case Number n -> {
                    return n.doubleValue();
                }
                default -> {
                    // Type non correspondant, on passe à la logique de la valeur par défaut
                }
            }
        }

        // Retourner la valeur par défaut si elle existe
        Object defaultValue = DEFAULT_VALUES.get(key);
        if (defaultValue instanceof Number n) {
            return n.doubleValue();
        }
        return 0.0;
    }
    
    /**
     * Récupère une valeur de configuration sous forme de booléen.
     * @param key Clé de configuration
     * @return Valeur de configuration ou valeur par défaut si non trouvée
     */
    public static boolean getBoolean(String key) {
        Object value = currentConfig.get(key);
        if (value != null) {
            switch (value) {
                case Boolean b -> {
                    return b;
                }
                default -> {
                    // Type non correspondant, on passe à la logique de la valeur par défaut
                }
            }
        }

        // Retourner la valeur par défaut si elle existe
        Object defaultValue = DEFAULT_VALUES.get(key);
        if (defaultValue instanceof Boolean b) {
            return b;
        }
        return false;
    }
    
    /**
     * Récupère une valeur de configuration sous forme de chaîne.
     * @param key Clé de configuration
     * @return Valeur de configuration ou valeur par défaut si non trouvée
     */
    public static String getString(String key) {
        Object value = currentConfig.get(key);
        if (value != null) {
            return value.toString();
        }
        Object defaultValue = DEFAULT_VALUES.get(key);
        return defaultValue != null ? defaultValue.toString() : "";
    }
    
    /**
     * Définit une valeur de configuration.
     * @param key Clé de configuration
     * @param value Valeur à définir
     */
    public static void setValue(String key, Object value) {
        currentConfig.put(key, value);
    }
    
    /**
     * Récupère toutes les pondérations sous forme de Map.
     * @return Map des pondérations (age, gender, hobbies)
     */
    public static Map<String, Double> getAllWeights() {
        Map<String, Double> weights = new HashMap<>();
        weights.put("age", getDouble("weight.age"));
        weights.put("gender", getDouble("weight.gender"));
        weights.put("hobbies", getDouble("weight.hobbies"));
        return weights;
    }
    
    /**
     * Remet la configuration aux valeurs par défaut.
     */
    public static void resetToDefaults() {
        currentConfig.clear();
        currentConfig.putAll(DEFAULT_VALUES);
    }
    
    /**
     * Récupère toute la configuration actuelle.
     * @return Map de la configuration actuelle
     */
    public static Map<String, Object> getAllConfiguration() {
        return new HashMap<>(currentConfig);
    }
    
    /**
     * Récupère les valeurs par défaut du système.
     * @return Map des valeurs par défaut
     */
    public static Map<String, Object> getDefaultValues() {
        return new HashMap<>(DEFAULT_VALUES);
    }
    
    /**
     * Vérifie si la compatibilité stricte est activée pour un critère donné.
     * @param criteriaType Type de critère (diet, animal, history, host_guest)
     * @return true si la compatibilité stricte est activée
     */
    public static boolean isStrictCompatibility(String criteriaType) {
        return getBoolean("strict." + criteriaType + "_compatibility");
    }
    
    /**
     * Crée un fichier de configuration d'exemple avec toutes les valeurs par défaut.
     * @param filename Nom du fichier à créer
     * @throws IOException Si le fichier ne peut pas être créé
     */
    public static void createExampleConfigFile(String filename) throws IOException {
        Properties props = new Properties();
        
        props.setProperty("# Pondérations pour le calcul du score final", "");
        props.setProperty("weight.age", DEFAULT_VALUES.get("weight.age").toString());
        props.setProperty("weight.gender", DEFAULT_VALUES.get("weight.gender").toString());
        props.setProperty("weight.hobbies", DEFAULT_VALUES.get("weight.hobbies").toString());
        
        props.setProperty("# Paramètres de calcul du score d'âge", "");
        props.setProperty("age.base_score", DEFAULT_VALUES.get("age.base_score").toString());
        props.setProperty("age.multiplier", DEFAULT_VALUES.get("age.multiplier").toString());
        
        props.setProperty("# Paramètres de calcul du score des hobbies", "");
        props.setProperty("hobbies.points_per_common", DEFAULT_VALUES.get("hobbies.points_per_common").toString());
        props.setProperty("hobbies.max_score", DEFAULT_VALUES.get("hobbies.max_score").toString());
        
        props.setProperty("# Pénalités et bonus", "");
        props.setProperty("bonus.history_same", DEFAULT_VALUES.get("bonus.history_same").toString());
        props.setProperty("bonus.one_history_same", DEFAULT_VALUES.get("bonus.one_history_same").toString());
        props.setProperty("penalty.history_incompatible", DEFAULT_VALUES.get("penalty.history_incompatible").toString());
        props.setProperty("penalty.diet_incompatible", DEFAULT_VALUES.get("penalty.diet_incompatible").toString());
        props.setProperty("penalty.animal_allergy", DEFAULT_VALUES.get("penalty.animal_allergy").toString());
        
        props.setProperty("# Limites du système", "");
        props.setProperty("score.min", DEFAULT_VALUES.get("score.min").toString());
        props.setProperty("score.max", DEFAULT_VALUES.get("score.max").toString());
        
        props.setProperty("# Compatibilité stricte (true = critères mutuellement exclusifs)", "");
        props.setProperty("strict.diet_compatibility", DEFAULT_VALUES.get("strict.diet_compatibility").toString());
        props.setProperty("strict.animal_compatibility", DEFAULT_VALUES.get("strict.animal_compatibility").toString());
        props.setProperty("strict.history_compatibility", DEFAULT_VALUES.get("strict.history_compatibility").toString());
        props.setProperty("strict.host_guest_criteria", DEFAULT_VALUES.get("strict.host_guest_criteria").toString());
        props.setProperty("strict.french_compatibility", DEFAULT_VALUES.get("strict.french_compatibility").toString());

        props.setProperty("# Seuils de compatibilité", "");
        props.setProperty("threshold.min_score_for_compatibility", DEFAULT_VALUES.get("threshold.min_score_for_compatibility").toString());

        
        try (OutputStream output = new FileOutputStream(filename)) {
            props.store(output, "Fichier de configuration d'exemple pour le système d'appariement d'adolescents");
        }
    }
}