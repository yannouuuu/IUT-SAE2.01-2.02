package main;

import java.util.List;
import java.util.Map;

public class Affectation {
    private List<Adolescent> hotes;
    private List<Adolescent> visiteurs;

    public Affectation(List<Adolescent> hotes, List<Adolescent> visiteurs) {
        this.hotes = hotes;
        this.visiteurs = visiteurs;
    }

    /**
     * Calcule l'appariement entre hôtes et visiteurs.
     * L'implémentation réelle utilisera des algorithmes d'optimisation (semaines suivantes).
     * @return Une Map représentant les paires (Visiteur -> Hôte).
     */
    public Map<Adolescent, Adolescent> calculerAppariement() {
        System.out.println("Calcul de l'appariement (logique simplifiée/placeholder)...");
        // Implémentation future basée sur les graphes et l'optimisation
        return Map.of(); // Retourne une map vide pour l'instant
    }

     // Getters
     public List<Adolescent> getHotes() { return hotes; }
     public List<Adolescent> getVisiteurs() { return visiteurs; }
}