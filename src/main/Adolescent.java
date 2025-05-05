package main;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Classe représentant un adolescent participant au programme de séjours linguistiques.
 * Chaque adolescent possède des informations personnelles ainsi que des critères
 * qui seront utilisés pour l'appariement.
 */
public class Adolescent {
    // Attributs
    private String nom;
    private String prenom;
    private String genre;
    private String paysOrigine;
    private Map<Criteres, String> criteres;
    private LocalDate dateNaissance;

    /**
     * Constructeur de la classe Adolescent
     * @param nom le nom de famille de l'adolescent
     * @param prenom le prénom de l'adolescent
     * @param genre le genre de l'adolescent (male, female, other)
     * @param paysOrigine le pays d'origine de l'adolescent
     * @param dateNaissance la date de naissance de l'adolescent
     */

    public Adolescent(String nom, String prenom, String genre, String paysOrigine, Map<Criteres, String> criteres,
            LocalDate dateNaissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.genre = genre;
        this.paysOrigine = paysOrigine;
        this.criteres = new HashMap<>();;
        this.dateNaissance = dateNaissance;
    }
// Adolescent("Hote", "A", "FR", LocalDate.now(), "male", Map.of(Criteres.HOST_FOOD, "vegetarian"));
    public Adolescent(String nom, String prenom, String genre, String paysOrigine, LocalDate dateNaissance) {
        this(nom, prenom, genre, paysOrigine, new HashMap<>(), dateNaissance);
    }


    /**
     * Ajoute ou modifie un critère pour cet adolescent
     * @param critere le type de critère à ajouter
     * @param valeur la valeur du critère
     */
    public void ajouterCritere(Criteres critere, String valeur) {
        if (critere.estValide(valeur)) {
            criteres.put(critere, valeur);
        }
    }

        // Getters
    public String getNom() {
        return nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public String getPaysOrigine() {
        return paysOrigine;
    }
    
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }
    
    public Map<Criteres, String> getCriteres() {
        return new HashMap<>(criteres);
    }
    
    @Override
    public String toString() {
        return prenom + " " + nom + " (" + paysOrigine + ")";
    }

    /**
     * Récupère la valeur d'un critère
     * @param critere le critère à récupérer
     * @return la valeur du critère ou null si non défini
     */
    public String getCritere(Criteres critere) {
        return criteres.get(critere);
    }

    /**
     * Vérifie la compatibilité avec un autre adolescent en tant qu'hôte
     * pour cet adolescent (this est l'hôte, other est le visiteur)
     * @param other l'adolescent visiteur
     * @return true si les deux adolescents sont compatibles, false sinon
     */
    public boolean estCompatible(Adolescent other) {
        // Vérification de la comptabilité des critères
        return CompatibleAnimal(other) && CompatibleRegime(other); // Si toutes les vérifications ont passé, les adolescents sont compatibles
    }

    public boolean CompatibleAnimal(Adolescent other) {
        // Vérification des allergies aux animaux
        String hostHasAnimal = this.getCritere(Criteres.HOST_HAS_ANIMAL);
        String guestAllergy = other.getCritere(Criteres.GUEST_ANIMAL_ALLERGY);
        
        if ((guestAllergy != null && guestAllergy.equals("yes")) && (hostHasAnimal != null && hostHasAnimal.equals("yes"))) {
                return false; // Non compatible: visiteur allergique et hôte avec animal
        }
        return true; // Si toutes les vérifications ont passé, les adolescents sont compatibles
    }

    public boolean CompatibleRegime(Adolescent other) {
        // Vérification des régimes alimentaires
        String hostDiet = this.getCritere(Criteres.HOST_FOOD);
        String guestDiet = other.getCritere(Criteres.GUEST_FOOD);
        
        if ((guestDiet != null && !guestDiet.isEmpty()) && (hostDiet == null || hostDiet.isEmpty())) {
                return false; // L'hôte n'accepte aucun régime particulier
        } else{

            // Vérification si tous les régimes du visiteur sont satisfaits par l'hôte
            String[] guestDiets = guestDiet.split(",");
            String[] hostDiets = hostDiet.split(",");
            Set<String> hostDietsSet = new HashSet<String>(Arrays.asList(hostDiets));
            
            for (String diet : guestDiets) {
                if (!hostDietsSet.contains(diet)) {
                    return false; // L'hôte ne peut pas satisfaire ce régime alimentaire
                }
            }
        }
        return true; // Si toutes les vérifications ont passé, les adolescents sont compatibles
    }

    // Vérification de l'historique à implémenter
        
    //String myHistory = this.getCritere(Criteres.HISTORY);
    //String otherHistory = other.getCritere(Criteres.HISTORY);

    // Pour simplifier, on suppose qu'il n'y a pas d'historique pour l'instant
    // Pour l'implémentation complète, il faudra vérifier l'historique des appariements
       


    /**
     * Calcule l'affinité entre cet adolescent et un autre
     * @param other l'autre adolescent
     * @return un score d'affinité sous forme d'entier
     */
    public int calculerAffinite(Adolescent other) {
        int score = 0;
        
        // Bonus pour les passe-temps communs
        String myHobbies = this.getCritere(Criteres.HOBBIES);
        String otherHobbies = other.getCritere(Criteres.HOBBIES);
        
        if (myHobbies != null && otherHobbies != null) {
            String[] myHobbiesList = myHobbies.split(",");
            Set<String> myHobbiesSet = new HashSet<String>(Arrays.asList(myHobbiesList));
        
            String[] otherHobbiesList = otherHobbies.split(",");
            Set<String> otherHobbiesSet = new HashSet<String>(Arrays.asList(otherHobbiesList));            

            for (String myHobby : myHobbiesList) {
                if (otherHobbiesSet.contains(myHobby)) {
                    score += 5; // +5 points pour chaque passe-temps commun
                }
            }
        }
        
        // Bonus pour les préférences de genre satisfaites
        String myGender = this.genre;
        String otherGender = other.genre;
        String myPrefGender = this.getCritere(Criteres.PAIR_GENDER);
        String otherPrefGender = other.getCritere(Criteres.PAIR_GENDER);
        
        if (myPrefGender == null || myPrefGender.isEmpty() || myPrefGender.equals(otherGender)) {
            score += 3; // +3 points si ma préférence est satisfaite ou pas de préférence
        }
        
        if (otherPrefGender == null || otherPrefGender.isEmpty() || otherPrefGender.equals(myGender)) {
            score += 3; // +3 points si sa préférence est satisfaite ou pas de préférence
        }
        
        // Bonus pour la différence d'âge
        if (calculerDifferenceAge(other) < 1.5) { // moins de 1 an et demi d'écart
            score += 4;
        }
        
        return score;
    }
    
    /**
     * Calcule la différence d'âge en années entre cet adolescent et un autre
     * @param other l'autre adolescent
     * @return la différence d'âge en années (valeur absolue)
     */
    private double calculerDifferenceAge(Adolescent other) {
        // Plus besoin de conversion ! On utilise directement les LocalDate.
        long days = Math.abs(ChronoUnit.DAYS.between(this.dateNaissance, other.dateNaissance));
        return days / 365.25; // approximation du nombre d'années
    }
}