package sae.decision.linguistic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;

public class SettingsController {

    @FXML
    private Label ageLabel;

    @FXML
    private Slider ageSlider;

    @FXML
    private Label sexeLabel;

    @FXML
    private Slider sexeSlider;

    @FXML
    private Label interetLabel;

    @FXML
    private Slider interetSlider;

    @FXML
    private Label langueLabel;

    @FXML
    private Slider langueSlider;

    @FXML
    private Label activitesLabel;

    @FXML
    private Slider activitesSlider;

    @FXML
    private Label totalLabel;

    @FXML
    private ToggleButton autoSaveSwitch;

    @FXML
    private ToggleButton notificationsSwitch;

    @FXML
    private Button saveButton;

    @FXML
    private Button resetButton;

    @FXML
    public void initialize() {
        System.out.println("SettingsController initialisé.");
        // Logique pour mettre à jour les labels des sliders, etc.
    }
} 