package sae.decision.linguistic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class PairingManualController {

    @FXML
    private Button retourButton;

    @FXML
    private ComboBox<?> hoteComboBox;

    @FXML
    private ComboBox<?> visiteurComboBox;

    @FXML
    private Button createButton;

    @FXML
    public void initialize() {
        System.out.println("PairingManualController initialisé.");
        // Logique pour charger les listes d'hôtes et de visiteurs dans les ComboBox
    }
} 