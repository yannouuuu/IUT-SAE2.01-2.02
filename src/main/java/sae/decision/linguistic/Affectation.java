package sae.decision.linguistic;

import java.util.List;
import java.util.Map;
import java.io.Serializable;

/**
 * Classe représentant une affectation d'adolescents visiteurs à des adolescents hôtes.
 * Elle contient les listes d'hôtes et de visiteurs et fournit une méthode pour calculer l'appariement.
 */
public class Affectation implements Serializable {
    private List<Adolescent> hosts;
    private List<Adolescent> visitors;
    private Map<Adolescent, Adolescent> pairs; // Map des paires
    private static final long serialVersionUID = 1L;
    /**
     * Construit une nouvelle instance d'Affectation.
     * @param hosts La liste des adolescents hôtes.
     * @param visitors La liste des adolescents visiteurs.
     */
    public Affectation(List<Adolescent> hosts, List<Adolescent> visitors) {
        this.hosts = hosts;
        this.visitors = visitors;
    }

    /**
     * Calcule l'appariement entre hôtes et visiteurs.
     * L'implémentation réelle utilisera des algorithmes d'optimisation (semaines suivantes).
     * @return Une Map représentant les paires (Visiteur -> Hôte).
     */
    public Map<Adolescent, Adolescent> calculatePairing() {
        System.out.println("Calcul de l'appariement (logique simplifiée/placeholder)...");
        // Implémentation future basée sur les graphes et l'optimisation
        return Map.of(); // Retourne une map vide pour l'instant
    }

    // Getters
    /**
     * Récupère la liste des adolescents hôtes.
     * @return La liste des hôtes.
     */
    public List<Adolescent> getHosts() { return hosts; }
    /**
     * Récupère la liste des adolescents visiteurs.
     * @return La liste des visiteurs.
     */
    public List<Adolescent> getVisitors() { return visitors; }
}
