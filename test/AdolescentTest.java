import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Map;

import main.*;
import static org.junit.jupiter.api.Assertions.*;

public class AdolescentTest {

    // --- Tests pour la compatibilité des animaux ---

    @Test
    void testAllergiesCompatibility() {
        // Hôte SANS animal, Visiteur allergique -> Compatible
        Adolescent host1 = new Adolescent("Host", "A", "male", "FR", Map.of(Criteres.HOST_HAS_ANIMAL, "no"), LocalDate.now());
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "IT", Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "yes"), LocalDate.now());
        assertTrue(host1.isAnimalCompatible(visitor1), "Hôte sans animal, Visiteur allergique");

        // Hôte AVEC animal, Visiteur NON allergique -> Compatible
        Adolescent host2 = new Adolescent("Host", "B", "male", "FR", Map.of(Criteres.HOST_HAS_ANIMAL, "yes"), LocalDate.now());
        Adolescent visitor2 = new Adolescent("Visitor", "Y", "female", "IT", Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "no"), LocalDate.now());
        assertTrue(host2.isAnimalCompatible(visitor2), "Hôte avec animal, Visiteur non allergique");

        // Hôte SANS animal, Visiteur NON allergique -> Compatible
        Adolescent host3 = new Adolescent("Host", "C", "male", "FR", Map.of(Criteres.HOST_HAS_ANIMAL, "no"), LocalDate.now());
        Adolescent visitor3 = new Adolescent("Visitor", "Z", "female", "IT", Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "no"), LocalDate.now());
        assertTrue(host3.isAnimalCompatible(visitor3), "Hôte sans animal, Visiteur non allergique");

        // Hôte SANS animal, Visiteur sans info allergie (défaut non) -> Compatible
        Adolescent host4 = new Adolescent("Host", "D", "male", "FR", Map.of(Criteres.HOST_HAS_ANIMAL, "no"), LocalDate.now());
        Adolescent visitor4 = new Adolescent("Visitor", "W", "female", "IT", Map.of(), LocalDate.now()); // Pas d'info allergie
        assertTrue(host4.isAnimalCompatible(visitor4), "Hôte sans animal, Visiteur sans info allergie");

        // Hôte sans info animal (défaut non interprété comme false), Visiteur allergique -> Compatible
        Adolescent host5 = new Adolescent("Host", "E", "male", "FR", Map.of(), LocalDate.now());
        Adolescent visitor5 = new Adolescent("Visitor", "V", "female", "IT", Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "yes"), LocalDate.now());
        assertTrue(host5.isAnimalCompatible(visitor5), "Hôte sans info animal, Visiteur allergique");
    }

    @Test
    void testAllergiesIncompatibility() {
        // Hôte AVEC animal, Visiteur allergique -> Incompatible
        Adolescent host1 = new Adolescent("Host", "A", "male", "FR", Map.of(Criteres.HOST_HAS_ANIMAL, "yes"), LocalDate.now());
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "IT", Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "yes"), LocalDate.now());
        assertFalse(host1.isAnimalCompatible(visitor1), "Hôte avec animal, Visiteur allergique");
    }

    // --- Tests pour la compatibilité des régimes alimentaires ---
    @Test
    void testDietCompatibilityOk() {
        // Hôte accepte végétarien, Visiteur demande végétarien -> Compatible
        Adolescent host1 = new Adolescent("Host", "A", "male", "FR", Map.of(Criteres.HOST_FOOD, "vegetarian, nonuts"), LocalDate.now());
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "IT", Map.of(Criteres.GUEST_FOOD, "vegetarian"), LocalDate.now());
        assertTrue(host1.isDietCompatible(visitor1), "Hôte ok pour végétarien, Visiteur végétarien");

        // Hôte accepte végétarien et sans noix, Visiteur demande végétarien et sans noix -> Compatible
        Adolescent host2 = new Adolescent("Host", "B", "male", "FR", Map.of(Criteres.HOST_FOOD, "vegetarian, nonuts"), LocalDate.now());
        Adolescent visitor2 = new Adolescent("Visitor", "Y", "female", "IT", Map.of(Criteres.GUEST_FOOD, "nonuts, vegetarian"), LocalDate.now());
        assertTrue(host2.isDietCompatible(visitor2), "Hôte ok pour plusieurs, Visiteur demande plusieurs");

        // Hôte accepte végétarien et sans noix, Visiteur demande juste sans noix -> Compatible
        Adolescent host3 = new Adolescent("Host", "C", "male", "FR", Map.of(Criteres.HOST_FOOD, "vegetarian, nonuts"), LocalDate.now());
        Adolescent visitor3 = new Adolescent("Visitor", "Z", "female", "IT", Map.of(Criteres.GUEST_FOOD, "nonuts"), LocalDate.now());
        assertTrue(host3.isDietCompatible(visitor3), "Hôte ok pour plusieurs, Visiteur demande un seul");
    }

    @Test
    void testDietIncompatibility() {
        // Hôte accepte juste végétarien, Visiteur demande sans noix -> Incompatible
        Adolescent host1 = new Adolescent("Host", "A", "male", "FR", Map.of(Criteres.HOST_FOOD, "vegetarian"), LocalDate.now());
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "IT", Map.of(Criteres.GUEST_FOOD, "nonuts"), LocalDate.now());
        assertFalse(host1.isDietCompatible(visitor1), "Hôte pas ok pour sans noix, Visiteur demande sans noix");

        // Hôte accepte végétarien, Visiteur demande végétarien ET sans noix -> Incompatible
        Adolescent host2 = new Adolescent("Host", "B", "male", "FR", Map.of(Criteres.HOST_FOOD, "vegetarian"), LocalDate.now());
        Adolescent visitor2 = new Adolescent("Visitor", "Y", "female", "IT", Map.of(Criteres.GUEST_FOOD, "vegetarian, nonuts"), LocalDate.now());
        assertFalse(host2.isDietCompatible(visitor2), "Hôte ok pour un seul, Visiteur demande plusieurs");
    }

    @Test
    void testDietCompatibilityVisitorWithoutNeeds() {
        // Hôte accepte végétarien, Visiteur n'a pas de régime -> Compatible
        Adolescent host1 = new Adolescent("Host", "A", "male", "FR", Map.of(Criteres.HOST_FOOD, "vegetarian"), LocalDate.now());
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "IT", Map.of(), LocalDate.now()); // Pas de GUEST_FOOD
        assertTrue(host1.isDietCompatible(visitor1), "Hôte ok végétarien, Visiteur sans régime");

        // Hôte n'accepte rien, Visiteur n'a pas de régime -> Compatible
        Adolescent host2 = new Adolescent("Host", "B", "male", "FR", Map.of(), LocalDate.now()); // Pas de HOST_FOOD
        Adolescent visitor2 = new Adolescent("Visitor", "Y", "female", "IT", Map.of(), LocalDate.now()); // Pas de GUEST_FOOD
        assertTrue(host2.isDietCompatible(visitor2), "Hôte sans offre, Visiteur sans régime");
    }

    @Test
    void testDietCompatibilityHostOffersNothing() {
        // Hôte n'accepte rien (pas de HOST_FOOD), Visiteur demande végétarien -> Incompatible
        Adolescent host1 = new Adolescent("Host", "A", "male", "FR", Map.of(), LocalDate.now());
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "IT", Map.of(Criteres.GUEST_FOOD, "vegetarian"), LocalDate.now());
        assertFalse(host1.isDietCompatible(visitor1), "Hôte sans offre, Visiteur végétarien");

        // Hôte a HOST_FOOD vide, Visiteur demande végétarien -> Incompatible
        Adolescent host2 = new Adolescent("Host", "B", "male", "FR", Map.of(Criteres.HOST_FOOD, ""), LocalDate.now());
        Adolescent visitor2 = new Adolescent("Visitor", "Y", "female", "IT", Map.of(Criteres.GUEST_FOOD, "vegetarian"), LocalDate.now());
        assertFalse(host2.isDietCompatible(visitor2), "Hôte offre vide, Visiteur végétarien");
    }

    // --- Tests pour la compatibilité d'historique ---
    @Test
    void testHistoryCompatibilityOk() {
        // Les deux adolescents n'ont pas de contrainte d'historique -> Compatible
        Adolescent host1 = new Adolescent("Host", "A", "male", "FR", Map.of(), LocalDate.now());
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "IT", Map.of(), LocalDate.now());
        assertTrue(host1.isHistoryCompatible(visitor1), "Aucune contrainte d'historique");

        // Les deux adolescents veulent être ensemble (same) -> Compatible
        Adolescent host2 = new Adolescent("Host", "B", "male", "FR", Map.of(Criteres.HISTORY, "same"), LocalDate.now());
        Adolescent visitor2 = new Adolescent("Visitor", "Y", "female", "IT", Map.of(Criteres.HISTORY, "same"), LocalDate.now());
        assertTrue(host2.isHistoryCompatible(visitor2), "Les deux veulent rester ensemble");

        // Un adolescent veut être ensemble (same), l'autre n'a pas de préférence -> Compatible
        Adolescent host3 = new Adolescent("Host", "C", "male", "FR", Map.of(Criteres.HISTORY, "same"), LocalDate.now());
        Adolescent visitor3 = new Adolescent("Visitor", "Z", "female", "IT", Map.of(), LocalDate.now());
        assertTrue(host3.isHistoryCompatible(visitor3), "Un veut rester, l'autre sans préférence");
    }

    @Test
    void testHistoryIncompatibility() {
        // Un adolescent ne veut pas être avec le même (other) -> Incompatible
        Adolescent host1 = new Adolescent("Host", "A", "male", "FR", Map.of(Criteres.HISTORY, "other"), LocalDate.now());
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "IT", Map.of(), LocalDate.now());
        assertFalse(host1.isHistoryCompatible(visitor1), "Hôte veut changer de correspondant");

        // L'autre adolescent ne veut pas être avec le même (other) -> Incompatible
        Adolescent host2 = new Adolescent("Host", "B", "male", "FR", Map.of(), LocalDate.now());
        Adolescent visitor2 = new Adolescent("Visitor", "Y", "female", "IT", Map.of(Criteres.HISTORY, "other"), LocalDate.now());
        assertFalse(host2.isHistoryCompatible(visitor2), "Visiteur veut changer de correspondant");

        // Les deux adolescents ne veulent pas être ensemble (other) -> Incompatible
        Adolescent host3 = new Adolescent("Host", "C", "male", "FR", Map.of(Criteres.HISTORY, "other"), LocalDate.now());
        Adolescent visitor3 = new Adolescent("Visitor", "Z", "female", "IT", Map.of(Criteres.HISTORY, "other"), LocalDate.now());
        assertFalse(host3.isHistoryCompatible(visitor3), "Les deux veulent changer de correspondant");
    }

    @Test
    void testHistoryAffinityBonus() {
        // Un adolescent veut être avec le même (same), l'autre n'a pas de préférence -> Bonus d'affinité
        Adolescent host = new Adolescent("Host", "A", "male", "FR", 
            Map.of(Criteres.HISTORY, "same", 
                   Criteres.HOST_HAS_ANIMAL, "no", 
                   Criteres.HOST_FOOD, "vegetarian"), 
            LocalDate.of(2008, 5, 15));
        
        Adolescent visitor = new Adolescent("Visitor", "X", "female", "IT", 
            Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "no", 
                   Criteres.GUEST_FOOD, "vegetarian"), 
            LocalDate.of(2008, 6, 10));
        
        // Le score devrait inclure un bonus pour l'historique
        int scoreWithHistoryBonus = host.calculateAffinity(visitor);
        
        // Création d'adolescents identiques mais sans contrainte d'historique
        Adolescent hostNoHistory = new Adolescent("Host", "A", "male", "FR", 
            Map.of(Criteres.HOST_HAS_ANIMAL, "no", 
                   Criteres.HOST_FOOD, "vegetarian"), 
            LocalDate.of(2008, 5, 15));
        
        Adolescent visitorNoHistory = new Adolescent("Visitor", "X", "female", "IT", 
            Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "no", 
                   Criteres.GUEST_FOOD, "vegetarian"), 
            LocalDate.of(2008, 6, 10));
        
        int scoreWithoutHistoryBonus = hostNoHistory.calculateAffinity(visitorNoHistory);
        
        // Vérification que le score avec l'historique est plus élevé
        assertTrue(scoreWithHistoryBonus > scoreWithoutHistoryBonus, 
            "Le bonus d'historique devrait augmenter le score d'affinité");
    }

    // --- Tests pour la compatibilité globale ---
    @Test
    void testGlobalCompatibilityOk() {
        // Compatible sur allergie ET régime
        Adolescent host1 = new Adolescent("Host", "A", "male", "FR", Map.of(Criteres.HOST_HAS_ANIMAL, "no", Criteres.HOST_FOOD, "vegetarian"), LocalDate.now());
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "IT", Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "yes", Criteres.GUEST_FOOD, "vegetarian"), LocalDate.now());
        assertTrue(host1.isCompatible(visitor1), "Compatible globalement (allergie ok, régime ok)");
    }

    @Test
    void testGlobalIncompatibilityAllergy() {
        // Incompatible sur allergie, compatible sur régime
        Adolescent host1 = new Adolescent("Host", "A", "male", "FR", Map.of(Criteres.HOST_HAS_ANIMAL, "yes", Criteres.HOST_FOOD, "vegetarian"), LocalDate.now());
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "IT", Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "yes", Criteres.GUEST_FOOD, "vegetarian"), LocalDate.now());
        assertFalse(host1.isCompatible(visitor1), "Incompatible globalement (problème allergie)");
    }

    @Test
    void testGlobalIncompatibilityDiet() {
        // Compatible sur allergie, incompatible sur régime
        Adolescent host1 = new Adolescent("Host", "A", "male", "FR", Map.of(Criteres.HOST_HAS_ANIMAL, "no", Criteres.HOST_FOOD, "nonuts"), LocalDate.now());
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "IT", Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "yes", Criteres.GUEST_FOOD, "vegetarian"), LocalDate.now());
        assertFalse(host1.isCompatible(visitor1), "Incompatible globalement (problème régime)");
    }

    @Test
    void testGlobalIncompatibilityHistory() {
        // Compatible sur allergie et régime, incompatible sur historique
        Adolescent host1 = new Adolescent("Host", "A", "male", "FR", 
            Map.of(Criteres.HOST_HAS_ANIMAL, "no", 
                   Criteres.HOST_FOOD, "vegetarian",
                   Criteres.HISTORY, "other"), 
            LocalDate.now());
        Adolescent visitor1 = new Adolescent("Visitor", "X", "female", "IT", 
            Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "no", 
                   Criteres.GUEST_FOOD, "vegetarian"), 
            LocalDate.now());
        assertFalse(host1.isCompatible(visitor1), "Incompatible globalement (problème historique)");
    }

    // --- Tests pour le calcul d'affinité ---

    @Test
    void testAffinityCommonHobbies() {
        // Deux adolescents compatibles avec des hobbies communs
        Adolescent host = new Adolescent("Host", "A", "male", "FR", 
            Map.of(Criteres.HOST_HAS_ANIMAL, "no", 
                   Criteres.HOST_FOOD, "vegetarian", 
                   Criteres.HOBBIES, "football,reading,music"), 
            LocalDate.of(2008, 5, 15));
        
        Adolescent visitor = new Adolescent("Visitor", "X", "female", "IT", 
            Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "no", 
                   Criteres.GUEST_FOOD, "vegetarian", 
                   Criteres.HOBBIES, "dance,reading,music"), 
            LocalDate.of(2008, 8, 10));
        
        int score = host.calculateAffinity(visitor);
        assertEquals(20, score, "Le score d'affinité devrait être 20 avec 2 hobbies communs et différence d'âge < 1,5 an");
    }

    @Test
    void testAffinityGenderPreference() {
        // Test d'affinité avec préférences de genre satisfaites
        Adolescent host = new Adolescent("Host", "B", "male", "FR", 
            Map.of(Criteres.HOST_HAS_ANIMAL, "no", 
                   Criteres.HOST_FOOD, "vegetarian", 
                   Criteres.PAIR_GENDER, "female"), 
            LocalDate.of(2007, 6, 20));
        
        Adolescent visitor = new Adolescent("Visitor", "Y", "female", "IT", 
            Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "no", 
                   Criteres.GUEST_FOOD, "vegetarian", 
                   Criteres.PAIR_GENDER, "male"), 
            LocalDate.of(2007, 9, 15));
        
        int score = host.calculateAffinity(visitor);
        assertEquals(10, score, "Le score d'affinité devrait être 10 avec préférences de genre satisfaites et différence d'âge < 1,5 an");
    }

    @Test
    void testAffinityIncompatibleTeenagers() {
        // Test d'affinité entre adolescents incompatibles (devrait être 0)
        Adolescent host = new Adolescent("Host", "C", "male", "FR", 
            Map.of(Criteres.HOST_HAS_ANIMAL, "yes", 
                   Criteres.HOST_FOOD, "vegetarian", 
                   Criteres.HOBBIES, "football,reading,music"), 
            LocalDate.of(2008, 5, 15));
        
        Adolescent visitor = new Adolescent("Visitor", "Z", "female", "IT", 
            Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "yes", 
                   Criteres.GUEST_FOOD, "vegetarian", 
                   Criteres.HOBBIES, "football,reading,music"), 
            LocalDate.of(2008, 7, 10));
        
        int score = host.calculateAffinity(visitor);
        assertEquals(0, score, "Le score d'affinité devrait être 0 pour des adolescents incompatibles");
    }
}