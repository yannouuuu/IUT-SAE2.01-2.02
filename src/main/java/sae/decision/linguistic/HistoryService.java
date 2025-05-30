package sae.decision.linguistic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class HistoryService {

    /**
     * Saves the affectation history to a binary file.
     * The history is a map where the key might represent "Year_OriginCountry_DestinationCountry".
     *
     * @param history  The map of past affectations.
     * @param filePath Path to the output binary file.
     */
    public void saveAffectationHistory(Map<String, Affectation> history, String filePath) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(filePath);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(history);
        } catch (IOException e) {
            System.err.println("Error saving affectation history: " + e.getMessage());
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    System.err.println("Error closing ObjectOutputStream: " + e.getMessage());
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    System.err.println("Error closing FileOutputStream: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Loads the affectation history from a binary file.
     *
     * @param filePath Path to the binary file.
     * @return A map of past affectations, or an empty map if loading fails or file doesn't exist.
     */
    @SuppressWarnings("unchecked") // For the cast from Object to Map
    public Map<String, Affectation> loadAffectationHistory(String filePath) {
        Map<String, Affectation> history = new HashMap<>();
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(filePath);
            ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                history = (Map<String, Affectation>) obj;
            } else {
                System.err.println("Error: Loaded object is not of type Map<String, Affectation>.");
            }
        } catch (IOException e) {
            // File not found is a common case for the first run, so don't print error for it unless verbose
            // System.err.println("Error loading affectation history (file might not exist yet): " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading affectation history (class not found): " + e.getMessage());
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    System.err.println("Error closing ObjectInputStream: " + e.getMessage());
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    System.err.println("Error closing FileInputStream: " + e.getMessage());
                }
            }
        }
        return history;
    }
}