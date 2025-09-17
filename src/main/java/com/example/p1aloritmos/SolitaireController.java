package com.example.p1aloritmos;

import com.example.p1aloritmos.ui.BackgroundManager;
import com.example.p1aloritmos.ui.ButtonManager;
import com.example.p1aloritmos.ui.GameRenderer;
import com.example.p1aloritmos.ui.TimerManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import solitaire.DrawPile;
import solitaire.SolitaireGame;
import solitaire.WastePile;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SolitaireController implements Initializable {

    // Labels
    @FXML private Label statusLabel;
    @FXML private Label timerLabel;

    // Piles
    @FXML private StackPane drawPilePane;
    @FXML private StackPane wastePilePane;
    @FXML private StackPane foundation0;
    @FXML private StackPane foundation1;
    @FXML private StackPane foundation2;
    @FXML private StackPane foundation3;

    // Tableau
    @FXML private VBox tableau0;
    @FXML private VBox tableau1;
    @FXML private VBox tableau2;
    @FXML private VBox tableau3;
    @FXML private VBox tableau4;
    @FXML private VBox tableau5;
    @FXML private VBox tableau6;

    // Layout
    @FXML private HBox tableauRow;
    @FXML private BorderPane rootPane;
    @FXML private HBox topBar;

    // Buttons
    @FXML private Button undoButton;
    @FXML private Button newGameButton;
    @FXML private Button exitButton;

    // Helpers
    private SolitaireGame game;
    private GameRenderer renderer;
    private TimerManager timerManager;
    private ButtonManager buttonManager;
    private BackgroundManager backgroundManager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Modelo del juego
        game = new SolitaireGame();

        // Helpers
        renderer = new GameRenderer(this, game);
        timerManager = new TimerManager(timerLabel);
        buttonManager = new ButtonManager(undoButton, newGameButton, exitButton, this);
        backgroundManager = new BackgroundManager(rootPane, topBar);

        // Estilos iniciales
        buttonManager.setButtonIcons();
        backgroundManager.setBackgroundImage();

        // Click en waste → recargar drawPile
        wastePilePane.setOnMouseClicked(e -> {
            DrawPile draw = game.getDrawPile();
            WastePile waste = game.getWastePile();
            if (draw != null && waste != null && !draw.hayCartas() && waste.hayCartas()) {
                game.reloadDrawPile();
                renderer.renderAll();
            }
        });

        // Click en draw → robar cartas
        drawPilePane.setOnMouseClicked(e -> {
            game.drawCards();
            renderer.renderAll();
        });

        // Iniciar render y cronometro
        timerManager.startTimer();
        renderer.renderAll();
    }

    // Accion deshacer movimiento
    @FXML
    private void onUndo() {
        if (game.undoLastMove()) {
            renderer.renderAll();
            setStatus("⏪ Movimiento deshecho");
        } else {
            setStatus("⚠ No hay movimientos para deshacer");
        }
    }

    // Accion nuevo juego
    @FXML
    private void onNewGame() {
        game = new SolitaireGame();
        renderer.setGame(game);
        timerManager.startTimer();
        renderer.renderAll();
    }

    // Accion salir
    @FXML
    private void onExit() {
        javafx.application.Platform.exit();
    }

    // Getters
    public Parent getRoot() { return rootPane; }
    public void setStatus(String s) { if (statusLabel != null) statusLabel.setText(s); }
    public Label getTimerLabel() { return timerLabel; }
    public List<VBox> getTableauCols() { return List.of(tableau0, tableau1, tableau2, tableau3, tableau4, tableau5, tableau6); }
    public List<StackPane> getFoundationPanes() { return List.of(foundation0, foundation1, foundation2, foundation3); }
    public StackPane getDrawPilePane() { return drawPilePane; }
    public StackPane getWastePilePane() { return wastePilePane; }
    public BorderPane getRootPane() { return rootPane; }
    public HBox getTopBar() { return topBar; }
}
