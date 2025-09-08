package com.example.p1aloritmos;

import DeckOfCards.CartaInglesa;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import solitaire.DrawPile;
import solitaire.FoundationDeck;
import solitaire.SolitaireGame;
import solitaire.TableauDeck;
import solitaire.WastePile;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SolitaireController implements Initializable {

    private static final boolean DEBUG = false;
    private static void d(String s){ if(DEBUG) System.out.println(s); }

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

    private List<VBox> tableauCols;
    private List<StackPane> foundationPanes;

    private SolitaireGame game;

    // Cronometro
    private Timeline timer;
    private int secondsElapsed = 0;

    // Dimensiones de las cartas
    private static final int CARD_WIDTH = 90;
    private static final int CARD_HEIGHT = 120;

    // Solapamiento vertical
    private static final double OVERLAP_RATIO = 0.8;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        game = new SolitaireGame();

        tableauCols = List.of(tableau0, tableau1, tableau2, tableau3, tableau4, tableau5, tableau6);
        foundationPanes = List.of(foundation0, foundation1, foundation2, foundation3);

        if (tableauRow != null) tableauRow.setSpacing(4);

        // Instalar drop en foundations
        for (StackPane pane : foundationPanes) {
            installFoundationDrop(pane);
        }

        // Click en Waste: recargar si Draw está vacio
        wastePilePane.setOnMouseClicked(e -> {
            DrawPile draw = getReflective("drawPile", DrawPile.class);
            WastePile waste = getReflective("wastePile", WastePile.class);
            if (draw != null && waste != null && !draw.hayCartas() && waste.hayCartas()) {
                game.reloadDrawPile();
                renderAll();
            }
        });

        // Click en Draw roba carta(s)
        drawPilePane.setOnMouseClicked(e -> onDraw());

        startTimer();
        renderAll();
    }

    // Cronometro
    private void startTimer() {
        if (timer != null) timer.stop();
        secondsElapsed = 0;
        if (timerLabel != null) timerLabel.setText("⏱ 00:00");
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            if (timerLabel != null) timerLabel.setText("⏱ " + formatTime(secondsElapsed));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void stopTimer() {
        if (timer != null) timer.stop();
    }

    private String formatTime(int totalSecs) {
        int mins = totalSecs / 60;
        int secs = totalSecs % 60;
        return String.format("%02d:%02d", mins, secs);
    }

   //invocaciones de la logica
    @FXML
    private void onDraw() {
        game.drawCards();
        renderAll();
    }

    @FXML
    private void onNewGame() {
        game = new SolitaireGame();
        startTimer();
        renderAll();
    }

    @FXML
    private void onExit() {
        javafx.application.Platform.exit();
    }

    //Actualizar
    private void renderAll() {
        renderDrawPile();
        renderWastePile();
        renderFoundations();
        renderTableau();

        if (game.isGameOver()) {
            setStatus("🎉 ¡Ganaste!");
            stopTimer();
            showVictoryAlert();
        } else {
            setStatus("");
        }
    }

    private void renderDrawPile() {
        drawPilePane.getChildren().clear();
        DrawPile draw = getReflective("drawPile", DrawPile.class);
        if (draw == null || !draw.hayCartas()) {
            decorateAsSlot(drawPilePane, "Draw");
        } else {
            drawPilePane.getChildren().add(makeCardBack());
        }
    }

    private void renderWastePile() {
        wastePilePane.getChildren().clear();
        WastePile waste = getReflective("wastePile", WastePile.class);
        if (waste == null || !waste.hayCartas()) {
            decorateAsSlot(wastePilePane, "Waste");
        } else {
            CartaInglesa top = waste.verCarta();
            StackPane topCard = makeCardNode(top);

            topCard.setOnDragDetected(e -> {
                Dragboard db = topCard.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString("WASTE");
                db.setContent(content);
                // Vista de arrastre opcional
                try { db.setDragView(topCard.snapshot(new SnapshotParameters(), null)); } catch (Exception ignore) {}
                d("[DRAG] desde WASTE");
                e.consume();
            });
            topCard.setCursor(Cursor.HAND);
            topCard.setOnDragDone(e -> {
                d("[DRAG DONE] WASTE → dropCompleted=" + e.isDropCompleted());
                renderAll();
            });

            wastePilePane.getChildren().setAll(topCard);
        }
    }

    private void renderFoundations() {
        ArrayList<FoundationDeck> fnds = getReflective("foundation", ArrayList.class);
        if (fnds == null) return;

        for (int i = 0; i < foundationPanes.size(); i++) {
            StackPane pane = foundationPanes.get(i);
            pane.getChildren().clear();

            FoundationDeck fd = fnds.get(i);
            CartaInglesa top = fd.getUltimaCarta();
            if (top == null) {
                decorateAsSlot(pane, "F" + (i + 1));
            } else {
                pane.getChildren().setAll(makeCardNode(top));
            }
        }
    }

    // Victoria
    private void showVictoryAlert() {
        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("¡Felicidades!");
        alert.setHeaderText("Has ganado el Solitario 🎉");
        alert.setContentText("Excelente trabajo, completaste el juego en " + formatTime(secondsElapsed) + ".");
        alert.setOnHidden(evt -> javafx.application.Platform.exit());
        alert.showAndWait();
    }

    private void renderTableau() {
        ArrayList<TableauDeck> tabs = getReflective("tableau", ArrayList.class);
        if (tabs == null) return;

        double overlap = -(CARD_HEIGHT * OVERLAP_RATIO);

        for (int i = 0; i < tableauCols.size(); i++) {
            VBox col = tableauCols.get(i);
            col.getChildren().clear();
            col.setPickOnBounds(true);

            TableauDeck td = tabs.get(i);
            ArrayList<CartaInglesa> cards = td.getCards();
            List<StackPane> nodes = new ArrayList<>();

            for (int r = 0; r < cards.size(); r++) {
                CartaInglesa c = cards.get(r);
                StackPane cardNode = makeCardNode(c);
                if (r > 0) VBox.setMargin(cardNode, new Insets(overlap, 0, 0, 0));

                if (c.isFaceup()) {
                    final int tableauIndex = i + 1; // 1..7
                    cardNode.setCursor(Cursor.HAND);
                    cardNode.setOnDragDetected(e -> {
                        Dragboard db = cardNode.startDragAndDrop(TransferMode.ANY);
                        ClipboardContent content = new ClipboardContent();
                        content.putString("TABLEAU:" + tableauIndex);
                        db.setContent(content);
                        try { db.setDragView(cardNode.snapshot(new SnapshotParameters(), null)); } catch (Exception ignore) {}
                        d("[DRAG] desde TABLEAU:" + tableauIndex);
                        e.consume();
                    });
                    cardNode.setOnDragDone(e -> {
                        d("[DRAG DONE] TABLEAU:" + tableauIndex + " → dropCompleted=" + e.isDropCompleted());
                        renderAll();
                    });
                } else {
                    cardNode.setCursor(Cursor.DEFAULT);
                }

                nodes.add(cardNode);
            }

            col.getChildren().addAll(nodes);

            if (cards.isEmpty()) {
                decorateAsSlot(col, "T" + (i + 1));
            }

            final int destIndex = i + 1;

            installTableauDrop(col, destIndex);
            for (javafx.scene.Node child : col.getChildren()) {
                installTableauDrop(child, destIndex);
            }
        }
    }

    //  Drop handlers
    private void installTableauDrop(javafx.scene.Node target, int destIndex) {
        target.setOnDragOver(e -> {
            // evitar auto-drop
            if (e.getGestureSource() != target && e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.ANY);
            }
            e.consume();
        });

        target.setOnDragDropped(e -> {
            String data = e.getDragboard().getString();
            boolean moved = false;

            if (data != null) {
                if (data.startsWith("TABLEAU:")) {
                    int sourceCol = Integer.parseInt(data.split(":")[1]); // 1..7
                    d("[DROP] TABLEAU:" + sourceCol + " → T" + destIndex);
                    moved = game.moveTableauToTableau(sourceCol, destIndex);
                } else if ("WASTE".equals(data)) {
                    d("[DROP] WASTE → T" + destIndex);
                    moved = game.moveWasteToTableau(destIndex);
                }
            }
            d("   moved=" + moved);

            renderAll();                 // refresh
            e.setDropCompleted(moved);
            e.consume();
        });
    }

    private void installFoundationDrop(StackPane pane) {
        pane.setOnDragOver(e -> {
            if (e.getGestureSource() != pane && e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.ANY);
            }
            e.consume();
        });
        pane.setOnDragDropped(e -> {
            String data = e.getDragboard().getString();
            boolean moved = false;

            if ("WASTE".equals(data)) {
                d("[DROP] WASTE → FOUNDATION");
                moved = game.moveWasteToFoundation();
            } else if (data != null && data.startsWith("TABLEAU:")) {
                int sourceCol = Integer.parseInt(data.split(":")[1]);
                d("[DROP] TABLEAU:" + sourceCol + " → FOUNDATION");
                moved = game.moveTableauToFoundation(sourceCol);
            }
            d("   moved=" + moved);

            renderAll();
            e.setDropCompleted(moved);
            e.consume();
        });
    }

    // invoca metodos de SolitaireGame
    @SuppressWarnings("unchecked")
    private <T> T getReflective(String fieldName, Class<T> type) {
        try {
            Field f = SolitaireGame.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            return (T) f.get(game);
        } catch (Exception e) {
            return null;
        }
    }

    private void setStatus(String s) {
        if (statusLabel != null) statusLabel.setText(s);
    }

    private String resolveSuitFolder(CartaInglesa c) {
        String s = c.getPalo().toString().toLowerCase();
        if (s.contains("heart") || s.contains("corazon") || s.contains("coraz")) return "heart";
        if (s.contains("diamond") || s.contains("diamant") || s.contains("rombo")) return "diamond";
        if (s.contains("club") || s.contains("treb") || s.contains("trébol") || s.contains("trebol")) return "club";
        return "spade";
    }

    private int resolveValue(CartaInglesa c) {
        int v = c.getValor();
        if (v <= 0) return 1;
        if (v == 14) return 1;
        return Math.min(v, 13);
    }
    // Si no esta boca arriba automaticamente mostrar la parte trasera de la carta
    private StackPane makeCardNode(CartaInglesa c) {
        if (!c.isFaceup()) {
            return makeCardBack();
        }

        StackPane root = new StackPane();
        root.setPrefSize(CARD_WIDTH, CARD_HEIGHT);

        int valor = resolveValue(c);
        String folder = resolveSuitFolder(c);
        String fileName = valor + "_" + folder + ".png";
        String path = "/com/example/p1aloritmos/individuals/" + folder + "/" + fileName;

        try {
            var is = getClass().getResourceAsStream(path);
            if (is == null) throw new IllegalArgumentException("No image: " + path);
            ImageView img = new ImageView(new Image(is));
            img.setFitWidth(CARD_WIDTH);
            img.setFitHeight(CARD_HEIGHT);
            root.getChildren().add(img);
        } catch (Exception ex) {
            Rectangle rect = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
            rect.setArcWidth(16);
            rect.setArcHeight(16);
            rect.setStyle("-fx-fill: white; -fx-stroke: #d33; -fx-stroke-width: 2;");
            Text t = new Text(c.toString());
            t.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #d33;");
            root.getChildren().addAll(rect, t);
            System.err.println("Falta PNG para " + c + " → " + path);
        }

        return root;
    }

    private StackPane makeCardBack() {
        StackPane root = new StackPane();
        root.setPrefSize(CARD_WIDTH, CARD_HEIGHT);

        String path = "/com/example/p1aloritmos/individuals/cardBack/card_back.png";
        try {
            var is = getClass().getResourceAsStream(path);
            if (is == null) throw new IllegalArgumentException("No back image: " + path);
            ImageView img = new ImageView(new Image(is));
            img.setFitWidth(CARD_WIDTH);
            img.setFitHeight(CARD_HEIGHT);
            root.getChildren().add(img);
        } catch (Exception ex) {
            Rectangle rect = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
            rect.setArcWidth(16);
            rect.setArcHeight(16);
            rect.setStyle("-fx-fill: #0044aa; -fx-stroke: #000; -fx-stroke-width: 1.5;");
            Text t = new Text("🂠");
            t.setStyle("-fx-font-size: 28px; -fx-fill: white;");
            root.getChildren().addAll(rect, t);
            System.err.println("Falta imagen de reverso: " + path);
        }

        return root;
    }

    private void decorateAsSlot(StackPane pane, String text) {
        Rectangle rect = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
        rect.setArcWidth(16);
        rect.setArcHeight(16);
        rect.setStyle("-fx-fill: rgba(255,255,255,0.05); -fx-stroke: #ccc; -fx-stroke-dash-array: 6 6;");
        Text t = new Text(text);
        t.setStyle("-fx-fill: #888;");
        pane.getChildren().setAll(rect, t);
    }

    private void decorateAsSlot(VBox box, String text) {
        StackPane pane = new StackPane();
        pane.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        Rectangle rect = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
        rect.setArcWidth(16);
        rect.setArcHeight(16);
        rect.setStyle("-fx-fill: rgba(255,255,255,0.05); -fx-stroke: #ccc; -fx-stroke-dash-array: 6 6;");
        Text t = new Text(text);
        t.setStyle("-fx-fill: #888;");
        pane.getChildren().addAll(rect, t);
        pane.setMouseTransparent(true);
        box.getChildren().add(pane);
    }
}
