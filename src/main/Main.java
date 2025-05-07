package main;

/**
 * Classe principale
 * Gère le chargement des données, le lancement de l'affectation et l'affichage des résultats.
 */

public class Main {

    /**
     * Charge les données initiales nécessaires pour le programme.
     * Dans les versions futures, cela pourrait impliquer la lecture de CSV
     */
    public void loadInitialData() {
        // Logique pour charger les données (depuis CSV plus tard)
        System.out.println("Chargement des données initiales...");
    }

    /**
     * Lance le processus d'affectation des adolescents visiteurs aux adolescents hôtes.
     */
    public void launchAssignment() {
        System.out.println("Lancement du calcul de l'affectation...");
    }

    /**
     * Affiche les résultats de l'affectation.
     * @param assignment L'objet Affectation contenant les paires réalisées.
     */
    public void displayResults(Affectation assignment) {
        System.out.println("Affichage des résultats...");
    }

    // public static void main(String[] args) {    }
}
