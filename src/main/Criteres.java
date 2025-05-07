package main;

/**
 * Énumération des critères pouvant être utilisés pour les appariements
 * entre adolescents lors des séjours linguistiques.
 */
public enum Criteres {
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
     * Vérifie si la valeur est valide pour ce critère
     * @param valeur la valeur à vérifier
     * @return true si la valeur est valide, false sinon
     */
    public boolean isValid(String valeur) {
        if (valeur == null) {
            return true;
        }
        if (this == GUEST_ANIMAL_ALLERGY || this == HOST_HAS_ANIMAL) {
            return valeur.equals("yes") || valeur.equals("no");
        }
        else if (this == GENDER || this == PAIR_GENDER) {
            return valeur.equals("male") || valeur.equals("female") || valeur.equals("other") || valeur.isEmpty();
        }
        else if (this == HISTORY) {
            return valeur.equals("same") || valeur.equals("other") || valeur.isEmpty();
        }
        return true;
    }
}