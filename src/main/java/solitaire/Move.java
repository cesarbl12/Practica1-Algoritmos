package solitaire;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;
import java.util.List;

/**
 * Movimiento con opción de deshacer, incluyendo el volteo de cartas.
 */
public class Move {
    private final Object from;
    private final Object to;
    private final List<CartaInglesa> moved;

    // Datos extra para revertir si el origen fue un Tableau
    private final TableauDeck sourceTableau;
    private final CartaInglesa flippedAtSourceTop;

    public Move(Object from, Object to, List<CartaInglesa> moved) {
        this(from, to, moved, null, null);
    }

    public Move(Object from, Object to, List<CartaInglesa> moved,
                TableauDeck sourceTableau, CartaInglesa flippedAtSourceTop) {
        this.from = from;
        this.to = to;
        this.moved = new ArrayList<>(moved);
        this.sourceTableau = sourceTableau;
        this.flippedAtSourceTop = flippedAtSourceTop;
    }

    public void undo() {
        // 1) Quitar del destino
        if (to instanceof TableauDeck dest) {
            for (int i = 0; i < moved.size(); i++) dest.removerUltimaCarta();
        } else if (to instanceof WastePile dest) {
            for (int i = 0; i < moved.size(); i++) dest.getCarta();
        } else if (to instanceof FoundationDeck dest) {
            for (int i = 0; i < moved.size(); i++) dest.removerUltimaCarta();
        }

        // 2) Volver a tapar carta en el origen si se descubrió antes
        if (sourceTableau != null && flippedAtSourceTop != null) {
            flippedAtSourceTop.makeFaceDown();
        }

        // 3) Regresar cartas al origen sin validar reglas
        if (from instanceof TableauDeck orig) {
            orig.pushBloqueSinValidar(new ArrayList<>(moved));
        } else if (from instanceof WastePile orig) {
            orig.addCartas(new ArrayList<>(moved));
        } else if (from instanceof FoundationDeck orig) {
            for (CartaInglesa c : moved) orig.pushSinValidar(c);
        } else if (from instanceof DrawPile orig) {
            orig.devolverAlFrente(moved);
        }
    }
}
