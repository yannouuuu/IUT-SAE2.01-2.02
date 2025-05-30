package sae.decision.linguistic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service gérant l'importation et l'exportation des données au format CSV.
 * Cette classe permet de charger les données des adolescents (hôtes et visiteurs)
 * depuis des fichiers CSV et d'exporter les affectations.
 */
public class CSVService {
    private static final String CSV_DELIMITER = ";";

    /**
     * Importe une liste d'adolescents depuis un fichier CSV.
     * La première ligne du CSV doit être un en-tête définissant les colonnes.
     * En-têtes attendus pour les informations de base : FORENAME, NAME, COUNTRY, BIRTH_DATE, GENDER.
     * Les autres en-têtes doivent correspondre aux noms de l'énumération Criteres.
     *
     * @param filePath Chemin vers le fichier CSV.
     * @param isHost   True si les adolescents dans ce fichier sont des hôtes, false s'ils sont des visiteurs.
     * @return Une liste d'objets Adolescent.
     */
    public List<Adolescent> importAdolescents(String filePath, boolean isHost) {
        List<Adolescent> adolescents = new ArrayList<>();
        String line = "";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath));
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("Erreur : Le fichier CSV est vide ou l'en-tête est manquant. Chemin : " + filePath);
                return adolescents; // Retourne une liste vide
            }

            String[] headers = headerLine.split(CSV_DELIMITER);
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].trim().toUpperCase(), i);
            }

            // Vérifier les en-têtes essentiels
            if (!headerMap.containsKey("FORENAME") || !headerMap.containsKey("NAME") ||
                    !headerMap.containsKey("COUNTRY") || !headerMap.containsKey("BIRTH_DATE") ||
                    !headerMap.containsKey("GENDER")) {
                System.err.println("Erreur : Le fichier CSV manque un ou plusieurs en-têtes essentiels (FORENAME, NAME, COUNTRY, BIRTH_DATE, GENDER). Chemin : " + filePath);
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        System.err.println("Erreur lors de la fermeture de BufferedReader : " + e.getMessage());
                    }
                }
                return adolescents; // Retourne une liste vide
            }

            line = br.readLine(); // Lire la première ligne de données
            while (line != null) {
                String[] data = line.split(CSV_DELIMITER, -1); // Inclure les chaînes vides finales
                if (data.length == headers.length) {
                    try {
                        String forename = data[headerMap.get("FORENAME")].trim();
                        String name = data[headerMap.get("NAME")].trim();
                        String country = data[headerMap.get("COUNTRY")].trim();
                        LocalDate birthDate = LocalDate.parse(data[headerMap.get("BIRTH_DATE")].trim());
                        String gender = data[headerMap.get("GENDER")].trim();

                        Map<Criteria, String> criteria = new HashMap<>();
                        for (int i = 0; i < headers.length; i++) {
                            String header = headers[i].trim().toUpperCase();
                            // GENDER est géré par le constructeur, les autres champs fixes ne sont pas des critères
                            if (!header.equals("FORENAME") && !header.equals("NAME") &&
                                    !header.equals("COUNTRY") && !header.equals("BIRTH_DATE") &&
                                    !header.equals("GENDER")) {
                                try {
                                    Criteria critere = Criteria.valueOf(header); // Suppose que l'en-tête correspond au nom de l'énumération
                                    String value = data[i].trim();
                                    if (!value.isEmpty()) { // N'ajouter que si la valeur n'est pas vide
                                        criteria.put(critere, value);
                                    } else if (critere == Criteria.PAIR_GENDER || critere == Criteria.HISTORY || critere == Criteria.GUEST_FOOD || critere == Criteria.HOST_FOOD || critere == Criteria.HOBBIES) {
                                        // Ceux-ci peuvent légitimement être vides/nuls selon la logique de Criteres.isValid
                                        criteria.put(critere, null);
                                    }
                                } catch (IllegalArgumentException e) {
                                    // L'en-tête ne correspond à aucun nom d'énumération Criteres, l'ignorer ou journaliser un avertissement
                                    // System.err.println("Avertissement : L'en-tête '" + header + "' ne correspond à aucun critère connu et sera ignoré.");
                                }
                            }
                        }

                        // Le constructeur Adolescent ajoutera en interne GENDER à sa map de critères
                        Adolescent ado = new Adolescent(name, forename, gender, country, criteria, birthDate, isHost);
                        adolescents.add(ado);

                    } catch (DateTimeParseException e) {
                        System.err.println("Erreur lors de l'analyse de la date dans la ligne : " + line + " - " + e.getMessage());
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.err.println("Erreur : Ligne mal formée (pas assez de colonnes) : " + line + " - " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Erreur lors de la création de l'adolescent à partir de la ligne : " + line + " - " + e.getMessage());
                    }
                } else {
                    System.err.println("Avertissement : Ligne mal formée ignorée (nombre de colonnes incorrect) : " + line);
                }
                line = br.readLine(); // Lire la ligne suivante
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier CSV : " + filePath + " - " + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.err.println("Erreur lors de la fermeture de BufferedReader : " + e.getMessage());
                }
            }
        }
        return adolescents;
    }

    /**
     * Exporte les appariements d'un objet Affectation vers un fichier CSV.
     *
     * @param pairings   La map des appariements (Visiteur -> Hôte).
     * @param filePath   Chemin vers le fichier CSV de sortie.
     */
    public void exportAffectations(Map<Adolescent, Adolescent> pairings, String filePath) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filePath));
            // Écrire l'en-tête
            bw.write("VISITOR_LASTNAME" + CSV_DELIMITER + "VISITOR_FIRSTNAME" + CSV_DELIMITER + "VISITOR_COUNTRY" +
                    CSV_DELIMITER + "HOST_LASTNAME" + CSV_DELIMITER + "HOST_FIRSTNAME" + CSV_DELIMITER + "HOST_COUNTRY");
            bw.newLine();

            // Écrire les données
            if (pairings != null) { // Vérifier si pairings est null
                // Besoin d'itérer en utilisant un itérateur explicite ou convertir entrySet en List pour la boucle indexée
                List<Map.Entry<Adolescent, Adolescent>> entryList = new ArrayList<>(pairings.entrySet());
                for (int i = 0; i < entryList.size(); i++) {
                    Map.Entry<Adolescent, Adolescent> entry = entryList.get(i);
                    Adolescent visitor = entry.getKey();
                    Adolescent host = entry.getValue();

                    bw.write(visitor.getLastName() + CSV_DELIMITER + visitor.getFirstName() + CSV_DELIMITER + visitor.getCountryOfOrigin() +
                            CSV_DELIMITER + host.getLastName() + CSV_DELIMITER + host.getFirstName() + CSV_DELIMITER + host.getCountryOfOrigin());
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier CSV : " + filePath + " - " + e.getMessage());
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    System.err.println("Erreur lors de la fermeture de BufferedWriter : " + e.getMessage());
                }
            }
        }
    }
}