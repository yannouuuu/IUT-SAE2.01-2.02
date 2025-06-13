package sae.decision.linguistic.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sae.decision.linguistic.model.Adolescent;
import sae.decision.linguistic.model.Criteria;

/**
 * Service gérant l'importation et l'exportation des données au format CSV.
 * Cette classe permet de charger les données des adolescents (hôtes et visiteurs)
 * depuis des fichiers CSV et d'exporter les affectations.
 */
public class CSVService {
    // Constantes pour le format CSV
    private static final String CSV_DELIMITER = ";";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    
    // En-têtes obligatoires pour les fichiers CSV d'entrée
    private static final Set<String> REQUIRED_HEADERS = Set.of(
        "FORENAME", "NAME", "COUNTRY", "BIRTH_DATE", "GENDER"
    );
    
    // En-têtes pour le fichier CSV de sortie
    private static final String[] EXPORT_HEADERS = {
        "VISITOR_LASTNAME", "VISITOR_FIRSTNAME", "VISITOR_COUNTRY",
        "HOST_LASTNAME", "HOST_FIRSTNAME", "HOST_COUNTRY"
    };

    /**
     * Importe une liste d'adolescents depuis un fichier CSV.
     * La première ligne du CSV doit contenir les en-têtes obligatoires.
     *
     * @param filePath Chemin vers le fichier CSV
     * @param isHost   True si les adolescents sont des hôtes, false pour des visiteurs
     * @return Liste d'objets Adolescent
     */
    public List<Adolescent> importAdolescents(String filePath, boolean isHost) {
        List<Adolescent> adolescents = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            Map<String, Integer> headerMap = parseHeaders(br, filePath);
            if (headerMap.isEmpty()) {
                return adolescents;
            }

            String line;
            while ((line = br.readLine()) != null) {
                processDataLine(line, headerMap, isHost, adolescents);
            }
            
        } catch (IOException e) {
            logError("Erreur lors de la lecture du fichier CSV", filePath, e);
        }
        
