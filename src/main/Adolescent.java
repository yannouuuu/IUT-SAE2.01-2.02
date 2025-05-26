package main;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Classe représentant un adolescent participant au programme de séjours linguistiques.
 * Chaque adolescent possède des informations personnelles ainsi que des critères
 * qui seront utilisés pour l'appariement.
 */
public class Adolescent {
    private String lastName;
    private String firstName;
    private String countryOfOrigin;
    private Map<Criteres, String> criteria;
    private LocalDate dateOfBirth;

    /**
     * Constructeur de la classe Adolescent
     * @param lastName le nom de famille de l'adolescent
     * @param firstName le prénom de l'adolescent
     * @param gender le genre de l'adolescent (male, female, other)
     * @param countryOfOrigin le pays d'origine de l'adolescent
     * @param criteria les critères de l'adolescent
     * @param dateOfBirth la date de naissance de l'adolescent
     */

    public Adolescent(String lastName, String firstName, String gender, String countryOfOrigin, Map<Criteres, String> criteria,
            LocalDate dateOfBirth, boolean isHost) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.countryOfOrigin = countryOfOrigin;
        this.criteria = new HashMap<>(); // Initialiser vide, puis ajouter les critères validés
        this.dateOfBirth = dateOfBirth;

        // D'abord on rajoute le genre préalablement fourni en paramètre mais qui est en fait en un critère
        if (gender != null && !gender.isEmpty()) {
            try {
                if (Criteres.GENDER.isValid(gender)) {
                    this.criteria.put(Criteres.GENDER, gender);
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Erreur lors de la validation du GENDER '" + gender + "' pour " + firstName + " " + lastName + ": " + e.getMessage());
            }
        }

        // Puis on ajoute les autres critères s'ils existent
        for (Map.Entry<Criteres, String> entry : criteria.entrySet()) {
            Criteres critere = entry.getKey();
            String valeur = entry.getValue();
            try {
                addCriterion(critere, valeur, isHost);
            } catch (IllegalArgumentException e) {
                System.err.println("Erreur pour " + firstName + " " + lastName + " avec le critère " + critere + " et valeur '" + valeur + "': " + e.getMessage());
            }
        }
    }

    /**
     * Constructeur simplifié de la classe Adolescent sans critères initiaux.
     * @param lastName le nom de famille de l'adolescent
     * @param firstName le prénom de l'adolescent
     * @param gender le genre de l'adolescent (male, female, other)
     * @param countryOfOrigin le pays d'origine de l'adolescent
     * @param dateOfBirth la date de naissance de l'adolescent
     */
    public Adolescent(String lastName, String firstName, String gender, String countryOfOrigin, LocalDate dateOfBirth, boolean isHost) {
        this(lastName, firstName, gender, countryOfOrigin, new HashMap<>(), dateOfBirth, isHost);
    }

    /**
     * Ajoute ou modifie un critère pour cet adolescent.
     * Lance une IllegalArgumentException si le critère est invalide ou viole les règles d'exclusivité.
     * @param criterion le type de critère à ajouter
     * @param value la valeur du critère
     * @throws IllegalArgumentException si le critère est invalide ou viole les règles d'exclusivité
     */
    public void addCriterion(Criteres criterion, String value, boolean isHost) throws IllegalArgumentException {
        // 1. Vérifier qu'il n'est pas déjà présent
        if (this.criteria.containsKey(criterion)){
            throw new IllegalArgumentException("Impossible d'ajouter " + criterion + " car le critère est déjà présent");
        }

        // 2. Valider la valeur pour le critère (lance une exception si invalide)
        criterion.isValid(value);

        // 3. Vérifier l'exclusivité HOST/GUEST
        boolean isGuestCriterion = criterion.name().startsWith("GUEST_");
        boolean isHostCriterion = criterion.name().startsWith("HOST_");

        if (isGuestCriterion && isHost==true) {
            throw new IllegalArgumentException("Conflit de critère: Impossible d'ajouter " + criterion +
                                            " car l'adolescent est un HOST");
                    
        } else if (isHostCriterion && isHost==false) {
            throw new IllegalArgumentException("Conflit de critère: Impossible d'ajouter " + criterion +
                                            " car l'adolescent est un GUEST");
        }

        // Si un critère est ajouté et qu'il existe déjà, il sera aussi remplacé.
        criteria.put(criterion, value);
    }

