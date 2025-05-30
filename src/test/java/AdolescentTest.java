import sae.decision.linguistic.*;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AdolescentTest {

    // --- Tests pour la compatibilité des animaux ---

    @Test
    void testAllergiesCompatibility() {
        // Hôte SANS animal, Visiteur allergique -> Compatible
        Adolescent host1 = new Adolescent("Host", "A", "male", "France", Map.of(Criteria.HOST_HAS_ANIMAL, "no"), LocalDate.now(),true);
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "Italia", Map.of(Criteria.GUEST_ANIMAL_ALLERGY, "yes"), LocalDate.now(),false);
        assertTrue(host1.animalScore(visitor1) == 0, "Hôte sans animal, Visiteur allergique");

        // Hôte AVEC animal, Visiteur NON allergique -> Compatible
        Adolescent host2 = new Adolescent("Host", "B", "male", "France", Map.of(Criteria.HOST_HAS_ANIMAL, "yes"), LocalDate.now(),true);
        Adolescent visitor2 = new Adolescent("Visitor", "Y", "female", "Italia", Map.of(Criteria.GUEST_ANIMAL_ALLERGY, "no"), LocalDate.now(),false);
        assertTrue(host2.animalScore(visitor2) == 0, "Hôte avec animal, Visiteur non allergique");

        // Hôte SANS animal, Visiteur NON allergique -> Compatible
        Adolescent host3 = new Adolescent("Host", "C", "male", "France", Map.of(Criteria.HOST_HAS_ANIMAL, "no"), LocalDate.now(),true);
        Adolescent visitor3 = new Adolescent("Visitor", "Z", "female", "Italia", Map.of(Criteria.GUEST_ANIMAL_ALLERGY, "no"), LocalDate.now(),false);
        assertTrue(host3.animalScore(visitor3) == 0, "Hôte sans animal, Visiteur non allergique");

        // Hôte SANS animal, Visiteur sans info allergie (défaut non) -> Compatible
        Adolescent host4 = new Adolescent("Host", "D", "male", "France", Map.of(Criteria.HOST_HAS_ANIMAL, "no"), LocalDate.now(),true);
        Adolescent visitor4 = new Adolescent("Visitor", "W", "female", "Italia", Map.of(), LocalDate.now(),false); // Pas d'info allergie
        assertTrue(host4.animalScore(visitor4) == 0, "Hôte sans animal, Visiteur sans info allergie");

        // Hôte sans info animal (défaut non interprété comme false), Visiteur allergique -> Compatible
        Adolescent host5 = new Adolescent("Host", "E", "male", "France", Map.of(), LocalDate.now(),true);
        Adolescent visitor5 = new Adolescent("Visitor", "V", "female", "Italia", Map.of(Criteria.GUEST_ANIMAL_ALLERGY, "yes"), LocalDate.now(),false);
        assertTrue(host5.animalScore(visitor5) == 0, "Hôte sans info animal, Visiteur allergique");
    }

    @Test
    void testAllergiesIncompatibility() {
        // Hôte AVEC animal, Visiteur allergique -> Incompatible
        Adolescent host1 = new Adolescent("Host", "A", "male", "France", Map.of(Criteria.HOST_HAS_ANIMAL, "yes"), LocalDate.now(),true);
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "Italia", Map.of(Criteria.GUEST_ANIMAL_ALLERGY, "yes"), LocalDate.now(),false);
        assertFalse(host1.animalScore(visitor1) == 0, "Hôte avec animal, Visiteur allergique");
    }

    // --- Tests pour la compatibilité des régimes alimentaires ---
    @Test
    void testDietCompatibilityOk() {
        // Hôte accepte végétarien, Visiteur demande végétarien -> Compatible
        Adolescent host1 = new Adolescent("Host", "A", "male", "France", Map.of(Criteria.HOST_FOOD, "vegetarian, nonuts"), LocalDate.now(),true);
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "Italia", Map.of(Criteria.GUEST_FOOD, "vegetarian"), LocalDate.now(),false);
        assertTrue(host1.dietScore(visitor1) == 0, "Hôte ok pour végétarien, Visiteur végétarien");

        // Hôte accepte végétarien et sans noix, Visiteur demande végétarien et sans noix -> Compatible
        Adolescent host2 = new Adolescent("Host", "B", "male", "France", Map.of(Criteria.HOST_FOOD, "vegetarian, nonuts"), LocalDate.now(),true);
        Adolescent visitor2 = new Adolescent("Visitor", "Y", "female", "Italia", Map.of(Criteria.GUEST_FOOD, "nonuts, vegetarian"), LocalDate.now(),false);
        assertTrue(host2.dietScore(visitor2) == 0, "Hôte ok pour plusieurs, Visiteur demande plusieurs");

        // Hôte accepte végétarien et sans noix, Visiteur demande juste sans noix -> Compatible
        Adolescent host3 = new Adolescent("Host", "C", "male", "France", Map.of(Criteria.HOST_FOOD, "vegetarian, nonuts"), LocalDate.now(),true);
        Adolescent visitor3 = new Adolescent("Visitor", "Z", "female", "Italia", Map.of(Criteria.GUEST_FOOD, "nonuts"), LocalDate.now(),false);
        assertTrue(host3.dietScore(visitor3) == 0, "Hôte ok pour plusieurs, Visiteur demande un seul");
    }

    @Test
    void testDietIncompatibility() {
        // Hôte accepte juste végétarien, Visiteur demande sans noix -> Incompatible
        Adolescent host1 = new Adolescent("Host", "A", "male", "France", Map.of(Criteria.HOST_FOOD, "vegetarian"), LocalDate.now(),true);
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "Italia", Map.of(Criteria.GUEST_FOOD, "nonuts"), LocalDate.now(),false);
        assertEquals(host1.dietScore(visitor1) ,-10, "Hôte propose régime végétarien Visiteur sans noix");

        // Hôte accepte végétarien, Visiteur demande végétarien ET sans noix -> Incompatible
        Adolescent host2 = new Adolescent("Host", "B", "male", "France", Map.of(Criteria.HOST_FOOD, "vegetarian"), LocalDate.now(),true);
        Adolescent visitor2 = new Adolescent("Visitor", "Y", "female", "Italia", Map.of(Criteria.GUEST_FOOD, "vegetarian, nonuts"), LocalDate.now(),false);
        assertEquals(host2.dietScore(visitor2) , -10, "Hôte ok pour un végétarien, Visiteur demande plusieurs");
    }

    @Test
    void testDietCompatibilityVisitorWithoutNeeds() {
        // Hôte accepte végétarien, Visiteur n'a pas de régime -> Compatible
        Adolescent host1 = new Adolescent("Host", "A", "male", "France", Map.of(Criteria.HOST_FOOD, "vegetarian"), LocalDate.now(),true);
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "Italia", Map.of(), LocalDate.now(),false); // Pas de GUEST_FOOD
        assertEquals(host1.dietScore(visitor1) ,0, "Hôte propose régime végétarien Visiteur n'a besoin de rien");

        // Hôte n'accepte rien, Visiteur n'a pas de régime -> Compatible
        Adolescent host2 = new Adolescent("Host", "B", "male", "France", Map.of(), LocalDate.now(),true); // Pas de HOST_FOOD
        Adolescent visitor2 = new Adolescent("Visitor", "Y", "female", "Italia", Map.of(), LocalDate.now(),false); // Pas de GUEST_FOOD
        assertEquals(host2.dietScore(visitor2) , 0, "Hôte propose un régime vide, Visiteur n'a besoin de rien");
    }

    @Test
    void testDietCompatibilityHostOffersNothing() {
        // Hôte n'accepte rien (pas de HOST_FOOD), Visiteur demande végétarien -> Incompatible
        Adolescent host1 = new Adolescent("Host", "A", "male", "France", Map.of(), LocalDate.now(),true);
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "Italia", Map.of(Criteria.GUEST_FOOD, "vegetarian"), LocalDate.now(),false);
        assertEquals(host1.dietScore(visitor1) ,-10, "Hôte ne propose pas de régimes Visiteur végétarien");

        // Hôte a HOST_FOOD vide, Visiteur demande végétarien -> Incompatible
        Adolescent host2 = new Adolescent("Host", "B", "male", "France", Map.of(Criteria.HOST_FOOD, ""), LocalDate.now(),true);
        Adolescent visitor2 = new Adolescent("Visitor", "Y", "female", "Italia", Map.of(Criteria.GUEST_FOOD, "vegetarian"), LocalDate.now(),false);
        assertEquals(host2.dietScore(visitor2) , -10, "Hôte propose un régime vide, Visiteur végétarien");

        // Hôte n'accepte rien, Visiteur demande végétarien ET sans noix -> Incompatible
        Adolescent host3 = new Adolescent("Host", "B", "male", "France", Map.of(), LocalDate.now(),true);
        Adolescent visitor3 = new Adolescent("Visitor", "Y", "female", "Italia", Map.of(Criteria.GUEST_FOOD, "vegetarian, nonuts"), LocalDate.now(),false);
        assertEquals(host3.dietScore(visitor3) , -20, "Hôte ok pour un végétarien, Visiteur demande plusieurs");
    }

    // --- Tests pour la compatibilité d'historique ---
    @Test
    void testHistoryCompatibilityOk() {
        // Les deux adolescents n'ont pas de contrainte d'historique -> Compatible
        Adolescent host1 = new Adolescent("Host", "A", "male", "France", Map.of(), LocalDate.now(),true);
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "Italia", Map.of(), LocalDate.now(),false);
        assertTrue(host1.isHistoryCompatible(visitor1), "Aucune contrainte d'historique");

        // Les deux adolescents veulent être ensemble (same) -> Compatible
        Adolescent host2 = new Adolescent("Host", "B", "male", "France", Map.of(Criteria.HISTORY, "same"), LocalDate.now(),true);
        Adolescent visitor2 = new Adolescent("Visitor", "Y", "female", "Italia", Map.of(Criteria.HISTORY, "same"), LocalDate.now(),false);
        assertTrue(host2.isHistoryCompatible(visitor2), "Les deux veulent rester ensemble");

        // Un adolescent veut être ensemble (same), l'autre n'a pas de préférence -> Compatible
        Adolescent host3 = new Adolescent("Host", "C", "male", "France", Map.of(Criteria.HISTORY, "same"), LocalDate.now(),true);
        Adolescent visitor3 = new Adolescent("Visitor", "Z", "female", "Italia", Map.of(), LocalDate.now(),false);
        assertTrue(host3.isHistoryCompatible(visitor3), "Un veut rester, l'autre sans préférence");
    }

    @Test
    void testHistoryIncompatibility() {
        // Un adolescent ne veut pas être avec le même (other) -> Incompatible
        Adolescent host1 = new Adolescent("Host", "A", "male", "France", Map.of(Criteria.HISTORY, "other"), LocalDate.now(),true);
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "Italia", Map.of(), LocalDate.now(),false);
        assertFalse(host1.isHistoryCompatible(visitor1), "Hôte veut changer de correspondant");

        // L'autre adolescent ne veut pas être avec le même (other) -> Incompatible
        Adolescent host2 = new Adolescent("Host", "B", "male", "France", Map.of(), LocalDate.now(),true);
        Adolescent visitor2 = new Adolescent("Visitor", "Y", "female", "Italia", Map.of(Criteria.HISTORY, "other"), LocalDate.now(),false);
        assertFalse(host2.isHistoryCompatible(visitor2), "Visiteur veut changer de correspondant");

        // Les deux adolescents ne veulent pas être ensemble (other) -> Incompatible
        Adolescent host3 = new Adolescent("Host", "C", "male", "France", Map.of(Criteria.HISTORY, "other"), LocalDate.now(),true);
        Adolescent visitor3 = new Adolescent("Visitor", "Z", "female", "Italia", Map.of(Criteria.HISTORY, "other"), LocalDate.now(),false);
        assertFalse(host3.isHistoryCompatible(visitor3), "Les deux veulent changer de correspondant");
    }

    // --- Tests pour la compatibilité avec les français ---
    @Test
    void testFrenchCompatibility() {
        // Deux adolescents compatibles avec des hobbies communs
        Adolescent host = new Adolescent("Host", "A", "male", "France",
                Map.of(Criteria.HOST_HAS_ANIMAL, "no",
                        Criteria.HOST_FOOD, "vegetarian",
                        Criteria.HOBBIES, "football,reading,gaming"),
                LocalDate.of(2008, 5, 15),true);

        Adolescent visitor = new Adolescent("Visitor", "X", "female", "Italia",
                Map.of(Criteria.GUEST_ANIMAL_ALLERGY, "no",
                        Criteria.HOBBIES, "dance,gaming,vocaloid"),
                LocalDate.of(2008, 8, 10),false);

        assertTrue(host.isFrenchCompatible(visitor), "Les deux adolescents sont compatibles bien que l'un soit français");
    }

    @Test
    void testFrenchIncompatibility() {
        // Deux adolescents compatibles avec des hobbies communs
        Adolescent host = new Adolescent("Host", "A", "male", "France",
                Map.of(Criteria.HOST_HAS_ANIMAL, "no",
                        Criteria.HOBBIES, "football,swimming,Streaming"),
                LocalDate.of(2008, 5, 15),true);

        Adolescent visitor = new Adolescent("Visitor", "X", "female", "Germany",
                Map.of(Criteria.GUEST_ANIMAL_ALLERGY, "no",
                        Criteria.HOBBIES, "dance,reading,music"),
                LocalDate.of(2008, 8, 10),false);

        assertFalse(host.isFrenchCompatible(visitor), "Les deux adolescents sont incompatibles parce que l'un est français est n'a pas de hobbies communs");
    }

    @Test
    void testHistoryAffinityBonus() {
        // Un adolescent veut être avec le même (same), l'autre n'a pas de préférence -> Bonus d'affinité
        Adolescent host = new Adolescent("Host", "A", "male", "Germany",
                Map.of(Criteria.HISTORY, "same",
                        Criteria.HOST_HAS_ANIMAL, "no",
                        Criteria.HOST_FOOD, "vegetarian"),
                LocalDate.of(2008, 5, 15),true);

        Adolescent visitor = new Adolescent("Visitor", "X", "female", "Italia",
                Map.of(Criteria.GUEST_ANIMAL_ALLERGY, "no",
                        Criteria.GUEST_FOOD, "vegetarian"),
                LocalDate.of(2008, 6, 10),false);

        // Le score devrait inclure un bonus pour l'historique
        int scoreWithHistoryBonus = host.calculateAffinity(visitor);

        // Création d'adolescents identiques mais sans contrainte d'historique
        Adolescent hostNoHistory = new Adolescent("Host", "A", "male", "Germany",
                Map.of(Criteria.HOST_HAS_ANIMAL, "no",
                        Criteria.HOST_FOOD, "vegetarian"),
                LocalDate.of(2008, 5, 15),true);

        Adolescent visitorNoHistory = new Adolescent("Visitor", "X", "female", "Italia",
                Map.of(Criteria.GUEST_ANIMAL_ALLERGY, "no",
                        Criteria.GUEST_FOOD, "vegetarian"),
                LocalDate.of(2008, 6, 10),false);

        int scoreWithoutHistoryBonus = hostNoHistory.calculateAffinity(visitorNoHistory);

        // Vérification que le score avec l'historique est plus élevé
        assertTrue(scoreWithHistoryBonus > scoreWithoutHistoryBonus,
                "Le bonus d'historique devrait augmenter le score d'affinité");
    }

    // --- Tests pour la compatibilité globale ---
    @Test
    void testGlobalCompatibilityOk() {
        // Compatible sur allergie ET régime
        Adolescent host1 = new Adolescent("Host", "A", "male", "Italia", Map.of(Criteria.HOST_HAS_ANIMAL, "no", Criteria.HOST_FOOD, "vegetarian"), LocalDate.now(),true);
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "Italia", Map.of(Criteria.GUEST_ANIMAL_ALLERGY, "yes", Criteria.GUEST_FOOD, "vegetarian"), LocalDate.now(),false);
        assertTrue(host1.isCompatible(visitor1), "Compatible globalement (allergie ok, régime ok)");
    }

    @Test
    void testGlobalIncompatibilityAllergy() {
        // Incompatible sur allergie, compatible sur régime
        Adolescent host1 = new Adolescent("Host", "A", "male", "Belgium", Map.of(Criteria.HOST_HAS_ANIMAL, "yes", Criteria.HOST_FOOD, "vegetarian"), LocalDate.now(),true);
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "Italia", Map.of(Criteria.GUEST_ANIMAL_ALLERGY, "yes", Criteria.GUEST_FOOD, "vegetarian"), LocalDate.now(),false);
        assertFalse(host1.isCompatible(visitor1), "Incompatible globalement (problème allergie)");
    }

    @Test
    void testGlobalIncompatibilityDiet() {
        // Compatible sur allergie, incompatible sur régime
        Adolescent host1 = new Adolescent("Host", "A", "male", "Italia", Map.of(Criteria.HOST_HAS_ANIMAL, "no", Criteria.HOST_FOOD, "nonuts"), LocalDate.now(),true);
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "Italia", Map.of(Criteria.GUEST_ANIMAL_ALLERGY, "yes", Criteria.GUEST_FOOD, "vegetarian"), LocalDate.now(),false);
        assertFalse(host1.isCompatible(visitor1), "Incompatible globalement (problème régime)");
    }

    @Test
    void testGlobalIncompatibilityHistory() {
        // Compatible sur allergie et régime, incompatible sur historique
        Adolescent host1 = new Adolescent("Host", "A", "male", "France",
                Map.of(Criteria.HOST_HAS_ANIMAL, "no",
                        Criteria.HOST_FOOD, "vegetarian",
                        Criteria.HISTORY, "other"),
                LocalDate.now(),true);
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "Italia",
                Map.of(Criteria.GUEST_ANIMAL_ALLERGY, "no",
                        Criteria.GUEST_FOOD, "vegetarian"),
                LocalDate.now(),false);
        assertFalse(host1.isCompatible(visitor1), "Incompatible globalement (problème historique)");
    }

    // --- Tests pour le calcul d'affinité ---
    @Test
    void testAffinityCommonHobbies() {
        // Deux adolescents compatibles avec des hobbies communs
        Adolescent host = new Adolescent("Host", "A", "male", "France",
                Map.of(Criteria.HOST_HAS_ANIMAL, "no",
                        Criteria.HOST_FOOD, "vegetarian",
                        Criteria.HOBBIES, "football,reading,music"),
                LocalDate.of(2008, 5, 15),true);

        Adolescent visitor = new Adolescent("Visitor", "X", "female", "Italia",
                Map.of(Criteria.GUEST_ANIMAL_ALLERGY, "no",
                        Criteria.GUEST_FOOD, "vegetarian",
                        Criteria.HOBBIES, "dance,reading,music"),
                LocalDate.of(2008, 8, 10),false);

        int score = host.calculateAffinity(visitor);
        assertEquals(70, score, "Le score d'affinité devrait être 70 avec 2 hobbies communs et différence d'âge < 1,5 an");
    }

    @Test
    void testAffinityGenderPreference() {
        // Test d'affinité avec préférences de genre satisfaites
        Adolescent host = new Adolescent("Host", "B", "male", "Spain",
                Map.of(Criteria.HOST_HAS_ANIMAL, "no",
                        Criteria.HOST_FOOD, "vegetarian",
                        Criteria.PAIR_GENDER, "female"),
                LocalDate.of(2007, 6, 20),true);

        Adolescent visitor = new Adolescent("Visitor", "Y", "female", "Italia",
                Map.of(Criteria.GUEST_ANIMAL_ALLERGY, "no",
                        Criteria.GUEST_FOOD, "vegetarian",
                        Criteria.PAIR_GENDER, "male"),
                LocalDate.of(2007, 9, 15),false);

        int score = host.calculateAffinity(visitor);
        assertEquals(60, score, "Le score d'affinité devrait être 60 avec préférences de genre satisfaites et différence d'âge < 1,5 an");
    }

    @Test
    void testAffinityIncompatibleTeenagers() {
        // Test d'affinité entre adolescents incompatibles (devrait être 0)
        Adolescent host = new Adolescent("Host", "C", "male", "France",
                Map.of(Criteria.HOST_HAS_ANIMAL, "yes",
                        Criteria.HOST_FOOD, "vegetarian",
                        Criteria.HOBBIES, "football,reading,music"),
                LocalDate.of(2008, 5, 15),true);

        Adolescent visitor = new Adolescent("Visitor", "Z", "female", "Italia",
                Map.of(Criteria.GUEST_ANIMAL_ALLERGY, "yes",
                        Criteria.GUEST_FOOD, "vegetarian",
                        Criteria.HOBBIES, "football,reading,music"),
                LocalDate.of(2008, 7, 10),false);

        int score = host.calculateAffinity(visitor);
        assertEquals(0, score, "Le score d'affinité devrait être 0 pour des adolescents incompatibles");
    }
}