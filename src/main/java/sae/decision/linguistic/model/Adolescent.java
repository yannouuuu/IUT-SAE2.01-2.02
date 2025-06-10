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

import sae.decision.linguistic.service.ConfigurationService;

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
    // Nom du précédent partenaire
    private String previousPartnerFullName;

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

        this.previousPartnerFullName = null;
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

        // 3. Vérifier l'exclusivité HOST/GUEST seulement si la compatibilité stricte est activée
        if (ConfigurationService.isStrictCompatibility("host_guest")) {
            boolean isGuestCriterion = criterion.name().startsWith("GUEST_");
            boolean isHostCriterion = criterion.name().startsWith("HOST_");

            if (isGuestCriterion && isHost==true) {
                throw new IllegalArgumentException("Conflit de critère: Impossible d'ajouter " + criterion +
                        " car l'adolescent est un HOST");

            } else if (isHostCriterion && isHost==false) {
                throw new IllegalArgumentException("Conflit de critère: Impossible d'ajouter " + criterion +
                        " car l'adolescent est un GUEST");
            }
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

    public String getPreviousPartnerFullName() {
        return previousPartnerFullName;
    }

    public void setPreviousPartnerFullName(String previousPartnerFullName) {
        this.previousPartnerFullName = previousPartnerFullName;
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
        return Objects.equals(this.firstName, that.getFirstName()) &&
                Objects.equals(this.lastName, that.getLastName());
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
        // La compatibilité est maintenant déterminée par la configuration et le score calculé
        double minScore = ConfigurationService.getDouble("threshold.min_score_for_compatibility");
        return calculateAffinityDetails(other).getFinalScore() > minScore;
    }

    /**
     * Vérifie la compatibilité d'historique entre deux adolescents.
     * L'appariement est impossible si :
     * - Un adolescent a le critère HISTORY "other" et que le partenaire actuel est son précédent partenaire.
     * - Un adolescent a le critère HISTORY "same" et que le partenaire actuel n'est PAS son précédent partenaire.
     * @param other L'autre adolescent.
     * @return true si l'historique est compatible, false sinon.
     */
    public boolean isHistoryCompatible(Adolescent other) {
        // Si la compatibilité stricte de l'historique est désactivée, toujours compatible
        if (!ConfigurationService.isStrictCompatibility("history")) {
            return true;
        }

        String myHistoryPreference = this.getCriterion(Criteria.HISTORY);
        String otherHistoryPreference = other.getCriterion(Criteria.HISTORY);
		
		// Vérifier si l'adolescent actuel est mon précédent partenaire et je n'en veux pas.
        if (myHistoryPreference != null && myHistoryPreference.startsWith("other:") && this.previousPartnerFullName != null && this.previousPartnerFullName.equals(other.toString())) {
                return false; 
            }   
		// Vérifier si "je" suis son ancien partenaire et il ne veut pas de "moi"
		if (otherHistoryPreference != null && otherHistoryPreference.startsWith("other:") && other.previousPartnerFullName != null && other.previousPartnerFullName.equals(this.toString())) {
                return false; 
            }
        
        // Vérifier si le partenaire actuel est mon précédent partenaire.
        if (myHistoryPreference != null && myHistoryPreference.startsWith("same:")) {
            if (this.previousPartnerFullName != null && !this.previousPartnerFullName.equals(other.toString())) {
                return false; 
            }
        }
		// Il veut son ancien partenaire, mais ce n'est pas moi.
        if (otherHistoryPreference != null && otherHistoryPreference.startsWith("same:")) {
            if (other.previousPartnerFullName != null && !other.previousPartnerFullName.equals(this.toString())) {
                return false; 
            }
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
        // Vérifier si cette règle est activée dans la configuration
        if (!ConfigurationService.getBoolean("threshold.french_hobby_required")) {
            return true;
        }

        boolean isHostFrench = FRANCE.equalsIgnoreCase(this.getCountryOfOrigin());
        boolean isVisitorFrench = FRANCE.equalsIgnoreCase(visitor.getCountryOfOrigin());

        if (isHostFrench || isVisitorFrench) {
            String hostHobbiesRaw = this.criteria.getOrDefault(Criteria.HOBBIES, "");
            String visitorHobbiesRaw = visitor.getCriteria().getOrDefault(Criteria.HOBBIES, "");

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

        // 1. Hard compatibility checks (basés sur la configuration)
        boolean isDietCompatible = true;
        boolean isAnimalCompatible = true;
        boolean historyStrictlyCompatible = true; // Renommé pour plus de clarté
        boolean isFrenchCompatible = true;
        
        if (ConfigurationService.isStrictCompatibility("french_hobby_required")) {
            isFrenchCompatible = isFrenchCompatible(other);
        }

        if (ConfigurationService.isStrictCompatibility("diet")) {
            isDietCompatible = dietScore(other) == 0;
        }

        if (ConfigurationService.isStrictCompatibility("animal")) {
            isAnimalCompatible = animalScore(other) == 0;
        }

        if (ConfigurationService.isStrictCompatibility("history")) {
            historyStrictlyCompatible = isHistoryCompatible(other); 
        }

        compatibilityChecks.put("diet", isDietCompatible);
        compatibilityChecks.put("animals", isAnimalCompatible);
        compatibilityChecks.put("history", historyStrictlyCompatible); 
        compatibilityChecks.put("french_hobby_required", isFrenchCompatible);

        // Si incompatibilité de base (et compatibilité stricte activée), on arrête le calcul
        if ((ConfigurationService.isStrictCompatibility("diet") && !isDietCompatible) ||
            (ConfigurationService.isStrictCompatibility("french_hobby_required") && !isFrenchCompatible) ||
            (ConfigurationService.isStrictCompatibility("animal") && !isAnimalCompatible) ||
            (ConfigurationService.isStrictCompatibility("history") && !historyStrictlyCompatible)) { 
            componentScores.put("age", 0.0);
            componentScores.put("gender", 0.0);
            componentScores.put("hobbies", 0.0);
            return new AffinityBreakdown(0, componentScores, compatibilityChecks);
        }

        // --- Calcul des scores pondérés ---
        Map<String, Double> weights = ConfigurationService.getAllWeights();
        double ageWeight = weights.getOrDefault("age", 0.0);
        double genderWeight = weights.getOrDefault("gender", 0.0);
        double hobbiesWeight = weights.getOrDefault("hobbies", 0.0);
        double totalWeight = ageWeight + genderWeight + hobbiesWeight;

        // Score d'âge (utilise les paramètres configurables)
        double ageDifference = calculateAgeDifference(other);
        double ageBaseScore = ConfigurationService.getDouble("age.base_score");
        double ageMultiplier = ConfigurationService.getDouble("age.multiplier");
        double ageScore = Math.max(0, ageBaseScore - (ageDifference * ageMultiplier));
        componentScores.put("age", ageScore);

        // Score de genre
        String myGender = this.getCriterion(Criteria.GENDER);
        String otherGender = other.getCriterion(Criteria.GENDER);
        String myPrefGender = this.getCriterion(Criteria.PAIR_GENDER);
        String otherPrefGender = other.getCriterion(Criteria.PAIR_GENDER);
        double mySatisfaction = (myPrefGender == null || myPrefGender.isEmpty() || myPrefGender.equalsIgnoreCase(otherGender)) ? 10 : 0;
        double otherSatisfaction = (otherPrefGender == null || otherPrefGender.isEmpty() || otherPrefGender.equalsIgnoreCase(myGender)) ? 10 : 0;
        double genderScore = (mySatisfaction + otherSatisfaction);
        componentScores.put("gender", genderScore);

        // Score des hobbies (utilise les paramètres configurables)
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

            double pointsPerCommon = ConfigurationService.getDouble("hobbies.points_per_common");
            double maxHobbiesScore = ConfigurationService.getDouble("hobbies.max_score");
            hobbiesScore = Math.min(maxHobbiesScore, commonHobbiesCount * pointsPerCommon);
        }
        componentScores.put("hobbies", hobbiesScore);
        componentScores.put("commonHobbiesCount", (double) commonHobbiesCount);

        // Calcul du score final pondéré
        double weightedScore = 50; //50 de base pour le score d'affinité
        if (totalWeight > 0) {
            weightedScore += (ageScore * ageWeight + genderScore * genderWeight + hobbiesScore * hobbiesWeight) / totalWeight;
        }

        // Ajustements basés sur les pénalités configurables
        if (!ConfigurationService.isStrictCompatibility("diet")) {
            // Appliques les pénalités liées aux régime
            weightedScore += dietScore(other);
        }

        if (!ConfigurationService.isStrictCompatibility("animal")) {
            // Applique les pénalités liées aux animaux
            weightedScore += animalScore(other);
        }

        // Gestion du bonus/malus d'historique (non strict)
        if (!ConfigurationService.isStrictCompatibility("history")) {
            weightedScore += getHistoryAffinityBonus(other); 
        }


        // Appliquer les limites configurables du score
        double minScore = ConfigurationService.getDouble("score.min");
        double maxScore = ConfigurationService.getDouble("score.max");
        int finalScore = (int) Math.max(minScore, Math.min(maxScore, weightedScore));

        return new AffinityBreakdown(finalScore, componentScores, compatibilityChecks);
    }

    /**
     * Calcule le bonus/malus d'affinité basé sur l'historique d'appariement.
     * Ce bonus/malus est appliqué si la compatibilité stricte de l'historique est désactivée.
     * @param other L'autre adolescent.
     * @return Le bonus ou la pénalité à ajouter au score.
     */
    private int getHistoryAffinityBonus(Adolescent other) {
        String myHistoryPreference = this.getCriterion(Criteria.HISTORY);
        String otherHistoryPreference = other.getCriterion(Criteria.HISTORY);

        int bonusHistory = 0;

        if (myHistoryPreference != null) {
            if (otherHistoryPreference != null && otherHistoryPreference.startsWith("same:")) {
                // this=same other=same
                bonusHistory += (int) ConfigurationService.getDouble("bonus.history_same");
            }else if (otherHistoryPreference == null){
				// this=same other=null
                bonusHistory += (int) ConfigurationService.getDouble("bonus.one_history_same");
            }
        } else if (otherHistoryPreference != null && otherHistoryPreference.startsWith("same:")) {
            // this=other other=same
            bonusHistory += (int) ConfigurationService.getDouble("bonus.one_history_same");
        }
        //this=null other=null
        return bonusHistory; 
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
        if ((hostDiet == null || hostDiet.isEmpty()) && !guestDiet.isEmpty()) {
            // On compte le nombre de régimes demandés par le visiteur comme incompatibles
            int incompatiblesDiets = 0;
            String[] guestDiets = guestDiet.split(",");
            double dietPenalty = ConfigurationService.getDouble("penalty.diet_incompatible");
            for (String diet : guestDiets) {
                if (diet != null && !diet.trim().isEmpty()) {
                    incompatiblesDiets += (int) dietPenalty;
                }
            }
            return incompatiblesDiets;
        }

        Set<String> hostDietsSet = new HashSet<>();
        String[] hostDiets = hostDiet.split(",");
        for (String diet : hostDiets) {
            hostDietsSet.add(diet.trim());
        }

        int incompatiblesDiets = 0;
        double dietPenalty = ConfigurationService.getDouble("penalty.diet_incompatible");
        String[] guestDiets = guestDiet.split(",");
        for (String diet : guestDiets) {
            if (!hostDietsSet.contains(diet.trim())) {
                incompatiblesDiets += (int) dietPenalty;
            }
        }

        return incompatiblesDiets;
    }

    /**
     * Vérifie la compatibilité concernant les animaux entre cet adolescent (hôte) et un autre (visiteur).
     * L'adolescent est compatible si le visiteur n'est pas allergique aux animaux OU si l'hôte n'a pas d'animaux.
     * @param other l'adolescent visiteur.
     * @return 0 si compatible concernant les animaux, pénalité configurée sinon.
     */
    public int animalScore(Adolescent other) {
        // Vérification des allergies aux animaux
        String hostHasAnimal = this.getCriterion(Criteria.HOST_HAS_ANIMAL);
        String guestAllergy = other.getCriterion(Criteria.GUEST_ANIMAL_ALLERGY);

        if ((guestAllergy != null && guestAllergy.equals("yes")) && (hostHasAnimal != null && hostHasAnimal.equals("yes"))) {
            return (int) ConfigurationService.getDouble("penalty.animal_allergy");
        }
        return 0;
    }

    /**
     * Calcule la différence d'âge en années entre cet adolescent et un autre
     * @param other l'autre adolescent
     * @return la différence d'âge en années (valeur absolue)
     */
    public double calculateAgeDifference(Adolescent other) {
        return Math.abs(ChronoUnit.MONTHS.between(this.dateOfBirth, other.getDateOfBirth())) / 12.0;
    }
}