// com/example/p1aloritmos/ui/BackgroundManager.java
package com.example.p1aloritmos.ui;

import com.example.p1aloritmos.menu.GameSettings;
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

    /**
     * Aplica el fondo de pantalla usando la ruta configurada en GameSettings.
     * Si no encuentra la imagen, lo reporta en consola.
     */
    public void setBackgroundImage() {
        String path = GameSettings.getBackgroundPath();
        try {
            var is = Objects.requireNonNull(
                    getClass().getResourceAsStream(path),
                    "No se encontró el recurso: " + path
            );

            Image bgImage = new Image(is);
            BackgroundSize bgSize = new BackgroundSize(
                    100, 100, true, true, true, true
            );
            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    bgSize
            );

            Background background = new Background(backgroundImage);

            if (rootPane != null) {
                rootPane.setBackground(background);
            }
            if (topBar != null) {
                topBar.setBackground(background);
            }

            System.out.println("[BackgroundManager] Fondo aplicado: " + path);
        } catch (Exception ex) {
            System.err.println("[BackgroundManager] Error cargando fondo: " + ex.getMessage());
        }
    }
}
