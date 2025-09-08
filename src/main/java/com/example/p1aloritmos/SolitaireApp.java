package com.example.p1aloritmos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class SolitaireApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/p1aloritmos/solitaire.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Solitario");
        stage.setScene(scene);
        stage.setWidth(1120);
        stage.setHeight(760);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
