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

public class CSVService {
    private static final String CSV_DELIMITER = ";";

    /**
     * Imports a list of adolescents from a CSV file.
     * The first line of the CSV must be a header defining the columns.
     * Expected headers for basic info: FORENAME, NAME, COUNTRY, BIRTH_DATE, GENDER.
     * Other headers should match Criteres enum names.
     *
     * @param filePath Path to the CSV file.
     * @param isHost   True if the adolescents in this file are hosts, false if they are guests.
     * @return A list of Adolescent objects.
     */
    public List<Adolescent> importAdolescents(String filePath, boolean isHost) {
        List<Adolescent> adolescents = new ArrayList<>();
        String line = "";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath));
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("Error: CSV file is empty or header is missing. Path: " + filePath);
                return adolescents; // Return empty list
            }

            String[] headers = headerLine.split(CSV_DELIMITER);
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].trim().toUpperCase(), i);
            }

            // Check for essential headers
            if (!headerMap.containsKey("FORENAME") || !headerMap.containsKey("NAME") ||
                    !headerMap.containsKey("COUNTRY") || !headerMap.containsKey("BIRTH_DATE") ||
                    !headerMap.containsKey("GENDER")) {
                System.err.println("Error: CSV file is missing one or more essential headers (FORENAME, NAME, COUNTRY, BIRTH_DATE, GENDER). Path: " + filePath);
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        System.err.println("Error closing BufferedReader: " + e.getMessage());
                    }
                }
                return adolescents; // Return empty list
            }

            line = br.readLine(); // Read the first data line
            while (line != null) {
                String[] data = line.split(CSV_DELIMITER, -1); // Include trailing empty strings
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
                            // GENDER is handled by constructor, other fixed fields are not criteria
                            if (!header.equals("FORENAME") && !header.equals("NAME") &&
                                    !header.equals("COUNTRY") && !header.equals("BIRTH_DATE") &&
                                    !header.equals("GENDER")) {
                                try {
                                    Criteria critere = Criteria.valueOf(header); // Assumes header matches enum name
                                    String value = data[i].trim();
                                    if (!value.isEmpty()) { // Only add if value is not empty
                                        criteria.put(critere, value);
                                    } else if (critere == Criteria.PAIR_GENDER || critere == Criteria.HISTORY || critere == Criteria.GUEST_FOOD || critere == Criteria.HOST_FOOD || critere == Criteria.HOBBIES) {
                                        // These can be legitimately empty/null as per Criteres.isValid logic
                                        criteria.put(critere, null);
                                    }
                                } catch (IllegalArgumentException e) {
                                    // Header does not match any Criteres enum name, ignore it or log warning
                                    // System.err.println("Warning: Header '" + header + "' does not match any known criterion and will be ignored.");
                                }
                            }
                        }

                        // The Adolescent constructor will internally add GENDER to its criteria map
                        Adolescent ado = new Adolescent(name, forename, gender, country, criteria, birthDate, isHost);
                        adolescents.add(ado);

                    } catch (DateTimeParseException e) {
                        System.err.println("Error parsing date in line: " + line + " - " + e.getMessage());
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.err.println("Error: Malformed line (not enough columns): " + line + " - " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error creating adolescent from line: " + line + " - " + e.getMessage());
                    }
                } else {
                    System.err.println("Warning: Skipping malformed line (column count mismatch): " + line);
                }
                line = br.readLine(); // Read next line
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + filePath + " - " + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.err.println("Error closing BufferedReader: " + e.getMessage());
                }
            }
        }
        return adolescents;
    }

    /**
     * Exports the pairings from an Affectation object to a CSV file.
     *
     * @param pairings   The map of pairings (Visitor -> Host).
     * @param filePath   Path to the output CSV file.
     */
    public void exportAffectations(Map<Adolescent, Adolescent> pairings, String filePath) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filePath));
            // Write header
            bw.write("VISITOR_LASTNAME" + CSV_DELIMITER + "VISITOR_FIRSTNAME" + CSV_DELIMITER + "VISITOR_COUNTRY" +
                    CSV_DELIMITER + "HOST_LASTNAME" + CSV_DELIMITER + "HOST_FIRSTNAME" + CSV_DELIMITER + "HOST_COUNTRY");
            bw.newLine();

            // Write data
            if (pairings != null) { // Check if pairings is null
                // Need to iterate using an explicit iterator or convert entrySet to List for indexed loop
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
            System.err.println("Error writing CSV file: " + filePath + " - " + e.getMessage());
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    System.err.println("Error closing BufferedWriter: " + e.getMessage());
                }
            }
        }
    }
}