package com.example.p1aloritmos.menu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SolitaireGameWindow extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/p1aloritmos/solitaire.fxml"));
        Scene scene = new Scene(root);

        stage.setTitle("Solitaire");
        stage.getIcons().add(new Image(
                getClass().getResourceAsStream("/com/example/p1aloritmos/individuals/icons/solitarioLogo.jpg")
        ));

        stage.setScene(scene);
        stage.setWidth(1120);
        stage.setHeight(760);
        stage.show();
    }
}
