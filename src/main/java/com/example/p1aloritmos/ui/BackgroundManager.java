package com.example.p1aloritmos.ui;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.Objects;

public class BackgroundManager {
    private final BorderPane rootPane;
    private final HBox topBar;

    public BackgroundManager(BorderPane rootPane, HBox topBar) {
        this.rootPane = rootPane;
        this.topBar = topBar;
    }

    public void setBackgroundImage(String resourcePath) {
        try {
            Image bgImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(resourcePath)));
            BackgroundSize bgSize = new BackgroundSize(100, 100, true, true, true, true);
            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    bgSize
            );

            Background background = new Background(backgroundImage);
            if (rootPane != null) rootPane.setBackground(background);
            if (topBar != null) topBar.setBackground(background);

        } catch (Exception ex) {
            System.err.println("No se pudo cargar el fondo: " + ex.getMessage());
        }
    }
}
