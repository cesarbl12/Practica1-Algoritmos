package com.example.p1aloritmos;

import com.example.p1aloritmos.menu.MainMenu;
import com.example.p1aloritmos.menu.OptionsMenu;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SolitaireApp extends Application {

    private Stage mainStage;
    private SolitaireController currentController;

    @Override
    public void start(Stage stage) {
        this.mainStage = stage;

        MainMenu menu = new MainMenu(this);
        Scene scene = new Scene(menu.getRoot(), 600, 400);

        stage.setTitle("Solitaire - Main Menu");
        stage.getIcons().add(new Image(
                getClass().getResourceAsStream("/com/example/p1aloritmos/individuals/icons/solitarioLogo.jpg")
        ));

        stage.setScene(scene);
        stage.show();
    }

    public void showGame() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/p1aloritmos/solitaire.fxml")
            );
            Scene gameScene = new Scene(loader.load(), 1120, 760);

            currentController = loader.getController();

            mainStage.setScene(gameScene);
            mainStage.setTitle("Solitaire");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showOptions() {
        OptionsMenu optionsMenu = new OptionsMenu(this);
        Scene scene = new Scene(optionsMenu.getRoot(), 600, 400);
        mainStage.setScene(scene);
        mainStage.setTitle("Options");
    }

    public void showMainMenu() {
        MainMenu menu = new MainMenu(this);
        Scene scene = new Scene(menu.getRoot(), 600, 400);
        mainStage.setScene(scene);
        mainStage.setTitle("Solitaire - Main Menu");
    }

    public javafx.scene.layout.BorderPane getCurrentRootPane() {
        if (currentController != null) {
            return currentController.getRootPane();
        }
        return null;
    }

    public javafx.scene.layout.HBox getCurrentTopBar() {
        if (currentController != null) {
            return currentController.getTopBar();
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
