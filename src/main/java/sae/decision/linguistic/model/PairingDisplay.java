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

    public PairingDisplay(Adolescent host, Adolescent visitor, int affinityScore) {
        this.host = host;
        this.visitor = visitor;
        this.hostName = new SimpleStringProperty(host.getFirstName() + " " + host.getLastName());
        this.visitorName = new SimpleStringProperty(visitor.getFirstName() + " " + visitor.getLastName());
        this.affinityScore = new SimpleIntegerProperty(affinityScore);
    }

    // Getters pour les propriétés JavaFX
    public String getHostName() { return hostName.get(); }
    public SimpleStringProperty hostNameProperty() { return hostName; }

    public String getVisitorName() { return visitorName.get(); }
    public SimpleStringProperty visitorNameProperty() { return visitorName; }

    public int getAffinityScore() { return affinityScore.get(); }
    public SimpleIntegerProperty affinityScoreProperty() { return affinityScore; }

    // Getters pour les objets originaux
    public Adolescent getHost() { return host; }
    public Adolescent getVisitor() { return visitor; }
} 