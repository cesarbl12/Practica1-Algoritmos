package com.example.p1aloritmos.ui;

import com.example.p1aloritmos.SolitaireController;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonManager {
    private final Button undoButton, newGameButton, exitButton;
    private final SolitaireController controller;

    public ButtonManager(Button undoButton, Button newGameButton, Button exitButton, SolitaireController controller) {
        this.undoButton = undoButton;
        this.newGameButton = newGameButton;
        this.exitButton = exitButton;
        this.controller = controller;
    }

    public void setButtonIcons() {
        setButtonIcon(undoButton, "/com/example/p1aloritmos/individuals/icons/back.png");
        setButtonIcon(newGameButton, "/com/example/p1aloritmos/individuals/icons/reiniciar.png");
        setButtonIcon(exitButton, "/com/example/p1aloritmos/individuals/icons/salir.png");
    }

    private void setButtonIcon(Button button, String resourcePath) {
        try {
            var is = getClass().getResourceAsStream(resourcePath);
            if (is == null) throw new IllegalArgumentException("No se encontró: " + resourcePath);

            ImageView img = new ImageView(new Image(is));
            img.setFitWidth(48);
            img.setFitHeight(48);
            img.setPreserveRatio(true);
            img.setSmooth(true);

            button.setGraphic(img);
            button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

            button.setOnMouseEntered(e -> {
                img.setFitWidth(54);
                img.setFitHeight(54);
            });
            button.setOnMouseExited(e -> {
                img.setFitWidth(48);
                img.setFitHeight(48);
            });

        } catch (Exception e) {
            System.err.println("Error cargando ícono: " + resourcePath);
        }
    }
}
