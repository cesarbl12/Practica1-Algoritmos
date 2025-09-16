package com.example.p1aloritmos;

import com.example.p1aloritmos.ui.BackgroundManager;
import com.example.p1aloritmos.ui.ButtonManager;
import com.example.p1aloritmos.ui.GameRenderer;
import com.example.p1aloritmos.ui.TimerManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    // ---------- UI (inyectado por FXML) ----------
    @FXML private Label statusLabel;
    @FXML private Label timerLabel;

    @FXML private StackPane drawPilePane;
    @FXML private StackPane wastePilePane;

    @FXML private StackPane foundation0;
    @FXML private StackPane foundation1;
    @FXML private StackPane foundation2;
    @FXML private StackPane foundation3;

    @FXML private VBox tableau0;
    @FXML private VBox tableau1;
    @FXML private VBox tableau2;
    @FXML private VBox tableau3;
    @FXML private VBox tableau4;
    @FXML private VBox tableau5;
    @FXML private VBox tableau6;

    @FXML private HBox tableauRow;
    @FXML private BorderPane rootPane;
    @FXML private HBox topBar;

    // Botones con iconos
    @FXML private Button undoButton;
    @FXML private Button newGameButton;
    @FXML private Button exitButton;

    // ---------- Estado / helpers ----------
    private SolitaireGame game;
    private GameRenderer renderer;
    private TimerManager timerManager;
    private ButtonManager buttonManager;
    private BackgroundManager backgroundManager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Modelo
        game = new SolitaireGame();

        // Helpers
        renderer = new GameRenderer(this, game);                 // <-- aquí dentro se crea DragDropHandler
        timerManager = new TimerManager(timerLabel);
        buttonManager = new ButtonManager(undoButton, newGameButton, exitButton, this);
        backgroundManager = new BackgroundManager(rootPane, topBar);

        // Estética
        buttonManager.setButtonIcons();
        backgroundManager.setBackgroundImage("/com/example/p1aloritmos/individuals/background_2.png");

        // Interacciones simples (clic para robar / recargar)
        wastePilePane.setOnMouseClicked(e -> {
            DrawPile draw = game.getDrawPile();
            WastePile waste = game.getWastePile();
            if (draw != null && waste != null && !draw.hayCartas() && waste.hayCartas()) {
                game.reloadDrawPile();
                renderer.renderAll();
            }
        });
        drawPilePane.setOnMouseClicked(e -> {
            game.drawCards();
            renderer.renderAll();
        });

        // Render inicial
        timerManager.startTimer();
        renderer.renderAll();
    }

    // ---------- Acciones ----------
    @FXML
    private void onUndo() {
        if (game.undoLastMove()) {
            renderer.renderAll();
            setStatus("⏪ Movimiento deshecho");
        } else {
            setStatus("⚠ No hay movimientos para deshacer");
        }
    }

    @FXML
    private void onNewGame() {
        game = new SolitaireGame();
        renderer.setGame(game);  // informar al renderer del nuevo modelo
        timerManager.startTimer();
        renderer.renderAll();
    }

    @FXML
    private void onExit() {
        javafx.application.Platform.exit();
    }

    // ---------- Utilidades expuestas para helpers ----------
    public void setStatus(String s) {
        if (statusLabel != null) statusLabel.setText(s);
    }

    public Label getTimerLabel() { return timerLabel; }

    public List<VBox> getTableauCols() {
        return List.of(tableau0, tableau1, tableau2, tableau3, tableau4, tableau5, tableau6);
    }

    public List<StackPane> getFoundationPanes() {
        return List.of(foundation0, foundation1, foundation2, foundation3);
    }

    public StackPane getDrawPilePane() { return drawPilePane; }
    public StackPane getWastePilePane() { return wastePilePane; }
}
