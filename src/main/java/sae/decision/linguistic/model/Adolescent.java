package sae.decision.linguistic.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * Représente un adolescent participant au programme de séjours linguistiques.
 * Cette classe gère les informations personnelles et les critères d'appariement
 * pour chaque participant, qu'il soit hôte ou visiteur.
 * 
 * Les critères d'appariement incluent :
 * - Informations personnelles (nom, prénom, date de naissance)
 * - Pays d'origine
 * - Genre
 * - Critères spécifiques (hobbies, régime alimentaire, animaux, etc.)
 */
public class Adolescent implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Constantes pour la validation et le calcul des scores
    private static final String FRANCE = "France";
    private static final String HOBBY_SEPARATOR = ",";
    
    // Informations personnelles
    private final String lastName;
    private final String firstName;
    private final String countryOfOrigin;
    private final LocalDate dateOfBirth;
    private final boolean isHost;
    
    // Critères d'appariement
    private final Map<Criteria, String> criteria;

    /**
     * Constructeur principal de la classe Adolescent.
     * Initialise un nouvel adolescent avec ses informations personnelles et ses critères.
     * 
     * @param lastName Nom de famille
     * @param firstName Prénom
     * @param gender Genre (male, female, other)
     * @param countryOfOrigin Pays d'origine
     * @param criteria Map des critères d'appariement
     * @param dateOfBirth Date de naissance
     * @param isHost true si l'adolescent est un hôte, false s'il est visiteur
     */
    public Adolescent(String lastName, String firstName, String gender, String countryOfOrigin, 
                     Map<Criteria, String> criteria, LocalDate dateOfBirth, boolean isHost) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.countryOfOrigin = countryOfOrigin;
        this.dateOfBirth = dateOfBirth;
        this.isHost = isHost;
        this.criteria = new HashMap<>();

        initializeGenderCriterion(gender);
        initializeOtherCriteria(criteria, isHost);
    }

    /**
     * Initialise le critère de genre.
     * @param gender Genre de l'adolescent
     */
    private void initializeGenderCriterion(String gender) {
        if (gender != null && !gender.isEmpty()) {
            try {
                if (Criteria.GENDER.isValid(gender)) {
                    this.criteria.put(Criteria.GENDER, gender);
                }
            } catch (IllegalArgumentException e) {
                System.err.println(String.format("Erreur de validation du genre '%s' pour %s %s: %s", 
                    gender, firstName, lastName, e.getMessage()));
            }
        }
    }

    /**
     * Initialise les autres critères d'appariement.
     * @param criteriaMap Map des critères à initialiser
     * @param isHost Indique si l'adolescent est un hôte
     */
    private void initializeOtherCriteria(Map<Criteria, String> criteriaMap, boolean isHost) {
        for (Map.Entry<Criteria, String> entry : criteriaMap.entrySet()) {
            try {
                addCriterion(entry.getKey(), entry.getValue(), isHost);
            } catch (IllegalArgumentException e) {
                System.err.println(String.format("Erreur pour %s %s avec le critère %s et valeur '%s': %s", 
                    firstName, lastName, entry.getKey(), entry.getValue(), e.getMessage()));
            }
        }
    }

    /**
     * Ajoute ou modifie un critère pour cet adolescent.
     * Lance une IllegalArgumentException si le critère est invalide ou viole les règles d'exclusivité.
     * @param criterion le type de critère à ajouter
     * @param value la valeur du critère
     * @param isHost indique si l'adolescent est un hôte (true) ou un visiteur (false)
     * @throws IllegalArgumentException si le critère est invalide ou viole les règles d'exclusivité
     */
    public void addCriterion(Criteria criterion, String value, boolean isHost) throws IllegalArgumentException {
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
    public HashMap<Criteria, String> getCriteria() {
        return new HashMap<>(criteria);
    }

    public boolean isHost() {
        return isHost;
    }

    /**
     * Calcule l'âge actuel de l'adolescent.
     * @return L'âge en années.
     */
    public long getAge() {
        return ChronoUnit.YEARS.between(this.dateOfBirth, LocalDate.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adolescent that = (Adolescent) o;
        return Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    /**
     * Récupère la valeur d'un critère
     * @param criterion le critère à récupérer
     * @return la valeur du critère ou null si non défini
     */
    public String getCriterion(Criteria criterion) {
        return criteria.get(criterion);
    }

    public boolean isCompatible(Adolescent other) {
        // La compatibilité est maintenant déterminée par la nouvelle méthode de calcul.
        // Un score > 0 implique que les contraintes de base sont respectées.
        return calculateAffinityDetails(other).getFinalScore() > 0;
    }

    /**
     * Vérifie la compatibilité d'historique entre deux adolescents.
     * L'appariement est impossible si l'un ou l'autre a spécifié vouloir
     * être avec un correspondant différent de l'année précédente.
     * @param visitor L'adolescent visiteur.
     * @return true si l'historique est compatible, false sinon.
     */
    public boolean isHistoryCompatible(Adolescent visitor) {
        String hostHistory = this.criteria.get(Criteria.HISTORY);
        String visitorHistory = visitor.criteria.get(Criteria.HISTORY);

        // Si l'un des deux veut "other", ils sont incompatibles.
        if ("other".equals(hostHistory) || "other".equals(visitorHistory)) {
            return false;
        }
        return true;
    }

    /**
     * Vérifie la compatibilité spécifique liée à la nationalité française.
     * Si l'un des adolescents est français, ils doivent avoir au moins un hobby en commun.
     * @param visitor L'autre adolescent.
     * @return true si compatible, false sinon.
     */
    public boolean isFrenchCompatible(Adolescent visitor) {
        boolean isHostFrench = FRANCE.equalsIgnoreCase(this.getCountryOfOrigin());
        boolean isVisitorFrench = FRANCE.equalsIgnoreCase(visitor.getCountryOfOrigin());

        if (isHostFrench || isVisitorFrench) {
            String hostHobbiesRaw = this.criteria.getOrDefault(Criteria.HOBBIES, "");
            String visitorHobbiesRaw = visitor.criteria.getOrDefault(Criteria.HOBBIES, "");

            if (hostHobbiesRaw.isEmpty() || visitorHobbiesRaw.isEmpty()) {
                return false; // Pas de hobbies, donc pas de hobbies communs.
            }

            Set<String> hostHobbies = new HashSet<>(Arrays.asList(hostHobbiesRaw.split(HOBBY_SEPARATOR)));
            Set<String> visitorHobbies = new HashSet<>(Arrays.asList(visitorHobbiesRaw.split(HOBBY_SEPARATOR)));

            hostHobbies.retainAll(visitorHobbies);
            return !hostHobbies.isEmpty();
        }

        return true; // Aucun n'est français, la contrainte ne s'applique pas.
    }

    public int calculateAffinity(Adolescent other) {
        return calculateAffinityDetails(other).getFinalScore();
    }

    /**
     * Calcule une analyse détaillée de l'affinité avec un autre adolescent.
     * C'est la méthode centrale pour tous les calculs de compatibilité et de score.
     * @param other L'autre adolescent.
     * @return Un objet AffinityBreakdown contenant tous les détails du calcul.
     */
    public AffinityBreakdown calculateAffinityDetails(Adolescent other) {
        Map<String, Double> componentScores = new HashMap<>();
        Map<String, Boolean> compatibilityChecks = new HashMap<>();

        // 1. Hard compatibility checks
        boolean isDietCompatible = dietScore(other) == 0;
        boolean isAnimalCompatible = animalScore(other) == 0;
        boolean historyCompatible = isHistoryCompatible(other);
        compatibilityChecks.put("diet", isDietCompatible);
        compatibilityChecks.put("animals", isAnimalCompatible);
        compatibilityChecks.put("history", historyCompatible);

        // Si incompatibilité de base, on arrête le calcul et on retourne un score de 0.
        if (!isDietCompatible || !isAnimalCompatible || !historyCompatible) {
            componentScores.put("age", 0.0);
            componentScores.put("gender", 0.0);
            componentScores.put("hobbies", 0.0);
            return new AffinityBreakdown(0, componentScores, compatibilityChecks);
        }
        
        // --- Calcul des scores pondérés ---
        Map<String, Double> weights = ConfigurationManager.getAllWeights();
        double ageWeight = weights.getOrDefault("age", 0.0);
        double genderWeight = weights.getOrDefault("gender", 0.0);
        double hobbiesWeight = weights.getOrDefault("hobbies", 0.0);
        double totalWeight = ageWeight + genderWeight + hobbiesWeight;

        // Score d'âge
        double ageDifference = calculateAgeDifference(other);
        double ageScore = Math.max(0, 100 - ageDifference * 25);
        componentScores.put("age", ageScore);

        // Score de genre
        String myGender = this.getCriterion(Criteria.GENDER);
        String otherGender = other.getCriterion(Criteria.GENDER);
        String myPrefGender = this.getCriterion(Criteria.PAIR_GENDER);
        String otherPrefGender = other.getCriterion(Criteria.PAIR_GENDER);
        double mySatisfaction = (myPrefGender == null || myPrefGender.isEmpty() || myPrefGender.equalsIgnoreCase(otherGender)) ? 100 : 0;
        double otherSatisfaction = (otherPrefGender == null || otherPrefGender.isEmpty() || otherPrefGender.equalsIgnoreCase(myGender)) ? 100 : 0;
        double genderScore = (mySatisfaction + otherSatisfaction) / 2.0;
        componentScores.put("gender", genderScore);

        // Score des hobbies
        String myHobbiesStr = this.getCriterion(Criteria.HOBBIES);
        String otherHobbiesStr = other.getCriterion(Criteria.HOBBIES);
        double hobbiesScore = 0;
        int commonHobbiesCount = 0;
        if (myHobbiesStr != null && !myHobbiesStr.isEmpty() && otherHobbiesStr != null && !otherHobbiesStr.isEmpty()) {
            Set<String> myHobbies = new HashSet<>(Arrays.asList(myHobbiesStr.toLowerCase().split(",")));
            Set<String> otherHobbies = new HashSet<>(Arrays.asList(otherHobbiesStr.toLowerCase().split(",")));
            Set<String> commonHobbies = new HashSet<>(myHobbies);
            commonHobbies.retainAll(otherHobbies);
            commonHobbiesCount = commonHobbies.size();
            hobbiesScore = Math.min(100, commonHobbiesCount * 25);
        }
        componentScores.put("hobbies", hobbiesScore);
        componentScores.put("commonHobbiesCount", (double) commonHobbiesCount);

        // Calcul du score final pondéré
        double weightedScore = 0;
        if (totalWeight > 0) {
            weightedScore = (ageScore * ageWeight + genderScore * genderWeight + hobbiesScore * hobbiesWeight) / totalWeight;
        }

        // Vérification de l'historique
        boolean hasBeenPaired = getHistoryAffinityBonus(other) < 0;
        compatibilityChecks.put("history", !hasBeenPaired);
        if (hasBeenPaired) {
            weightedScore -= 10; // Pénalité pour un appariement précédent
        }

        int finalScore = Math.max(0, Math.min(100, (int) weightedScore));
        
        return new AffinityBreakdown(finalScore, componentScores, compatibilityChecks);
    }

    private int getHistoryAffinityBonus(Adolescent other) {
        String myHistory = this.getCriterion(Criteria.HISTORY);
        String otherHistory = other.getCriterion(Criteria.HISTORY);

        // Si l'un des deux n'a pas d'historique ou si l'historique est vide, on ne peut pas comparer.
        if (myHistory == null || otherHistory == null) {
            return 0;
        }
        
        // On pénalise seulement si les deux ont la même valeur d'historique non nulle.
        if (myHistory.equals(otherHistory)) {
            return -10;
        }
        return 0;
    }

    public int dietScore(Adolescent other) {
        // Vérification des régimes alimentaires
        String hostDiet = this.getCriterion(Criteria.HOST_FOOD);
        String guestDiet = other.getCriterion(Criteria.GUEST_FOOD);

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
     * Vérifie la compatibilité concernant les animaux entre cet adolescent (hôte) et un autre (visiteur).
     * L'adolescent est compatible si le visiteur n'est pas allergique aux animaux OU si l'hôte n'a pas d'animaux.
     * @param other l'adolescent visiteur.
     * @return 0 si compatible concernant les animaux, -25 sinon.
     */
    public int animalScore(Adolescent other) {
        // Vérification des allergies aux animaux
        String hostHasAnimal = this.getCriterion(Criteria.HOST_HAS_ANIMAL);
        String guestAllergy = other.getCriterion(Criteria.GUEST_ANIMAL_ALLERGY);

        if ((guestAllergy != null && guestAllergy.equals("yes")) && (hostHasAnimal != null && hostHasAnimal.equals("yes"))) {
            return -25;
        }
        return 0;
    }

    /**
     * Calcule la différence d'âge en années entre cet adolescent et un autre
     * @param other l'autre adolescent
     * @return la différence d'âge en années (valeur absolue)
     */
    public double calculateAgeDifference(Adolescent other) {
        return Math.abs(ChronoUnit.MONTHS.between(this.dateOfBirth, other.dateOfBirth)) / 12.0;
    }
}