package sae.decision.linguistic.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.cell.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import sae.decision.linguistic.controller.MainController;
import javafx.beans.property.SimpleStringProperty;

public class AppFX extends Application {

    public static class PairingDisplayEntry {
        private final SimpleStringProperty status;
        private final SimpleStringProperty hote;
        private final SimpleStringProperty visiteur;
        private final SimpleStringProperty score;

        public PairingDisplayEntry(String status, String hote, String visiteur, String score) {
            this.status = new SimpleStringProperty(status);
            this.hote = new SimpleStringProperty(hote);
            this.visiteur = new SimpleStringProperty(visiteur);
            this.score = new SimpleStringProperty(score);
        }

        public String getStatus() { return status.get(); }
        public SimpleStringProperty statusProperty() { return status; }
        public String getHote() { return hote.get(); }
        public SimpleStringProperty hoteProperty() { return hote; }
        public String getVisiteur() { return visiteur.get(); }
        public SimpleStringProperty visiteurProperty() { return visiteur; }
        public String getScore() { return score.get(); }
        public SimpleStringProperty scoreProperty() { return score; }
    }

    private MainController controller;

    @Override
    public void start(Stage primaryStage) {
        controller = new MainController(primaryStage);

        BorderPane rootLayout = new BorderPane();
        rootLayout.setPadding(new Insets(20));

        // --- TOP: Titre et barre d'outils ---
        VBox topSection = new VBox(15);
        topSection.setPadding(new Insets(0, 0, 20, 0));

        Label titleLabel = new Label("Application d'appariement linguistique");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER_LEFT);

        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        Button btnChargerCsv = new Button("[C] Charger CSV");
        Button btnSauvegarderResultat = new Button("[S] Sauvegarder Résultat");
        Button btnReglages = new Button("[R] Réglages");
        toolbar.getChildren().addAll(btnChargerCsv, btnSauvegarderResultat, btnReglages);
        
        topSection.getChildren().addAll(titleLabel, toolbar, new Separator());
        rootLayout.setTop(topSection);

        // --- CENTER: Semection des pays et liste des appariements ---
        VBox centerSection = new VBox(20);
        centerSection.setPadding(new Insets(10, 0, 10, 0));

        // Selection des pays
        VBox countrySelectionBox = new VBox(10);
        Label lblSelectionPays = new Label("Selection des pays");
        lblSelectionPays.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        HBox countryChoosers = new HBox(10);
        countryChoosers.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> comboPaysA = new ComboBox<>();
        comboPaysA.setPromptText("PAYS A");
        comboPaysA.getItems().addAll("FRANCE", "ALLEMAGNE", "ESPAGNE", "ITALIE", "ROYAUME-UNI"); 
        comboPaysA.setValue("FRANCE");

        Label arrowLabel = new Label("➔");
        arrowLabel.setFont(Font.font("Arial", 20));

        ComboBox<String> comboPaysB = new ComboBox<>();
        comboPaysB.setPromptText("PAYS B");
        comboPaysB.getItems().addAll("ALLEMAGNE", "FRANCE", "ESPAGNE", "ITALIE", "ROYAUME-UNI"); 
        comboPaysB.setValue("ALLEMAGNE");
        
        HBox.setHgrow(comboPaysA, Priority.ALWAYS);
        HBox.setHgrow(comboPaysB, Priority.ALWAYS);
        
        Button btnValiderPays = new Button("Valider/Apparier");
        btnValiderPays.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-font-weight: bold;");

        countryChoosers.getChildren().addAll(comboPaysA, arrowLabel, comboPaysB, btnValiderPays);
        countrySelectionBox.getChildren().addAll(lblSelectionPays, countryChoosers);
        
        // Liste des appariements
        VBox pairingsListBox = new VBox(10);
        Label lblListeAppariements = new Label("Liste des appariements");
        lblListeAppariements.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TableView<PairingDisplayEntry> tableView = new TableView<>();

        TableColumn<PairingDisplayEntry, String> statusCol = new TableColumn<>("");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(40);
        statusCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<PairingDisplayEntry, String> hoteCol = new TableColumn<>("Hôte");
        hoteCol.setCellValueFactory(new PropertyValueFactory<>("hote"));
        hoteCol.setPrefWidth(200);

        TableColumn<PairingDisplayEntry, String> visiteurCol = new TableColumn<>("Visiteur");
        visiteurCol.setCellValueFactory(new PropertyValueFactory<>("visiteur"));
        visiteurCol.setPrefWidth(200);

        TableColumn<PairingDisplayEntry, String> scoreCol = new TableColumn<>("Score affinité");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreCol.setPrefWidth(100);
        scoreCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        tableView.getColumns().addAll(statusCol, hoteCol, visiteurCol, scoreCol);
        tableView.setPrefHeight(250);

        pairingsListBox.getChildren().addAll(lblListeAppariements, tableView);

        centerSection.getChildren().addAll(countrySelectionBox, new Separator(), pairingsListBox);
        rootLayout.setCenter(centerSection);

        // --- BOTTOM: Boutons d'actions ---
        HBox bottomSection = new HBox(10);
        bottomSection.setPadding(new Insets(20, 0, 0, 0));
        bottomSection.setAlignment(Pos.CENTER_LEFT);

        Button btnVoirDetails = new Button("Voir les détails");
        btnVoirDetails.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-font-weight: bold;");
        
        Button btnGestionAppariements = new Button("Gestion des appariements");
        
        bottomSection.getChildren().addAll(btnVoirDetails, btnGestionAppariements);
        
        VBox bottomContainer = new VBox(bottomSection, new Separator());
        rootLayout.setBottom(bottomContainer);

        controller.initializeControls(
                tableView, 
                comboPaysA, comboPaysB, 
                btnChargerCsv, btnValiderPays
        );

        btnSauvegarderResultat.setOnAction(e -> System.out.println("Bouton 'Sauvegarder Résultat' cliqué"));
        btnReglages.setOnAction(e -> System.out.println("Bouton 'Réglages' cliqué"));
        btnVoirDetails.setOnAction(e -> System.out.println("Bouton 'Voir les détails' cliqué"));
        btnGestionAppariements.setOnAction(e -> System.out.println("Bouton 'Gestion des appariements' cliqué"));

        Scene scene = new Scene(rootLayout, 850, 750);
        primaryStage.setTitle("Application d'Appariement Linguistique - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 