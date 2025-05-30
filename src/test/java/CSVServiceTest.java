import sae.decision.linguistic.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CSVServiceTest {

    private CSVService csvService;
    @TempDir
    Path tempDir;
    private Path sampleHostsCsv;
    private Path sampleGuestsCsv;
    private Path outputCsv;

    @BeforeEach
    void setUp() throws IOException {
        csvService = new CSVService();
        sampleHostsCsv = tempDir.resolve("testhosts.csv");
        sampleGuestsCsv = tempDir.resolve("testguests.csv");
        outputCsv = tempDir.resolve("output_pairings.csv");

        // Créer un fichier CSV d'hôtes exemple
        List<String> hostLines = new ArrayList<>();
        hostLines.add("NAME;FORENAME;COUNTRY;BIRTH_DATE;GENDER;HOST_HAS_ANIMAL;HOST_FOOD;HOBBIES;HISTORY");
        hostLines.add("HostOne;Eva;France;2008-01-15;female;no;vegetarian;reading,music;");
        hostLines.add("HostTwo;Adam;France;2007-07-20;male;yes;nonuts;sports;same");
        Files.write(sampleHostsCsv, hostLines);

        // Créer un fichier CSV de visiteurs exemple
        List<String> guestLines = new ArrayList<>();
        guestLines.add("NAME;FORENAME;COUNTRY;BIRTH_DATE;GENDER;GUEST_ANIMAL_ALLERGY;GUEST_FOOD;HOBBIES;PAIR_GENDER");
        guestLines.add("VisitorOne;Leo;Germany;2008-03-22;male;no;vegetarian;music,games;female");
        guestLines.add("VisitorTwo;Mia;Germany;2007-09-01;female;yes;;sports,reading;"); // GUEST_FOOD vide
        Files.write(sampleGuestsCsv, guestLines);
    }

    @Test
    void testImportAdolescentsHosts() {
        List<Adolescent> hosts = csvService.importAdolescents(sampleHostsCsv.toString(), true);
        assertEquals(2, hosts.size());

        Adolescent host1 = hosts.get(0);
        assertEquals("HostOne", host1.getLastName());
        assertEquals("Eva", host1.getFirstName());
        assertEquals("France", host1.getCountryOfOrigin());
        assertEquals(LocalDate.of(2008, 1, 15), host1.getDateOfBirth());
        assertEquals("female", host1.getCriterion(Criteria.GENDER));
        assertEquals("no", host1.getCriterion(Criteria.HOST_HAS_ANIMAL));
        assertEquals("vegetarian", host1.getCriterion(Criteria.HOST_FOOD));
        assertEquals("reading,music", host1.getCriterion(Criteria.HOBBIES));
        assertNull(host1.getCriterion(Criteria.HISTORY)); // Chaîne vide dans le CSV pour HISTORY correspond à null

        Adolescent host2 = hosts.get(1);
        assertEquals("HostTwo", host2.getLastName());
        assertEquals("yes", host2.getCriterion(Criteria.HOST_HAS_ANIMAL));
        assertEquals("nonuts", host2.getCriterion(Criteria.HOST_FOOD));
        assertEquals("sports", host2.getCriterion(Criteria.HOBBIES));
        assertEquals("same", host2.getCriterion(Criteria.HISTORY));
    }

    @Test
    void testImportAdolescentsGuests() {
        List<Adolescent> guests = csvService.importAdolescents(sampleGuestsCsv.toString(), false);
        assertEquals(2, guests.size());

        Adolescent guest1 = guests.get(0);
        assertEquals("VisitorOne", guest1.getLastName());
        assertEquals("Leo", guest1.getFirstName());
        assertEquals("Germany", guest1.getCountryOfOrigin());
        assertEquals(LocalDate.of(2008, 3, 22), guest1.getDateOfBirth());
        assertEquals("male", guest1.getCriterion(Criteria.GENDER));
        assertEquals("no", guest1.getCriterion(Criteria.GUEST_ANIMAL_ALLERGY));
        assertEquals("vegetarian", guest1.getCriterion(Criteria.GUEST_FOOD));
        assertEquals("music,games", guest1.getCriterion(Criteria.HOBBIES));
        assertEquals("female", guest1.getCriterion(Criteria.PAIR_GENDER));

        Adolescent guest2 = guests.get(1);
        assertNull(guest2.getCriterion(Criteria.GUEST_FOOD)); // Chaîne vide pour GUEST_FOOD devrait être null
        assertEquals("yes", guest2.getCriterion(Criteria.GUEST_ANIMAL_ALLERGY));
    }

    @Test
    void testImportAdolescentsMalformedFile() throws IOException {
        Path malformedCsv = tempDir.resolve("malformed.csv");
        List<String> lines = new ArrayList<>();
        lines.add("NAME;FORENAME;COUNTRY;BIRTH_DATE;GENDER"); // En-tête correct
        lines.add("OnlyName;;;"); // Pas assez de colonnes
        Files.write(malformedCsv, lines);

        List<Adolescent> adolescents = csvService.importAdolescents(malformedCsv.toString(), true);
        assertTrue(adolescents.isEmpty(), "Devrait retourner une liste vide pour les lignes mal formées ou gérer les erreurs en ignorant les lignes.");
    }

    @Test
    void testImportAdolescentsMissingHeaderFile() throws IOException {
        Path missingHeaderCsv = tempDir.resolve("missing_header.csv");
        List<String> lines = new ArrayList<>();
        // Pas de ligne d'en-tête
        lines.add("HostOne;Eva;France;2008-01-15;female;no;vegetarian;reading,music;");
        Files.write(missingHeaderCsv, lines);

        List<Adolescent> adolescents = csvService.importAdolescents(missingHeaderCsv.toString(), true);
        assertTrue(adolescents.isEmpty(), "Devrait retourner une liste vide si les en-têtes essentiels sont manquants.");
    }

    @Test
    void testImportAdolescentsEmptyFile() throws IOException {
        Path emptyCsv = tempDir.resolve("empty.csv");
        // Créer un fichier vide
        Files.createFile(emptyCsv);

        List<Adolescent> adolescents = csvService.importAdolescents(emptyCsv.toString(), true);
        assertTrue(adolescents.isEmpty(), "Devrait retourner une liste vide pour un fichier vide.");
    }

    @Test
    void testExportAffectations() throws IOException {
        Map<Adolescent, Adolescent> pairings = new HashMap<>();
        Adolescent visitor1 = new Adolescent("VisitorOne", "Leo", "male", "Germany", new HashMap<>(), LocalDate.of(2008, 3, 22), false);
        Adolescent host1 = new Adolescent("HostOne", "Eva", "female", "France", new HashMap<>(), LocalDate.of(2008, 1, 15), true);
        pairings.put(visitor1, host1);

        csvService.exportAffectations(pairings, outputCsv.toString());

        assertTrue(Files.exists(outputCsv));
        List<String> lines = Files.readAllLines(outputCsv);
        assertEquals(2, lines.size()); // En-tête + 1 ligne de données
        assertEquals("VISITOR_LASTNAME;VISITOR_FIRSTNAME;VISITOR_COUNTRY;HOST_LASTNAME;HOST_FIRSTNAME;HOST_COUNTRY", lines.get(0));
        assertEquals("VisitorOne;Leo;Germany;HostOne;Eva;France", lines.get(1));
    }

    @Test
    void testExportAffectationsEmptyMap() throws IOException {
        Map<Adolescent, Adolescent> pairings = new HashMap<>();
        csvService.exportAffectations(pairings, outputCsv.toString());

        assertTrue(Files.exists(outputCsv));
        List<String> lines = Files.readAllLines(outputCsv);
        assertEquals(1, lines.size()); // Seulement l'en-tête
        assertEquals("VISITOR_LASTNAME;VISITOR_FIRSTNAME;VISITOR_COUNTRY;HOST_LASTNAME;HOST_FIRSTNAME;HOST_COUNTRY", lines.get(0));
    }
}