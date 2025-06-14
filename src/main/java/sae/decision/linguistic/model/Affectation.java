package sae.decision.linguistic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe représentant une affectation d'adolescents visiteurs à des adolescents hôtes.
 * Elle contient les listes d'hôtes et de visiteurs et fournit une méthode pour calculer l'appariement
 * en utilisant l'algorithme hongrois pour optimiser les affinités.
 */
public class Affectation implements Serializable {
    private List<Adolescent> hosts;
    private List<Adolescent> visitors;
    private final Map<Adolescent, Adolescent> pairs; // Map des paires (Visiteur -> Hôte)
    private static final long serialVersionUID = 1L;
    /**
     * Construit une nouvelle instance d'Affectation.
     * @param hosts La liste des adolescents hôtes.
     * @param visitors La liste des adolescents visiteurs.
     */
    public Affectation(List<Adolescent> hosts, List<Adolescent> visitors) {
        this.hosts = hosts;
        this.visitors = visitors;
        this.pairs = new HashMap<>();
    }

    /**
     * Fusionne une autre affectation dans celle-ci.
     * Les paires, les listes d'hôtes et de visiteurs sont ajoutées en évitant les doublons.
     * @param other L'affectation à fusionner.
     */
    public void merge(Affectation other) {
        if (other == null) {
            return;
        }

        // Fusionner les paires
        if (other.getPairs() != null) {
            this.getPairs().putAll(other.getPairs());
        }

        // Fusionner les hôtes (en évitant les doublons)
        if (other.getHosts() != null) {
            Set<Adolescent> hostSet = new HashSet<>(this.hosts);
            hostSet.addAll(other.getHosts());
            this.hosts = new ArrayList<>(hostSet);
        }

        // Fusionner les visiteurs (en évitant les doublons)
        if (other.getVisitors() != null) {
            Set<Adolescent> visitorSet = new HashSet<>(this.visitors);
            visitorSet.addAll(other.getVisitors());
            this.visitors = new ArrayList<>(visitorSet);
        }
    }

    /**
     * Calcule l'appariement optimal entre hôtes et visiteurs en utilisant l'algorithme hongrois.
     * @return Une Map représentant les paires (Visiteur -> Hôte).
     */
    public Map<Adolescent, Adolescent> calculatePairing() {
        return calculatePairing(null);
    }

    /**
     * Calcule l'appariement optimal entre hôtes et visiteurs en utilisant l'algorithme hongrois, en tenant compte de l'historique.
     * @param history L'historique des affectations passées pour éviter les ré-appariements.
     * @return Une Map représentant les paires (Visiteur -> Hôte).
     */
    public Map<Adolescent, Adolescent> calculatePairing(Map<String, Affectation> history) {
        int n = Math.max(hosts.size(), visitors.size());
        int[][] costMatrix = new int[n][n];
        
        // Construire la matrice de coût (on utilise le négatif des affinités car l'algorithme minimise)
        for (int i = 0; i < visitors.size(); i++) {
            for (int j = 0; j < hosts.size(); j++) {
                costMatrix[i][j] = -visitors.get(i).calculateAffinity(hosts.get(j));
            }
        }

        // Compléter la matrice avec des valeurs très élevées si nécessaire
        for (int i = visitors.size(); i < n; i++) {
            for (int j = 0; j < n; j++) {
                costMatrix[i][j] = Integer.MAX_VALUE / 2;
            }
        }
        for (int i = 0; i < visitors.size(); i++) {
            for (int j = hosts.size(); j < n; j++) {
                costMatrix[i][j] = Integer.MAX_VALUE / 2;
            }
        }

        // Exécuter l'algorithme hongrois
        int[] assignment = hungarianAlgorithm(costMatrix);

        // Créer la map des paires
        this.pairs.clear();
        for (int i = 0; i < visitors.size(); i++) {
            if (assignment[i] < hosts.size()) {
                pairs.put(visitors.get(i), hosts.get(assignment[i]));
            }
        }

        return pairs;
    }

    /**
     * Implémentation de l'algorithme hongrois (Munkres).
     * @param costMatrix la matrice de coût
     * @return un tableau d'indices représentant l'affectation optimale
     */
    private int[] hungarianAlgorithm(int[][] costMatrix) {
        int n = costMatrix.length;
        int[] lx = new int[n];
        int[] ly = new int[n];
        int[] xy = new int[n];
        int[] yx = new int[n];
        
        // Initialisation
        for (int i = 0; i < n; i++) {
            xy[i] = -1;
            yx[i] = -1;
            ly[i] = 0;
        }

        // Initialiser lx avec le minimum de chaque ligne
        for (int i = 0; i < n; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (costMatrix[i][j] < min) {
                    min = costMatrix[i][j];
                }
            }
            lx[i] = min;
        }

