package sae.decision.linguistic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainViewController {

    @FXML
    private ComboBox<String> comboPaysA;

    @FXML
    private ComboBox<String> comboPaysB;

    @FXML
    private TableView<?> tableAppariements;

    @FXML
    private TableColumn<?, ?> colStatut;

    @FXML
    private TableColumn<?, ?> colHote;

    @FXML
    private TableColumn<?, ?> colVisiteur;

    @FXML
    private TableColumn<?, ?> colScore;
	
}
