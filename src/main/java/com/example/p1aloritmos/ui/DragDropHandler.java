package com.example.p1aloritmos.ui;

import com.example.p1aloritmos.SolitaireController;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import solitaire.SolitaireGame;

public class DragDropHandler {
    private SolitaireGame game;
    private final SolitaireController controller;
    private final GameRenderer renderer;

    public DragDropHandler(SolitaireGame game, SolitaireController controller, GameRenderer renderer) {
        this.game = game;
        this.controller = controller;
        this.renderer = renderer;
    }

    public void setGame(SolitaireGame game) {
        this.game = game;
    }

    //  Waste
    public void enableWasteDrag(StackPane cardNode) {
        cardNode.setOnDragDetected(e -> {
            Dragboard db = cardNode.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("WASTE");
            db.setContent(content);
            e.consume();
        });
        cardNode.setOnDragDone(e -> renderer.renderAll());
    }

    //  Tableau (drag)
    public void enableTableauDrag(StackPane cardNode, int tableauIndex) {
        cardNode.setOnDragDetected(e -> {
            Dragboard db = cardNode.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("TABLEAU:" + tableauIndex);
            db.setContent(content);
            e.consume();
        });
        cardNode.setOnDragDone(e -> renderer.renderAll());
    }

    //  Tableau (drop)
    public void enableTableauDrop(Node target, int destIndex) {
        target.setOnDragOver(e -> {
            if (e.getDragboard().hasString()) e.acceptTransferModes(TransferMode.MOVE);
            e.consume();
        });

        target.setOnDragDropped(e -> {
            String data = e.getDragboard().getString();
            boolean moved = false;

            if (data != null) {
                if (data.startsWith("TABLEAU:")) {
                    int sourceCol = Integer.parseInt(data.split(":")[1]);
                    moved = game.moveTableauToTableau(sourceCol, destIndex);
                } else if ("WASTE".equals(data)) {
                    moved = game.moveWasteToTableau(destIndex);
                }
            }

            renderer.renderAll();
            e.setDropCompleted(moved);
            e.consume();
        });
    }

    //  Foundation
    public void enableFoundationDrop(StackPane pane, int index) {
        pane.setOnDragOver(e -> {
            if (e.getDragboard().hasString()) e.acceptTransferModes(TransferMode.MOVE);
            e.consume();
        });

        pane.setOnDragDropped(e -> {
            String data = e.getDragboard().getString();
            boolean moved = false;

            if ("WASTE".equals(data)) {
                moved = game.moveWasteToFoundation();
            } else if (data != null && data.startsWith("TABLEAU:")) {
                int sourceCol = Integer.parseInt(data.split(":")[1]);
                moved = game.moveTableauToFoundation(sourceCol);
            }

            renderer.renderAll();
            e.setDropCompleted(moved);
            e.consume();
        });
    }
}