        // Trouver une affectation initiale
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (xy[i] == -1 && yx[j] == -1 && costMatrix[i][j] - lx[i] - ly[j] == 0) {
                    xy[i] = j;
                    yx[j] = i;
                }
            }
        }

        int[] queue = new int[n];
        int[] prev = new int[n];
        boolean[] S = new boolean[n];
        boolean[] T = new boolean[n];

        // Améliorer l'affectation jusqu'à ce qu'elle soit optimale
        for (int i = 0; i < n; i++) {
            if (xy[i] == -1) {
                // Initialiser les ensembles S et T
                for (int j = 0; j < n; j++) {
                    S[j] = false;
                    T[j] = false;
                    prev[j] = -1;
                }
                
                // Ajouter i à S
                S[i] = true;
                
                // Initialiser la file d'attente
                int qh = 0, qt = 0;
                queue[qt++] = i;
                
                // Chercher un chemin augmentant
                int j = -1;
                while (j == -1 && qh < qt) {
                    int k = queue[qh++];
                    for (int l = 0; l < n; l++) {
                        if (!T[l] && costMatrix[k][l] - lx[k] - ly[l] == 0) {
                            if (yx[l] == -1) {
                                j = l;
                                prev[j] = k;
                                break;
                            }
                            T[l] = true;
                            queue[qt++] = yx[l];
                            prev[l] = k;
                        }
                    }
                }
                
                if (j != -1) {  // Chemin augmentant trouvé
                    int k = j;
                    while (k != -1) {
                        int p = prev[k];
                        int temp = xy[p];
                        yx[k] = p;
                        xy[p] = k;
                        k = temp;
                    }
                } else {  // Pas de chemin augmentant trouvé
                    int delta = Integer.MAX_VALUE;
                    for (int k = 0; k < n; k++) {
                        if (S[k]) {
                            for (int l = 0; l < n; l++) {
                                if (!T[l] && costMatrix[k][l] != Integer.MAX_VALUE / 2) {
                                    delta = Math.min(delta, costMatrix[k][l] - lx[k] - ly[l]);
                                }
                            }
                        }
                    }
                    
                    // Vérifier si delta est toujours à sa valeur initiale (aucun progrès possible)
                    if (delta == Integer.MAX_VALUE) {
                        // Si on ne peut pas améliorer, sortir de la boucle
                        break;
                    }
                    
                    // Mettre à jour les étiquettes
                    for (int k = 0; k < n; k++) {
                        if (S[k]) lx[k] += delta;
                        if (T[k]) ly[k] -= delta;
                    }
                    
                    i--;  // Réessayer avec le même i
                }
            }
        }

        return xy;
    }

    /**
     * Récupère les paires d'adolescents affectés.
     * @return La map des paires (Visiteur -> Hôte).
     */
    public Map<Adolescent, Adolescent> getPairs() {
        return pairs;
    }

    /**
     * Récupère la liste des adolescents hôtes.
     * @return La liste des hôtes.
     */
    public List<Adolescent> getHosts() { 
        return hosts; 
    }

    /**
     * Récupère la liste des adolescents visiteurs.
     * @return La liste des visiteurs.
     */
    public List<Adolescent> getVisitors() { 
        return visitors; 
    }

    /**
     * Récupère la liste des hôtes non assignés.
     * @return Une liste d'hôtes sans visiteur assigné.
     */
    public List<Adolescent> getUnassignedHosts() {
        Set<Adolescent> assignedHosts = new HashSet<>(pairs.values());
        List<Adolescent> unassigned = new ArrayList<>();
        for (Adolescent host : hosts) {
            if (!assignedHosts.contains(host)) {
                unassigned.add(host);
            }
        }
        return unassigned;
    }

    /**
     * Récupère la liste des visiteurs non assignés.
     * @return Une liste de visiteurs sans hôte assigné.
     */
    public List<Adolescent> getUnassignedVisitors() {
        Set<Adolescent> assignedVisitors = new HashSet<>(pairs.keySet());
        List<Adolescent> unassigned = new ArrayList<>();
        for (Adolescent visitor : visitors) {
            if (!assignedVisitors.contains(visitor)) {
                unassigned.add(visitor);
            }
        }
        return unassigned;
    }
}