        return adolescents;
    }

    /**
     * Analyse et valide les en-têtes du fichier CSV.
     * @param br Le lecteur de buffer pour lire le fichier.
     * @param filePath Le chemin du fichier pour les messages d'erreur.
     * @return Une carte des en-têtes et de leurs positions, ou une carte vide si invalide.
     * @throws IOException Si une erreur se produit.
     */
    private Map<String, Integer> parseHeaders(BufferedReader br, String filePath) throws IOException {
        String headerLine = br.readLine();
        if (headerLine == null) {
            logError("Le fichier CSV est vide ou l'en-tête est manquant", filePath, null);
            return new HashMap<>();
        }

        Map<String, Integer> headerMap = new HashMap<>();
        String[] headers = headerLine.split(CSV_DELIMITER);
        
        for (int i = 0; i < headers.length; i++) {
            headerMap.put(headers[i].trim().toUpperCase(), i);
        }

        if (!headerMap.keySet().containsAll(REQUIRED_HEADERS)) {
            logError("Le fichier CSV manque un ou plusieurs en-têtes obligatoires", filePath, null);
            return new HashMap<>();
        }

        return headerMap;
    }

    /**
     * Traite une seule ligne de données du fichier CSV et crée un objet Adolescent.
     * @param line La ligne de données à traiter.
     * @param headerMap La carte des en-têtes pour l'interprétation des données.
     * @param isHost Indique si l'adolescent est un hôte.
     * @param adolescents La liste à laquelle ajouter le nouvel adolescent.
     */
    private void processDataLine(String line, Map<String, Integer> headerMap, boolean isHost, List<Adolescent> adolescents) {
        String[] data = line.split(CSV_DELIMITER, -1);
        if (data.length != headerMap.size()) {
            logError("Ligne mal formée ignorée (nombre de colonnes incorrect)", line, null);
            return;
        }

        try {
            Map<Criteria, String> criteria = extractCriteria(data, headerMap, isHost);
            Adolescent ado = createAdolescent(data, headerMap, criteria, isHost);
            adolescents.add(ado);
        } catch (Exception e) {
            logError("Erreur lors du traitement de la ligne", line, e);
        }
    }

    /**
     * Extrait les critères d'une ligne de données en fonction des en-têtes.
     * @param data Le tableau de chaînes de caractères de la ligne de données.
     * @param headerMap La carte des en-têtes.
     * @param isHost Indique si l'adolescent est un hôte.
     * @return Une carte des critères extraits.
     */
    private Map<Criteria, String> extractCriteria(String[] data, Map<String, Integer> headerMap, boolean isHost) {
        Map<Criteria, String> criteria = new HashMap<>();
        
        for (Map.Entry<String, Integer> header : headerMap.entrySet()) {
            String headerName = header.getKey();
            int columnIndex = header.getValue();
            
            if (!REQUIRED_HEADERS.contains(headerName)) {
                try {
                    Criteria critere = Criteria.valueOf(headerName);
                    
                    // Ignorer les critères incompatibles avec le type d'adolescent
                    if ((isHost && headerName.startsWith("GUEST_")) || 
                        (!isHost && headerName.startsWith("HOST_"))) {
                        continue;
                    }
                    
                    String value = data[columnIndex].trim();
                    if (!value.isEmpty()) {
                        criteria.put(critere, value);
                    } else if (isNullableField(critere)) {
                        criteria.put(critere, null);
                    }
                } catch (IllegalArgumentException e) {
                    logError("En-tête non reconnu ignoré", headerName, null);
                }
            }
        }
        
        return criteria;
    }

    /**
     * Vérifie si un champ de critère peut être null.
     * @param critere Le critère à vérifier.
     * @return true si le critère peut être null, sinon false.
     */
    private boolean isNullableField(Criteria critere) {
        return critere == Criteria.PAIR_GENDER || 
               critere == Criteria.HISTORY || 
               critere == Criteria.GUEST_FOOD || 
               critere == Criteria.HOST_FOOD || 
               critere == Criteria.HOBBIES;
    }

    /**
     * Crée un objet Adolescent à partir des données CSV analysées.
     * @param data Le tableau de chaînes de caractères de la ligne de données.
     * @param headerMap La carte des en-têtes.
     * @param criteria La carte des critères pour l'adolescent.
     * @param isHost Indique si l'adolescent est un hôte.
     * @return Le nouvel objet Adolescent.
     */
    private Adolescent createAdolescent(String[] data, Map<String, Integer> headerMap, 
                                      Map<Criteria, String> criteria, boolean isHost) {
        String forename = data[headerMap.get("FORENAME")].trim();
        String name = data[headerMap.get("NAME")].trim();
        String country = data[headerMap.get("COUNTRY")].trim();
        LocalDate birthDate = LocalDate.parse(data[headerMap.get("BIRTH_DATE")].trim());
        String gender = data[headerMap.get("GENDER")].trim();

        return new Adolescent(name, forename, gender, country, criteria, birthDate, isHost);
    }

    /**
     * Exporte les appariements vers un fichier CSV.
     *
     * @param pairings Map des appariements (Visiteur -> Hôte)
     * @param filePath Chemin du fichier de sortie
     */
    public void exportAffectations(Map<Adolescent, Adolescent> pairings, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // En-tête
            bw.write(String.join(CSV_DELIMITER, EXPORT_HEADERS));
            bw.write(LINE_SEPARATOR);

            // Données
            if (pairings != null) {
                for (Map.Entry<Adolescent, Adolescent> entry : pairings.entrySet()) {
                    Adolescent visitor = entry.getKey();
                    Adolescent host = entry.getValue();
                    
                    String[] rowData = {
                        visitor.getLastName(), visitor.getFirstName(), visitor.getCountryOfOrigin(),
                        host.getLastName(), host.getFirstName(), host.getCountryOfOrigin()
                    };
                    
                    bw.write(String.join(CSV_DELIMITER, rowData));
                    bw.write(LINE_SEPARATOR);
                }
            }
        } catch (IOException e) {
            logError("Erreur lors de l'écriture du fichier CSV", filePath, e);
        }
    }

    /**
     * Enregistre un message d'erreur avec un contexte et une exception facultative.
     * @param message Le message d'erreur principal.
     * @param context Le contexte dans lequel l'erreur s'est produite (par exemple, nom de fichier, ligne).
     * @param e L'exception qui a été levée (peut être nulle).
     */
    private void logError(String message, String context, Exception e) {
        StringBuilder error = new StringBuilder(message);
        if (context != null && !context.isEmpty()) {
            error.append(" : ").append(context);
        }
        if (e != null) {
            error.append(" - ").append(e.getMessage());
        }
        System.err.println(error);
    }
}