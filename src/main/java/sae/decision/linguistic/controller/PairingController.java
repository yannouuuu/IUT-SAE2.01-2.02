package sae.decision.linguistic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PairingController {

    @FXML
    private Label totalAppariementsLabel;

    @FXML
    private Label validesLabel;

    @FXML
    private Label enAttenteLabel;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<?> statusFilter;

    @FXML
    private TableView<?> appariementsTable;

    @FXML
    private TableColumn<?, ?> hoteColumn;

    @FXML
    private TableColumn<?, ?> visiteurColumn;

    @FXML
    private TableColumn<?, ?> scoreColumn;

    @FXML
    private TableColumn<?, ?> statutColumn;

    @FXML
    private TableColumn<?, ?> dateCreationColumn;

    @FXML
    private TableColumn<?, ?> actionsColumn;

    @FXML
    public void initialize() {
        System.out.println("PairingController a été initialisé et est prêt.");
        // Vous pouvez ajouter ici la logique pour charger les données dans le tableau
    }
} 