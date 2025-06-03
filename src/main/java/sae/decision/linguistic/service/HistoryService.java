package sae.decision.linguistic.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import sae.decision.linguistic.model.Affectation;

/**
 * Service gérant l'historique des affectations.
 * Cette classe permet de sauvegarder et charger l'historique des affectations
 * entre les adolescents (hôtes et visiteurs).
 */
public class HistoryService {

    /**
     * Sauvegarde l'historique des affectations dans un fichier binaire.
     * L'historique est une map où la clé peut représenter "Année_PaysOrigine_PaysDestination".
     *
     * @param history  La map des affectations passées.
     * @param filePath Chemin vers le fichier binaire de sortie.
     */
    public void saveAffectationHistory(Map<String, Affectation> history, String filePath) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(filePath);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(history);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde de l'historique des affectations: " + e.getMessage());
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    System.err.println("Erreur lors de la fermeture de ObjectOutputStream: " + e.getMessage());
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    System.err.println("Erreur lors de la fermeture de FileOutputStream: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Charge l'historique des affectations depuis un fichier binaire.
     *
     * @param filePath Chemin vers le fichier binaire.
     * @return Une map des affectations passées, ou une map vide si le chargement échoue ou si le fichier n'existe pas.
     */
    @SuppressWarnings("unchecked") // Pour le cast de Object vers Map
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
                System.err.println("Erreur: L'objet chargé n'est pas de type Map<String, Affectation>.");
            }
        } catch (IOException e) {
            // Le fichier non trouvé est un cas courant pour la première exécution, donc ne pas afficher d'erreur sauf si verbeux
            System.err.println("Erreur lors du chargement de l'historique des affectations (le fichier n'existe peut-être pas encore): " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement de l'historique: Classe non trouvée - " + e.getMessage());
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    System.err.println("Erreur lors de la fermeture de ObjectInputStream: " + e.getMessage());
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    System.err.println("Erreur lors de la fermeture de FileInputStream: " + e.getMessage());
                }
            }
        }
        return history;
    }
}