import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Map;

import main.*;
import static org.junit.jupiter.api.Assertions.*;

public class AdolescentTest {

    // --- Tests pour estCompatibleAllergieAnimal ---

    @Test
    void testCompatibiliteAllergies() {
        // Hôte SANS animal, Visiteur allergique -> Compatible
        Adolescent hote1 = new Adolescent("Hote", "A", "male", "FR", Map.of(Criteres.HOST_HAS_ANIMAL, "no"), LocalDate.now());
        Adolescent visiteur1 = new Adolescent("Visiteur", "X", "female", "IT", Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "yes"), LocalDate.now());
        assertTrue(hote1.CompatibleAnimal(visiteur1), "Hôte sans animal, Visiteur allergique");

        // Hôte AVEC animal, Visiteur NON allergique -> Compatible
        Adolescent hote2 = new Adolescent("Hote", "B", "male", "FR", Map.of(Criteres.HOST_HAS_ANIMAL, "yes"), LocalDate.now());
        Adolescent visiteur2 = new Adolescent("Visiteur", "Y", "female", "IT", Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "no"), LocalDate.now());
        assertTrue(hote2.CompatibleAnimal(visiteur2), "Hôte avec animal, Visiteur non allergique");

        // Hôte SANS animal, Visiteur NON allergique -> Compatible
        Adolescent hote3 = new Adolescent("Hote", "C", "male", "FR", Map.of(Criteres.HOST_HAS_ANIMAL, "no"), LocalDate.now());
        Adolescent visiteur3 = new Adolescent("Visiteur", "Z", "female", "IT", Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "no"), LocalDate.now());
        assertTrue(hote3.CompatibleAnimal(visiteur3), "Hôte sans animal, Visiteur non allergique");

        // Hôte SANS animal, Visiteur sans info allergie (défaut non) -> Compatible
        Adolescent hote4 = new Adolescent("Hote", "D", "male", "FR", Map.of(Criteres.HOST_HAS_ANIMAL, "no"), LocalDate.now());
        Adolescent visiteur4 = new Adolescent("Visiteur", "W", "female", "IT", Map.of(), LocalDate.now()); // Pas d'info allergie
        assertTrue(hote4.CompatibleAnimal(visiteur4), "Hôte sans animal, Visiteur sans info allergie");

        // Hôte sans info animal (défaut non interprété comme false), Visiteur allergique -> Compatible
        Adolescent hote5 = new Adolescent("Hote", "E", "male", "FR", Map.of(), LocalDate.now());
        Adolescent visiteur5 = new Adolescent("Visiteur", "V", "female", "IT", Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "yes"), LocalDate.now());
        assertTrue(hote5.CompatibleAnimal(visiteur5), "Hôte sans info animal, Visiteur allergique");
    }

    @Test
    void testIncompatibiliteAllergies() {
        // Hôte AVEC animal, Visiteur allergique -> Incompatible
        Adolescent hote1 = new Adolescent("Hote", "A", "male", "FR", Map.of(Criteres.HOST_HAS_ANIMAL, "yes"), LocalDate.now());
        Adolescent visiteur1 = new Adolescent("Visiteur", "X", "female", "IT", Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "yes"), LocalDate.now());
        assertFalse(hote1.CompatibleAnimal(visiteur1), "Hôte avec animal, Visiteur allergique");
    }

    // --- Tests pour estCompatibleRegimeAlimentaire ---
    @Test
    void testCompatibiliteRegimeOk() {
        // Hôte accepte vegetarien, Visiteur demande vegetarien -> Compatible
        Adolescent hote1 = new Adolescent("Hote", "A", "male", "FR", Map.of(Criteres.HOST_FOOD, "vegetarian, nonuts"), LocalDate.now());
        Adolescent visiteur1 = new Adolescent("Visiteur", "X", "female", "IT", Map.of(Criteres.GUEST_FOOD, "vegetarian"), LocalDate.now());
        assertTrue(hote1.CompatibleRegime(visiteur1), "Hôte ok pour végétarien, Visiteur végétarien");

        // Hôte accepte vegetarien et sans noix, Visiteur demande vegetarien et sans noix -> Compatible
        Adolescent hote2 = new Adolescent("Hote", "B", "male", "FR", Map.of(Criteres.HOST_FOOD, "vegetarian, nonuts"), LocalDate.now());
        Adolescent visiteur2 = new Adolescent("Visiteur", "Y", "female", "IT", Map.of(Criteres.GUEST_FOOD, "nonuts, vegetarian"), LocalDate.now());
        assertTrue(hote2.CompatibleRegime(visiteur2), "Hôte ok pour plusieurs, Visiteur demande plusieurs");

        // Hôte accepte vegetarien et sans noix, Visiteur demande juste sans noix -> Compatible
        Adolescent hote3 = new Adolescent("Hote", "C", "male", "FR", Map.of(Criteres.HOST_FOOD, "vegetarian, nonuts"), LocalDate.now());
        Adolescent visiteur3 = new Adolescent("Visiteur", "Z", "female", "IT", Map.of(Criteres.GUEST_FOOD, "nonuts"), LocalDate.now());
        assertTrue(hote3.CompatibleRegime(visiteur3), "Hôte ok pour plusieurs, Visiteur demande un seul");
    }

    @Test
    void testCompatibiliteRegimeNon() {
        // Hôte accepte juste vegetarien, Visiteur demande sans noix -> Incompatible
        Adolescent hote1 = new Adolescent("Hote", "A", "male", "FR", Map.of(Criteres.HOST_FOOD, "vegetarian"), LocalDate.now());
        Adolescent visiteur1 = new Adolescent("Visiteur", "X", "female", "IT", Map.of(Criteres.GUEST_FOOD, "nonuts"), LocalDate.now());
        assertFalse(hote1.CompatibleRegime(visiteur1), "Hôte pas ok pour sans noix, Visiteur demande sans noix");

        // Hôte accepte vegetarien, Visiteur demande vegetarien ET sans noix -> Incompatible
        Adolescent hote2 = new Adolescent("Hote", "B", "male", "FR", Map.of(Criteres.HOST_FOOD, "vegetarian"), LocalDate.now());
        Adolescent visiteur2 = new Adolescent("Visiteur", "Y", "female", "IT", Map.of(Criteres.GUEST_FOOD, "vegetarian, nonuts"), LocalDate.now());
        assertFalse(hote2.CompatibleRegime(visiteur2), "Hôte ok pour un seul, Visiteur demande plusieurs");
    }

    @Test
    void testCompatibiliteRegimeVisiteurSansBesoin() {
        // Hôte accepte vegetarien, Visiteur n'a pas de régime -> Compatible
        Adolescent hote1 = new Adolescent("Hote", "A", "male", "FR", Map.of(Criteres.HOST_FOOD, "vegetarian"), LocalDate.now());
        Adolescent visiteur1 = new Adolescent("Visiteur", "X", "female", "IT", Map.of(), LocalDate.now()); // Pas de GUEST_FOOD
        assertTrue(hote1.CompatibleRegime(visiteur1), "Hôte ok végétarien, Visiteur sans régime");

        // Hôte n'accepte rien, Visiteur n'a pas de régime -> Compatible
        Adolescent hote2 = new Adolescent("Hote", "B", "male", "FR", Map.of(), LocalDate.now()); // Pas de HOST_FOOD
        Adolescent visiteur2 = new Adolescent("Visiteur", "Y", "female", "IT", Map.of(), LocalDate.now()); // Pas de GUEST_FOOD
        assertTrue(hote2.CompatibleRegime(visiteur2), "Hôte sans offre, Visiteur sans régime");
    }

    @Test
    void testCompatibiliteRegimeHoteNeProposeRien() {
        // Hôte n'accepte rien (pas de HOST_FOOD), Visiteur demande vegetarien -> Incompatible
        Adolescent hote1 = new Adolescent("Hote", "A", "male", "FR", Map.of(), LocalDate.now());
        Adolescent visiteur1 = new Adolescent("Visiteur", "X", "female", "IT", Map.of(Criteres.GUEST_FOOD, "vegetarian"), LocalDate.now());
        assertFalse(hote1.CompatibleRegime(visiteur1), "Hôte sans offre, Visiteur végétarien");

        // Hôte a HOST_FOOD vide, Visiteur demande vegetarien -> Incompatible
        Adolescent hote2 = new Adolescent("Hote", "B", "male", "FR", Map.of(Criteres.HOST_FOOD, ""), LocalDate.now());
        Adolescent visiteur2 = new Adolescent("Visiteur", "Y", "female", "IT", Map.of(Criteres.GUEST_FOOD, "vegetarian"), LocalDate.now());
        assertFalse(hote2.CompatibleRegime(visiteur2), "Hôte offre vide, Visiteur végétarien");
    }

    // --- Tests pour estCompatible (global) ---
    @Test
    void testCompatibiliteGlobaleOk() {
        // Compatible sur allergie ET régime
        Adolescent hote1 = new Adolescent("Hote", "A", "male", "FR", Map.of(Criteres.HOST_HAS_ANIMAL, "no", Criteres.HOST_FOOD, "vegetarian"), LocalDate.now());
        Adolescent visiteur1 = new Adolescent("Visiteur", "X", "female", "IT", Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "yes", Criteres.GUEST_FOOD, "vegetarian"), LocalDate.now());
        assertTrue(hote1.estCompatible(visiteur1), "Compatible globalement (allergie ok, régime ok)");
    }

    @Test
    void testIncompatibiliteGlobaleAllergie() {
        // Incompatible sur allergie, compatible sur régime
        Adolescent hote1 = new Adolescent("Hote", "A", "male", "FR", Map.of(Criteres.HOST_HAS_ANIMAL, "yes", Criteres.HOST_FOOD, "vegetarian"), LocalDate.now());
        Adolescent visiteur1 = new Adolescent("Visiteur", "X", "female", "IT", Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "yes", Criteres.GUEST_FOOD, "vegetarian"), LocalDate.now());
        assertFalse(hote1.estCompatible(visiteur1), "Incompatible globalement (allergie ko)");
    }

    @Test
    void testIncompatibiliteGlobaleRegime() {
        // Compatible sur allergie, incompatible sur régime
        Adolescent hote1 = new Adolescent("Hote", "A", "male", "FR", Map.of(Criteres.HOST_HAS_ANIMAL, "no", Criteres.HOST_FOOD, "nonuts"), LocalDate.now());
        Adolescent visiteur1 = new Adolescent("Visiteur", "X", "female", "IT", Map.of(Criteres.GUEST_ANIMAL_ALLERGY, "yes", Criteres.GUEST_FOOD, "vegetarian"), LocalDate.now());
        assertFalse(hote1.estCompatible(visiteur1), "Incompatible globalement (régime ko)");
    }
}