package com.example.p1aloritmos.ui;

import DeckOfCards.CartaInglesa;
import com.example.p1aloritmos.menu.GameSettings;
import com.example.p1aloritmos.SolitaireController;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import solitaire.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GameRenderer {
    private final SolitaireController controller;
    private SolitaireGame game;
    private final DragDropHandler dragDropHandler;

    private static final int CARD_WIDTH = 90;
    private static final int CARD_HEIGHT = 120;
    private static final double OVERLAP_RATIO = 0.8;

    public GameRenderer(SolitaireController controller, SolitaireGame game) {
        this.controller = controller;
        this.game = game;
        this.dragDropHandler = new DragDropHandler(game, controller, this);
    }

    public void setGame(SolitaireGame game) {
        this.game = game;
        dragDropHandler.setGame(game);
    }

    //  Renderizar
    public void renderAll() {
        renderDrawPile();
        renderWastePile();
        renderFoundations();
        renderTableau();

        if (game.isGameOver()) {
            controller.setStatus("🎉 ¡Ganaste!");
            new TimerManager(controller.getTimerLabel()).stopTimer();
            showVictoryAlert();
        } else {
            controller.setStatus("");
        }
    }

    //  Draw pile
    private void renderDrawPile() {
        StackPane drawPane = controller.getDrawPilePane();
        drawPane.getChildren().clear();
        DrawPile draw = getReflective("drawPile", DrawPile.class);

        if (draw == null || !draw.hayCartas()) {
            decorateAsSlot(drawPane, "Draw");
        } else {
            drawPane.getChildren().add(makeCardBack());
        }
    }

    //  Waste pile
    private void renderWastePile() {
        StackPane wastePane = controller.getWastePilePane();
        wastePane.getChildren().clear();
        WastePile waste = getReflective("wastePile", WastePile.class);

        if (waste == null || !waste.hayCartas()) {
            decorateAsSlot(wastePane, "Waste");
        } else {
            CartaInglesa top = waste.verCarta();
            StackPane topCard = makeCardNode(top);
            dragDropHandler.enableWasteDrag(topCard);
            wastePane.getChildren().setAll(topCard);
        }
    }

    //  Foundations
    private void renderFoundations() {
        List<FoundationDeck> fnds = getReflective("foundation", List.class);
        if (fnds == null) return;

        List<StackPane> panes = controller.getFoundationPanes();
        for (int i = 0; i < panes.size(); i++) {
            StackPane pane = panes.get(i);
            pane.getChildren().clear();

            FoundationDeck fd = fnds.get(i);
            CartaInglesa top = fd.getUltimaCarta();
            if (top == null) {
                decorateAsSlot(pane, "F" + (i + 1));
            } else {
                pane.getChildren().setAll(makeCardNode(top));
            }
            dragDropHandler.enableFoundationDrop(pane, i);
        }
    }

    //  Tableau
    private void renderTableau() {
        List<TableauDeck> tabs = getReflective("tableau", List.class);
        if (tabs == null) return;

        double overlap = -(CARD_HEIGHT * OVERLAP_RATIO);
        List<VBox> tableauCols = controller.getTableauCols();

        for (int i = 0; i < tableauCols.size(); i++) {
            VBox col = tableauCols.get(i);
            col.getChildren().clear();
            col.setPickOnBounds(true);

            TableauDeck td = tabs.get(i);
            List<CartaInglesa> cards = td.getCards();
            List<StackPane> nodes = new ArrayList<>();

            for (int r = 0; r < cards.size(); r++) {
                CartaInglesa c = cards.get(r);
                StackPane cardNode = makeCardNode(c);
                if (r > 0) VBox.setMargin(cardNode, new Insets(overlap, 0, 0, 0));

                if (c.isFaceup()) {
                    cardNode.setCursor(Cursor.HAND);
                    dragDropHandler.enableTableauDrag(cardNode, i + 1);
                } else {
                    cardNode.setCursor(Cursor.DEFAULT);
                }
                nodes.add(cardNode);
            }

            col.getChildren().addAll(nodes);
            if (cards.isEmpty()) decorateAsSlot(col, "T" + (i + 1));

            dragDropHandler.enableTableauDrop(col, i + 1);
            for (StackPane node : nodes) {
                dragDropHandler.enableTableauDrop(node, i + 1);
            }
        }
    }

    //  Helpers graficos
    private StackPane makeCardNode(CartaInglesa c) {
        if (!c.isFaceup()) return makeCardBack();

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
        }
        return root;
    }

    private StackPane makeCardBack() {
        StackPane root = new StackPane();
        root.setPrefSize(CARD_WIDTH, CARD_HEIGHT);

        String path = GameSettings.getCardBackPath(); // <-- ahora lee de GameSettings
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

    //  Utils
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

    private void showVictoryAlert() {
        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("¡Felicidades!");
        alert.setHeaderText("Has ganado el Solitario 🎉");
        alert.setContentText("Excelente trabajo, completaste el juego.");
        alert.setOnHidden(evt -> javafx.application.Platform.exit());
        alert.showAndWait();
    }
}
