package com.example.p1aloritmos.menu;

import com.example.p1aloritmos.SolitaireApp;
import com.example.p1aloritmos.ui.BackgroundManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class OptionsMenu {
    private final SolitaireApp app;
    private final StackPane root;

    public OptionsMenu(SolitaireApp app) {
        this.app = app;

        // Titulo
        Label title = new Label("Game Options");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Selector de fondo
        Label bgLabel = new Label("Select background:");
        bgLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        ComboBox<String> backgroundSelector = new ComboBox<>();
        backgroundSelector.getItems().addAll("Green", "Blue");
        backgroundSelector.setValue("Background");
        backgroundSelector.setOnAction(e -> {
            if ("Green".equals(backgroundSelector.getValue())) {
                GameSettings.setBackgroundPath("/com/example/p1aloritmos/individuals/background_3.png");
            } else {
                GameSettings.setBackgroundPath("/com/example/p1aloritmos/individuals/background_2.png");
            }

            if (app.getCurrentRootPane() != null && app.getCurrentTopBar() != null) {
                new BackgroundManager(app.getCurrentRootPane(), app.getCurrentTopBar())
                        .setBackgroundImage();
            }
        });

        // Selector de dorso
        Label backLabel = new Label("Select card back:");
        backLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        ComboBox<String> backSelector = new ComboBox<>();
        backSelector.getItems().addAll("Red", "Green", "Blue");
        backSelector.setValue("Card Back");
        backSelector.setOnAction(e -> {
            if ("Red".equals(backSelector.getValue())) {
                GameSettings.setCardBackPath("/com/example/p1aloritmos/individuals/cardBack/cardBackRed.png");
            } else if ("Green".equals(backSelector.getValue())) {
                GameSettings.setCardBackPath("/com/example/p1aloritmos/individuals/cardBack/cardBackGreen.png");
            } else if ("Blue".equals(backSelector.getValue())) {
                GameSettings.setCardBackPath("/com/example/p1aloritmos/individuals/cardBack/card_back.png");
            }
        });

        // Boton volver
        Button backButton = new Button("⬅ Back to main menu");
        backButton.setStyle("-fx-font-size: 16px; -fx-background-color: #ffffff; -fx-text-fill: #2E2E2E; "
                + "-fx-background-radius: 10; -fx-cursor: hand;");
        backButton.setOnAction(e -> app.showMainMenu());

        // Layout
        VBox box = new VBox(20,
                title,
                bgLabel, backgroundSelector,
                backLabel, backSelector,
                backButton
        );
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        box.setStyle("-fx-background-color: linear-gradient(to bottom, #00ADF1, #145214);");

        root = new StackPane(box);
    }

    public StackPane getRoot() {
        return root;
    }
}
