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
    private String gender;
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
            LocalDate dateOfBirth) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.gender = gender;
        this.countryOfOrigin = countryOfOrigin;
        this.criteria = new HashMap<>(criteria);
        this.dateOfBirth = dateOfBirth;
    }
// Adolescent("Hote", "A", "FR", LocalDate.now(), "male", Map.of(Criteres.HOST_FOOD, "vegetarian"));
    /**
     * Constructeur simplifié de la classe Adolescent sans critères initiaux.
     * @param lastName le nom de famille de l'adolescent
     * @param firstName le prénom de l'adolescent
     * @param gender le genre de l'adolescent (male, female, other)
     * @param countryOfOrigin le pays d'origine de l'adolescent
     * @param dateOfBirth la date de naissance de l'adolescent
     */
    public Adolescent(String lastName, String firstName, String gender, String countryOfOrigin, LocalDate dateOfBirth) {
        this(lastName, firstName, gender, countryOfOrigin, new HashMap<>(), dateOfBirth);
    }


    /**
     * Ajoute ou modifie un critère pour cet adolescent
     * @param criterion le type de critère à ajouter
     * @param value la valeur du critère
     */
    public void addCriterion(Criteres criterion, String value) {
        if (criterion.isValid(value)) {
            criteria.put(criterion, value);
        }
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
     * Récupère le genre de l'adolescent.
     * @return le genre de l'adolescent.
     */
    public String getGender() {
        return gender;
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
    public Map<Criteres, String> getCriteria() {
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
        return isAnimalCompatible(other) && isDietCompatible(other) && isHistoryCompatible(other); // Si toutes les vérifications ont passé, les adolescents sont compatibles
    }

    /**
     * Vérifie la compatibilité concernant les animaux entre cet adolescent (hôte) et un autre (visiteur).
     * L'adolescent est compatible si le visiteur n'est pas allergique aux animaux OU si l'hôte n'a pas d'animaux.
     * @param other l'adolescent visiteur.
     * @return true si compatible concernant les animaux, false sinon.
     */
    public boolean isAnimalCompatible(Adolescent other) {
        // Vérification des allergies aux animaux
        String hostHasAnimal = this.getCriterion(Criteres.HOST_HAS_ANIMAL);
        String guestAllergy = other.getCriterion(Criteres.GUEST_ANIMAL_ALLERGY);
        
        if ((guestAllergy != null && guestAllergy.equals("yes")) && (hostHasAnimal != null && hostHasAnimal.equals("yes"))) {
                return false;
        }
        return true;
    }

    /**
     * Vérifie la compatibilité des régimes alimentaires entre cet adolescent (hôte) et un autre (visiteur).
     * Compatible si le visiteur n'a pas de régime spécifique, ou si l'hôte peut satisfaire tous les régimes du visiteur.
     * @param other l'adolescent visiteur.
     * @return true si les régimes sont compatibles, false sinon.
     */
    public boolean isDietCompatible(Adolescent other) {
        // Vérification des régimes alimentaires
        String hostDiet = this.getCriterion(Criteres.HOST_FOOD);
        String guestDiet = other.getCriterion(Criteres.GUEST_FOOD);
        
        // Si le visiteur n'a pas de régime spécial, c'est toujours compatible
        if (guestDiet == null || guestDiet.isEmpty()) {
            return true;
        }
        
        // Si l'hôte n'accepte aucun régime particulier
        if (hostDiet == null || hostDiet.isEmpty()) {
            return false;
        }

        // Vérification si tous les régimes du visiteur sont satisfaits par l'hôte
        String[] guestDiets = guestDiet.split(",");
        String[] hostDiets = hostDiet.split(",");
        
        Set<String> hostDietsSet = new HashSet<>();
        for (String diet : hostDiets) {
            hostDietsSet.add(diet.trim());
        }
        
        for (String diet : guestDiets) {
            if (!hostDietsSet.contains(diet.trim())) {
                return false;
            }
        }
        
        return true;
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
            return 3;
        }
        return 0;
    }

    /**
     * Calcule l'affinité entre cet adolescent et un autre
     * @param other l'autre adolescent
     * @return un score d'affinité sous forme d'entier
     */
    public int calculateAffinity(Adolescent other) {
        int score = 0;

        if (!isCompatible(other)) {
            return 0;
        }
        
        // Bonus pour les passe-temps communs
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
        
        String myGender = this.gender;
        String otherGender = other.gender;
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
    private double calculateAgeDifference(Adolescent other) {
        long days = Math.abs(ChronoUnit.DAYS.between(this.dateOfBirth, other.dateOfBirth));
        return days / 365.25;
    }
}