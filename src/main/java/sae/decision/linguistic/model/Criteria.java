package sae.decision.linguistic.model;

/**
 * Énumération des critères pouvant être utilisés pour les appariements
 * entre adolescents lors des séjours linguistiques.
 */
public enum Criteria {
    /** Indique si l'adolescent visiteur est allergique aux animaux ("yes", "no"). */
    GUEST_ANIMAL_ALLERGY,
    /** Indique si l'adolescent hôte possède des animaux ("yes", "no"). */
    HOST_HAS_ANIMAL,
    /** Régime alimentaire spécifique de l'adolescent visiteur (ex: "vegetarian,no-pork"). */
    GUEST_FOOD,
    /** Régime alimentaire que l'adolescent hôte peut accommoder (ex: "vegetarian,halal"). */
    HOST_FOOD,
    /** Liste des passe-temps de l'adolescent, séparés par des virgules (ex: "sport,music,reading"). */
    HOBBIES,
    /** Genre de l'adolescent ("male", "female", "other"). */
    GENDER,
    /** Préférence de genre pour l'appariement ("male", "female", "other", ou vide si pas de préférence). */
    PAIR_GENDER,
    /** Historique d'appariement avec un autre adolescent ("same" pour éviter le même type de profil, "other" sinon, ou vide). */
    HISTORY;

    /**
     * Permet d'obtenir le type de critère (booléen, texte, numérique ou date)
     * @return un caractère représentant le type du critère
     */
    public char getType() {
        if (this == GUEST_ANIMAL_ALLERGY || this == HOST_HAS_ANIMAL) {
            return 'B';
        }
        else if (this == GUEST_FOOD || this == HOST_FOOD || this == HOBBIES ||
                this == GENDER || this == PAIR_GENDER || this == HISTORY) {
            return 'T';
        }

        return 'T';
    }

    /**
     * Vérifie si la valeur est valide pour ce critère.
     * Lance une IllegalArgumentException si la valeur n'est pas valide.
     * @param valeur la valeur à vérifier
     * @return true si la valeur est valide (ne sera atteint que si aucune exception n'est levée)
     * @throws IllegalArgumentException si la valeur n'est pas valide
     */
    public boolean isValid(String valeur) throws IllegalArgumentException {
        if (valeur == null) {
            if (this == PAIR_GENDER || this == HISTORY) {
                return true; // Vide (null) est permis pour PAIR_GENDER et HISTORY
            } else if (this == GUEST_FOOD || this == HOST_FOOD || this == HOBBIES) {
                return true; // Peut être null si pas de régime/hobbies
            } else if (this == GUEST_ANIMAL_ALLERGY || this == HOST_HAS_ANIMAL || this == GENDER) {
                throw new IllegalArgumentException("La valeur pour " + this + " ne peut pas être null.");
            }
        }

        if (this == GUEST_ANIMAL_ALLERGY || this == HOST_HAS_ANIMAL) {
            if (!"yes".equalsIgnoreCase(valeur) && !"no".equalsIgnoreCase(valeur)) {
                throw new IllegalArgumentException("La valeur pour " + this + " doit être 'yes' ou 'no'. Valeur reçue: " + valeur);
            }
        } else if (this == GENDER) {
            if (valeur == null) {
                throw new IllegalArgumentException("La valeur pour " + this + " ne peut pas être null.");
            }
            if (!"male".equalsIgnoreCase(valeur) && !"female".equalsIgnoreCase(valeur) && !"other".equalsIgnoreCase(valeur)) {
                throw new IllegalArgumentException("La valeur pour " + this + " doit être 'male', 'female', ou 'other'. Valeur reçue: " + valeur);
            }
        } else if (this == PAIR_GENDER) {
            if (valeur != null && !valeur.isEmpty() &&
                    !"male".equalsIgnoreCase(valeur) && !"female".equalsIgnoreCase(valeur) && !"other".equalsIgnoreCase(valeur)) {
                throw new IllegalArgumentException("La valeur pour " + this + " doit être 'male', 'female', 'other' ou vide. Valeur reçue: " + valeur);
            }
        } else if (this == HISTORY) {
            if (valeur != null && !valeur.isEmpty() &&
                    !"same".equalsIgnoreCase(valeur) && !"other".equalsIgnoreCase(valeur)) {
                throw new IllegalArgumentException("La valeur pour " + this + " doit être 'same', 'other' ou vide. Valeur reçue: " + valeur);
            }
        } else if (this == GUEST_FOOD || this == HOST_FOOD || this == HOBBIES) {
            if (valeur != null) {
                if (valeur.contains(";")) {
                    throw new IllegalArgumentException("La valeur pour " + this + " ne peut pas contenir de point-virgule. Valeur reçue: " + valeur);
                }
            }
        }
        return true; // Si on arrive ici, la valeur est valide
    }

}