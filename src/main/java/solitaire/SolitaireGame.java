package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;

import java.util.ArrayList;
import java.util.List;

/**
 * Juego de solitario con historial de movimientos para undo.
 *
 * @version 2025-2
 */
public class SolitaireGame {
    private final ArrayList<TableauDeck> tableau = new ArrayList<>();
    public final ArrayList<FoundationDeck> foundation = new ArrayList<>();
    private FoundationDeck lastFoundationUpdated;
    private DrawPile drawPile;
    private WastePile wastePile;
    private final MoveHistory history = new MoveHistory(); // historial de movimientos

    public SolitaireGame() {
        drawPile = new DrawPile();
        wastePile = new WastePile();
        createTableaux();
        createFoundations();
        // primera descarga al waste
        wastePile.addCartas(drawPile.retirarCartas());
    }

    /** Recargar Draw desde Waste (acciÃ³n global, no se registra como movimiento). */
    public void reloadDrawPile() {
        List<CartaInglesa> cards = wastePile.emptyPile();
        drawPile.recargar(cards);
    }

    /** Robar cartas del Draw al Waste */
    public void drawCards() {
        List<CartaInglesa> cards = drawPile.retirarCartas();
        if (!cards.isEmpty()) {
            wastePile.addCartas(cards);
            history.record(new Move(drawPile, wastePile, cards));
        }
    }

    /** Mover carta del Waste al Tableau */
    public boolean moveWasteToTableau(int tableauDestino) {
        TableauDeck destino = tableau.get(tableauDestino - 1);
        return moveWasteToTableau(destino);
    }

    public boolean moveWasteToTableau(TableauDeck destino) {
        boolean movimientoRealizado = false;
        CartaInglesa carta = wastePile.verCarta();
        if (moveCartaToTableau(carta, destino)) {
            carta = wastePile.getCarta();
            List<CartaInglesa> moved = new ArrayList<>();
            moved.add(carta);
            // No hay flip en fuente (origen = waste)
            history.record(new Move(wastePile, destino, moved));
            movimientoRealizado = true;
        }
        return movimientoRealizado;
    }

    /** Mover bloque entre Tableaux */
    public boolean moveTableauToTableau(int tableauFuente, int tableauDestino) {
        boolean movimientoRealizado = false;
        TableauDeck fuente = tableau.get(tableauFuente - 1);
        if (!fuente.isEmpty()) {
            TableauDeck destino = tableau.get(tableauDestino - 1);

            int valorEsperado;
            if (!destino.isEmpty()) {
                CartaInglesa cartaUltimaDelDestino = destino.verUltimaCarta();
                valorEsperado = cartaUltimaDelDestino.getValor() - 1;
            } else {
                valorEsperado = 13; // Rey
            }

            // Determinar carta inicial del bloque a mover
            CartaInglesa cartaInicial = fuente.viewCardStartingAt(valorEsperado);
            if (cartaInicial != null && destino.sePuedeAgregarCarta(cartaInicial)) {

                // === Detectar la carta que QUEDARÃ arriba en la fuente tras quitar el bloque ===
                // SerÃ¡ la carta inmediatamente anterior a 'cartaInicial' en la columna.
                CartaInglesa flippedAtSourceTop = null;
                List<CartaInglesa> srcCardsBefore = fuente.getCards();
                int idx = srcCardsBefore.indexOf(cartaInicial);
                if (idx > 0) {
                    CartaInglesa candidate = srcCardsBefore.get(idx - 1);
                    // Si estaba boca abajo, el movimiento la destaparÃ¡; debemos recordarlo para el Undo.
                    if (!candidate.isFaceup()) {
                        flippedAtSourceTop = candidate;
                    }
                }

                // Remover bloque y colocarlo en destino
                List<CartaInglesa> bloque = fuente.removeStartingAt(valorEsperado);
                if (destino.agregarBloqueDeCartas(bloque)) {
                    // El juego destapa la carta ahora expuesta (si hay)
                    if (!fuente.isEmpty()) {
                        fuente.verUltimaCarta().makeFaceUp();
                    }
                    // Registrar movimiento, indicando quÃ© carta se destapÃ³ en la fuente (si hubo)
                    history.record(new Move(fuente, destino, bloque, fuente, flippedAtSourceTop));
                    movimientoRealizado = true;
                }
            }
        }
        return movimientoRealizado;
    }

