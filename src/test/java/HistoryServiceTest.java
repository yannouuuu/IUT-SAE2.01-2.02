import sae.decision.linguistic.model.Affectation;
import sae.decision.linguistic.service.HistoryService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryServiceTest {

    private HistoryService historyService;
    @BeforeEach
    void setUp() {
        historyService = new HistoryService();
    }

    @Test
    void testLoadNonExistentHistoryFile() {
        Map<String, Affectation> loadedHistory = historyService.loadAffectationHistory("non_existent_file.dat");
        assertNotNull(loadedHistory);
        assertTrue(loadedHistory.isEmpty(), "Le chargement depuis un fichier inexistant devrait retourner une map vide.");
    }
}