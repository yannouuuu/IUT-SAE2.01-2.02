package sae.decision.linguistic.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Classe de présentation pour afficher une paire d'adolescents dans une TableView JavaFX.
 */
public class PairingDisplay {

    private final SimpleStringProperty hostName;
    private final SimpleStringProperty visitorName;
    private final SimpleIntegerProperty affinityScore;
    private final Adolescent host;
    private final Adolescent visitor;

    /**
     * Construit une nouvelle instance de PairingDisplay.
     * @param host L'adolescent hôte.
     * @param visitor L'adolescent visiteur.
     * @param affinityScore Le score d'affinité entre l'hôte et le visiteur.
     */
    public PairingDisplay(Adolescent host, Adolescent visitor, int affinityScore) {
        this.host = host;
        this.visitor = visitor;
        this.hostName = new SimpleStringProperty(host.getFirstName() + " " + host.getLastName());
        this.visitorName = new SimpleStringProperty(visitor.getFirstName() + " " + visitor.getLastName());
        this.affinityScore = new SimpleIntegerProperty(affinityScore);
    }

    /**
     * Récupère le nom complet de l'hôte.
     * @return Le nom de l'hôte.
     */
    public String getHostName() { 
        return hostName.get(); 
    }

    /**
     * Récupère la propriété JavaFX du nom de l'hôte.
     * @return La propriété du nom de l'hôte.
     */
    public SimpleStringProperty hostNameProperty() { 
        return hostName; 
    }

    /**
     * Récupère le nom complet du visiteur.
     * @return Le nom du visiteur.
     */
    public String getVisitorName() { 
        return visitorName.get(); 
    }

    /**
     * Récupère la propriété JavaFX du nom du visiteur.
     * @return La propriété du nom du visiteur.
     */
    public SimpleStringProperty visitorNameProperty() { 
        return visitorName; 
    }

    /**
     * Récupère le score d'affinité.
     * @return Le score d'affinité.
     */
    public int getAffinityScore() { 
        return affinityScore.get(); 
    }

    /**
     * Récupère la propriété JavaFX du score d'affinité.
     * @return La propriété du score d'affinité.
     */
    public SimpleIntegerProperty affinityScoreProperty() { 
        return affinityScore; 
    }

    /**
     * Récupère l'objet Adolescent de l'hôte.
     * @return L'hôte.
     */
    public Adolescent getHost() { 
        return host; 
    }

    /**
     * Récupère l'objet Adolescent du visiteur.
     * @return Le visiteur.
     */
    public Adolescent getVisitor() { 
        return visitor; 
    }
} 