    /** Mover carta de un Tableau a su Foundation */
    public boolean moveTableauToFoundation(int numero) {
        boolean movimientoRealizado = false;
        TableauDeck fuente = tableau.get(numero - 1);

        // === Detectar si se va a destapar la carta de abajo (segunda desde arriba antes de quitar la Ãºltima) ===
        CartaInglesa flippedAtSourceTop = null;
        List<CartaInglesa> srcCardsBefore = fuente.getCards();
        if (srcCardsBefore.size() >= 2) {
            CartaInglesa candidate = srcCardsBefore.get(srcCardsBefore.size() - 2);
            if (!candidate.isFaceup()) {
                flippedAtSourceTop = candidate;
            }
        }

        CartaInglesa carta = fuente.removerUltimaCarta(); // esto destapa la nueva top si existe
        if (carta != null && moveCartaToFoundation(carta)) {
            List<CartaInglesa> moved = new ArrayList<>();
            moved.add(carta);
            history.record(new Move(fuente, lastFoundationUpdated, moved, fuente, flippedAtSourceTop));
            movimientoRealizado = true;
        } else if (carta != null) {
            // Revertir si no se pudo mover a foundation (y reponer el estado visual)
            fuente.agregarCarta(carta);
            // Si la carta de abajo se destapÃ³ por remover, hay que volverla a tapar:
            if (flippedAtSourceTop != null) {
                flippedAtSourceTop.makeFaceDown();
            }
        }
        return movimientoRealizado;
    }

    /** Mover carta del Waste a Foundation */
    public boolean moveWasteToFoundation() {
        boolean movimientoRealizado = false;
        CartaInglesa carta = wastePile.verCarta();
        if (carta != null && moveCartaToFoundation(carta)) {
            carta = wastePile.getCarta();
            List<CartaInglesa> moved = new ArrayList<>();
            moved.add(carta);
            // Origen no es tableau, no hay flip secundario
            history.record(new Move(wastePile, lastFoundationUpdated, moved));
            movimientoRealizado = true;
        }
        return movimientoRealizado;
    }

    private boolean moveCartaToTableau(CartaInglesa carta, TableauDeck destino) {
        return destino != null && carta != null && destino.agregarCarta(carta);
    }

    private boolean moveCartaToFoundation(CartaInglesa carta) {
        if (carta == null) return false;
        int cualFoundation = carta.getPalo().ordinal();
        FoundationDeck destino = foundation.get(cualFoundation);
        lastFoundationUpdated = destino;
        return destino.agregarCarta(carta);
    }

    /** Â¿Juego terminado? */
    public boolean isGameOver() {
        for (FoundationDeck f : foundation) {
            if (f.estaVacio()) return false;
            CartaInglesa ultima = f.getUltimaCarta();
            if (ultima.getValor() != 13) return false;
        }
        return true;
    }

    private void createFoundations() {
        for (Palo palo : Palo.values()) {
            foundation.add(new FoundationDeck(palo));
        }
    }

    private void createTableaux() {
        for (int i = 0; i < 7; i++) {
            TableauDeck t = new TableauDeck();
            List<CartaInglesa> iniciales = drawPile.getCartas(i + 1);
            t.inicializar(iniciales);
            tableau.add(t);
        }
    }

    public DrawPile getDrawPile() { return drawPile; }
    public ArrayList<TableauDeck> getTableau() { return tableau; }
    public WastePile getWastePile() { return wastePile; }
    public FoundationDeck getLastFoundationUpdated() { return lastFoundationUpdated; }

    /** Undo del Ãºltimo movimiento */
    public boolean undoLastMove() {
        if (history.canUndo()) {
            history.undo();
            return true;
        }
        return false;
    }

    public boolean hasUndo() { return history.canUndo(); }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Foundation\n");
        for (FoundationDeck foundationDeck : foundation) {
            str.append(foundationDeck).append("\n");
        }
        str.append("\nTableaux\n");
        int tableauNumber = 1;
        for (TableauDeck tableauDeck : tableau) {
            str.append(tableauNumber).append(" ").append(tableauDeck).append("\n");
            tableauNumber++;
        }
        str.append("Waste\n").append(wastePile).append("\n");
        str.append("Draw\n").append(drawPile);
        return str.toString();
    }
}