package sae.decision.linguistic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Classe pour gérer les données des adolescents (hôtes et visiteurs)
 * de manière centralisée dans l'application.
 */
public class DataManager {

    private static final List<Adolescent> allAdolescents = new ArrayList<>();
    private static final ObjectProperty<Affectation> lastAffectation = new SimpleObjectProperty<>(null);

    /**
     * Ajoute une liste d'adolescents à la liste globale.
     * @param adolescents La liste d'adolescents à ajouter.
     */
    public static void addAdolescents(List<Adolescent> adolescents) {
        // Pour éviter les doublons, on pourrait ajouter une vérification ici
        allAdolescents.addAll(adolescents);
    }

    /**
     * Sauvegarde la dernière affectation calculée.
     * @param affectation L'affectation à stocker.
     */
    public static void setLastAffectation(Affectation affectation) {
        lastAffectation.set(affectation);
    }

    /**
     * Récupère la dernière affectation calculée.
     * @return La dernière affectation, ou null si aucune n'a été calculée.
     */
    public static Affectation getLastAffectation() {
        return lastAffectation.get();
    }

    /**
     * Récupère la propriété JavaFX de la dernière affectation.
     * @return La propriété de l'affectation.
     */
    public static ObjectProperty<Affectation> lastAffectationProperty() {
        return lastAffectation;
    }

    /**
     * Récupère tous les adolescents de type Hôte.
     * @return Une liste d'adolescents hôtes.
     */
    public static List<Adolescent> getHosts() {
        return allAdolescents.stream()
                .filter(Adolescent::isHost)
                .collect(Collectors.toList());
    }

    /**
     * Récupère tous les adolescents de type Visiteur.
     * @return Une liste d'adolescents visiteurs.
     */
    public static List<Adolescent> getVisitors() {
        return allAdolescents.stream()
                .filter(ado -> !ado.isHost())
                .collect(Collectors.toList());
    }

    /**
     * Récupère tous les adolescents.
     * @return La liste complète des adolescents.
     */
    public static List<Adolescent> getAllAdolescents() {
        return allAdolescents;
    }

    /**
     * Efface toutes les données.
     */
    public static void clearData() {
        allAdolescents.clear();
        lastAffectation.set(null);
    }
} 