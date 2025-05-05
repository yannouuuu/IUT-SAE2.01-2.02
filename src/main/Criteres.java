package main;

/**
 * Énumération des critères pouvant être utilisés pour les appariements
 * entre adolescents lors des séjours linguistiques.
 */
public enum Criteres {
    GUEST_ANIMAL_ALLERGY,
    HOST_HAS_ANIMAL,
    GUEST_FOOD,
    HOST_FOOD,
    HOBBIES,
    GENDER,
    PAIR_GENDER,
    HISTORY;
    
    /**
     * Permet d'obtenir le type de critère (booléen, texte, numérique ou date)
     * @return un caractère représentant le type du critère
     */
    public char getType() {
        if (this == GUEST_ANIMAL_ALLERGY || this == HOST_HAS_ANIMAL) {
            return 'B'; // type booléen
        } 
        else if (this == GUEST_FOOD || this == HOST_FOOD || this == HOBBIES || 
                 this == GENDER || this == PAIR_GENDER || this == HISTORY) {
            return 'T'; // type texte
        }
        
        return 'T'; // par défaut, on retourne texte
    }
    
    /**
     * Vérifie si la valeur est valide pour ce critère
     * @param valeur la valeur à vérifier
     * @return true si la valeur est valide, false sinon
     */
    public boolean estValide(String valeur) {
        if (valeur == null) {
            return true; // Une valeur vide est considérée comme valide (absence de contrainte)
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
        
        // Pour les autres critères, toutes les valeurs sont valides
        return true;
    }
}