     // Getters
    /**
     * Récupère le nom de famille de l'adolescent.
     * @return le nom de famille de l'adolescent.
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * Récupère le prénom de l'adolescent.
     * @return le prénom de l'adolescent.
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Récupère le pays d'origine de l'adolescent.
     * @return le pays d'origine de l'adolescent.
     */
    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }
    
    /**
     * Récupère la date de naissance de l'adolescent.
     * @return la date de naissance de l'adolescent.
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    /**
     * Récupère une copie des critères de l'adolescent.
     * @return une map contenant les critères de l'adolescent.
     */
    public HashMap<Criteres, String> getCriteria() {
        return new HashMap<>(criteria);
    }
    
    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + countryOfOrigin + ")";
    }

    /**
     * Récupère la valeur d'un critère
     * @param criterion le critère à récupérer
     * @return la valeur du critère ou null si non défini
     */

    public String getCriterion(Criteres criterion) {
        return criteria.get(criterion);
    }

    /**
     * Vérifie la compatibilité avec un autre adolescent en tant qu'hôte
     * pour cet adolescent (this est l'hôte, other est le visiteur)
     * @param other l'adolescent visiteur
     * @return true si les deux adolescents sont compatibles, false sinon
     */
    public boolean isCompatible(Adolescent other) {
        // Vérification de la comptabilité des critères
        return isFrenchCompatible(other) && 
               isHistoryCompatible(other) && 
               animalScore(other) == 0 && 
               dietScore(other) == 0; // Si toutes les vérifications ont passé, les adolescents sont compatibles
    }

    /**
     * Vérifie la compatibilité entre cet adolescent (hôte) et un autre (visiteur) si l'un des deux est Français.
     * @param other l'adolescent visiteur.
     * @return true si ils ont un passe temps en commun ou qu'aucun n'est français, false sinon.
     */
    public boolean isFrenchCompatible(Adolescent other) {
        String myCountry = this.countryOfOrigin;
        String otherCountry = other.countryOfOrigin;

        // Si aucun n'est français pas de problèmes
        if ((myCountry == null || !myCountry.equals("France")) &&
            (otherCountry == null || !otherCountry.equals("France"))) {
            return true;
        }

        // Si l'un des deux est français on s'intéresse aux pase temps'
        if (myCountry != null && myCountry.equals("France") ||
            otherCountry != null && otherCountry.equals("France")) {
            String myHobbies = this.getCriterion(Criteres.HOBBIES);
            String otherHobbies = other.getCriterion(Criteres.HOBBIES);

            if (myHobbies != null && otherHobbies != null) {
                Set<String> myHobbiesSet = new HashSet<>();
                try {
                    String[] myHobbiesList = myHobbies.split(",");
                    for (String hobby : myHobbiesList) {
                        myHobbiesSet.add(hobby.trim());
                    }
                } catch (NullPointerException e) {
                    System.err.println("Erreur lors du traitement des hobbies de " + this.firstName + ": " + e.getMessage());
                    return false;
                }

                Set<String> otherHobbiesSet = new HashSet<>();
                try {
                    String[] otherHobbiesList = otherHobbies.split(",");
                    for (String hobby : otherHobbiesList) {
                        otherHobbiesSet.add(hobby.trim());
                    }
                } catch (NullPointerException e) {
                    System.err.println("Erreur lors du traitement des hobbies de " + other.firstName + ": " + e.getMessage());
                    return false;
                }

                for (String myHobby : myHobbiesSet) {
                    if (otherHobbiesSet.contains(myHobby)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Vérifie la compatibilité des historiques entre cet adolescent (hôte) et un autre (visiteur).
     * @param other l'adolescent visiteur.
     * @return true si les historiques sont compatibles, false sinon.
     */
    public boolean isHistoryCompatible(Adolescent other) {
        String myHistory = this.getCriterion(Criteres.HISTORY);
        String otherHistory = other.getCriterion(Criteres.HISTORY);

        // Si l'un des deux a la contrainte "other", ils sont incompatibles
        if ((myHistory != null && myHistory.equals("other")) || 
            (otherHistory != null && otherHistory.equals("other"))) {
            return false;
        }

        // Si les deux ont la contrainte "same", ils doivent être compatibles
        if (myHistory != null && myHistory.equals("same") &&
            otherHistory != null && otherHistory.equals("same")) {
            return true;
        }
        return true;
    }

    /**
     * Vérifie la compatibilité concernant les animaux entre cet adolescent (hôte) et un autre (visiteur).
     * L'adolescent est compatible si le visiteur n'est pas allergique aux animaux OU si l'hôte n'a pas d'animaux.
     * @param other l'adolescent visiteur.
     * @return 0 si compatible concernant les animaux, -25 sinon.
     */
    public int animalScore(Adolescent other) {
        // Vérification des allergies aux animaux
        String hostHasAnimal = this.getCriterion(Criteres.HOST_HAS_ANIMAL);
        String guestAllergy = other.getCriterion(Criteres.GUEST_ANIMAL_ALLERGY);
        
        if ((guestAllergy != null && guestAllergy.equals("yes")) && (hostHasAnimal != null && hostHasAnimal.equals("yes"))) {
                return -25;
        }
        return 0;
    }

    /**
     * Vérifie la compatibilité des régimes alimentaires entre cet adolescent (hôte) et un autre (visiteur).
     * Compatible si le visiteur n'a pas de régime spécifique, ou si l'hôte peut satisfaire tous les régimes du visiteur.
     * @param other l'adolescent visiteur.
     * @return 0 si les régimes sont compatibles, -5*(Nbr de régimes non-respectés) sinon.
     */
    public int dietScore(Adolescent other) {
        // Vérification des régimes alimentaires
        String hostDiet = this.getCriterion(Criteres.HOST_FOOD);
        String guestDiet = other.getCriterion(Criteres.GUEST_FOOD);

        // Si le visiteur n'a pas de régime spécial, c'est toujours compatible
        if (guestDiet == null || guestDiet.isEmpty()) {
            return 0;
        }

        // Si l'hôte n'accepte aucun régime particulier et que le visiteur en a un
        if ((hostDiet == null || hostDiet.isEmpty()) && (guestDiet != null && !guestDiet.isEmpty())) {
            // On compte le nombre de régimes demandés par le visiteur comme incompatibles
            int incompatiblesDiets = 0;
            String[] guestDiets = guestDiet.split(",");
            for (String diet : guestDiets) {
                if (diet != null && !diet.trim().isEmpty()) {
                    incompatiblesDiets -= 10;
                }
            }
            return incompatiblesDiets;
        }

        Set<String> hostDietsSet = new HashSet<>();
        try {
            if (hostDiet != null) {
                String[] hostDiets = hostDiet.split(",");
                for (String diet : hostDiets) {
                    hostDietsSet.add(diet.trim());
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Erreur lors du traitement des régimes de l'hôte " + this.firstName + ": " + e.getMessage());
            return 0; // Ou une autre logique de gestion d'erreur
        }

        int incompatiblesDiets=0;
        try {
            if (guestDiet != null) {
                String[] guestDiets = guestDiet.split(",");
                for (String diet : guestDiets) {
                    if (!hostDietsSet.contains(diet.trim())) {
                        incompatiblesDiets-=10;
                    }
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Erreur lors du traitement des régimes du visiteur " + other.firstName + ": " + e.getMessage());
            return 0; // Ou une autre logique de gestion d'erreur
        }

        return incompatiblesDiets;
    }

    /**
     * Calcule le bonus d'affinité lié à l'historique entre cet adolescent et un autre.
     * @param other l'autre adolescent.
     * @return le bonus d'affinité (entier).
     */
    public int getHistoryAffinityBonus(Adolescent other) {
        String myHistory = this.getCriterion(Criteres.HISTORY);
        String otherHistory = other.getCriterion(Criteres.HISTORY);

        // Si l'un a exprimé "same" et l'autre n'a rien exprimé, bonus d'affinité
        if ((myHistory != null && myHistory.equals("same") && (otherHistory == null || otherHistory.isEmpty())) ||
            (otherHistory != null && otherHistory.equals("same") && (myHistory == null || myHistory.isEmpty()))) {
            return 10;
        }
        return 0;
    }

    /**
     * Calcule l'affinité entre cet adolescent et un autre
     * @param other l'autre adolescent
     * @return un score d'affinité sous forme d'entier
     */
    public int calculateAffinity(Adolescent other) {
        int score = 50;

        if (!isCompatible(other)) {
            return 0;
        }
        
        //Malus éventuel pour les régimes non respectés (au mieux 0)
        score+=dietScore(other);

        //Malus éventuel pour les alergies aux animaux (au mieux 0)
        score+=animalScore(other);

        // Bonus pour les passe-temps communs

        //Pas besoins de try catch puisque les cas potentiels sont déjà vérifiés plus tôt
        String myHobbies = this.getCriterion(Criteres.HOBBIES);
        String otherHobbies = other.getCriterion(Criteres.HOBBIES);
        
        if (myHobbies != null && otherHobbies != null) {
            String[] myHobbiesList = myHobbies.split(",");
            Set<String> myHobbiesSet = new HashSet<>();
            for (String hobby : myHobbiesList) {
                myHobbiesSet.add(hobby.trim());
            }
        
            String[] otherHobbiesList = otherHobbies.split(",");
            Set<String> otherHobbiesSet = new HashSet<>();
            for (String hobby : otherHobbiesList) {
                otherHobbiesSet.add(hobby.trim());
            }            

            for (String myHobby : myHobbiesSet) {
                if (otherHobbiesSet.contains(myHobby)) {
                    score += 5; // +5 points pour chaque passe-temps commun
                }
            }
        }
        
        String myGender = this.getCriterion(Criteres.GENDER);
        String otherGender = other.getCriterion(Criteres.GENDER);
        String myPrefGender = this.getCriterion(Criteres.PAIR_GENDER);
        String otherPrefGender = other.getCriterion(Criteres.PAIR_GENDER);
        
        if (myPrefGender == null || myPrefGender.isEmpty() || myPrefGender.equals(otherGender)) {
            score += 3;
        }
        
        if (otherPrefGender == null || otherPrefGender.isEmpty() || otherPrefGender.equals(myGender)) {
            score += 3;
        }
        
        if (calculateAgeDifference(other) < 1.5) { // moins de 1 an et demi d'écart
            score += 4;
        }

        // Ajout du bonus d'affinité pour l'historique
        score += getHistoryAffinityBonus(other);
        
        return score;
    }
    
    /**
     * Calcule la différence d'âge en années entre cet adolescent et un autre
     * @param other l'autre adolescent
     * @return la différence d'âge en années (valeur absolue)
     */
    public double calculateAgeDifference(Adolescent other) {
        long days = Math.abs(ChronoUnit.DAYS.between(this.dateOfBirth, other.dateOfBirth));
        return days / 365.25;
    }
}