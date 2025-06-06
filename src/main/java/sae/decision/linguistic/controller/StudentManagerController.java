package sae.decision.linguistic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class StudentManagerController {

    @FXML
    private Button importButton;

    @FXML
    private Button showFormButton;

    @FXML
    private Label hotesLabel;

    @FXML
    private ToggleButton viewSwitch;

    @FXML
    private Label visiteursLabel;

    @FXML
    private VBox formPane;

    @FXML
    private Label formTitleLabel;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField nomField;

    @FXML
    private DatePicker dateNaissancePicker;

    @FXML
    private ComboBox<?> genreComboBox;

    @FXML
    private TextField paysField;

    @FXML
    private TextField hobbiesField;

    @FXML
    private CheckBox sansNoixCheckBox;

    @FXML
    private CheckBox vegetarienCheckBox;

    @FXML
    private ToggleButton hoteTypeToggle;

    @FXML
    private ToggleGroup typeToggleGroup;

    @FXML
    private ToggleButton visiteurTypeToggle;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label listTitleLabel;

    @FXML
    private ListView<?> elevesListView;

    @FXML
    public void initialize() {
        System.out.println("StudentManagerController initialisé.");
        // Logique pour afficher/cacher le formulaire, gérer le switch, etc.
    }
} 