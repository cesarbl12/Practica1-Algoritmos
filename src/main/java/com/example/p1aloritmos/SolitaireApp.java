package com.example.p1aloritmos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SolitaireApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/com/example/p1aloritmos/solitaire.fxml"));
        Scene scene = new Scene(root);

        // 🔹 Cambiar título con símbolo de carta
        stage.setTitle("Solitaire");

        // 🔹 Asignar icono (usa solitarioLogo.jpg de la carpeta /icons)
        stage.getIcons().add(new Image(
                getClass().getResourceAsStream("/com/example/p1aloritmos/individuals/icons/solitarioLogo.jpg")
        ));

        stage.setScene(scene);
        stage.setWidth(1120);
        stage.setHeight(760);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
