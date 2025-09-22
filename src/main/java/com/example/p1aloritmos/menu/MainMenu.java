package com.example.p1aloritmos.menu;

import com.example.p1aloritmos.SolitaireApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class MainMenu {

    private final SolitaireApp app;
    private final StackPane root;

    public MainMenu(SolitaireApp app) {
        this.app = app;

        // Botones principales
        Button playButton = new Button("Play");
        Button optionsButton = new Button("Options");
        Button exitButton = new Button("Exit");

        // Estilo de botones
        for (Button btn : new Button[]{playButton, optionsButton, exitButton}) {
            btn.setPrefWidth(200);
            btn.setStyle("-fx-font-size: 18px; -fx-background-color: white; -fx-text-fill: #2E2E2E; "
                    + "-fx-background-radius: 10; -fx-cursor: hand;");
        }

        // Acciones de los botones
        playButton.setOnAction(e -> app.showGame());
        optionsButton.setOnAction(e -> app.showOptions());
        exitButton.setOnAction(e -> System.exit(0));

        // Logo
        ImageView logoView = new ImageView(
                new Image(getClass().getResourceAsStream("/com/example/p1aloritmos/individuals/icons/solitarioLogo.jpg"))
        );
        logoView.setFitWidth(150);
        logoView.setPreserveRatio(true);

        // Layout con logo y botones
        VBox menuBox = new VBox(30, logoView, playButton, optionsButton, exitButton);
        menuBox.setAlignment(Pos.CENTER);

        BackgroundFill backgroundFill = new BackgroundFill(
                Color.web("#00ADF1"),
                CornerRadii.EMPTY,
                Insets.EMPTY
        );

        root = new StackPane(menuBox);
        root.setBackground(new Background(backgroundFill));
    }

    public StackPane getRoot() {
        return root;
    }
}
