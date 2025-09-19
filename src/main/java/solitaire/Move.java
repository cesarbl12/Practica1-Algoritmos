package solitaire;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un movimiento en el Solitario, con soporte para "Undo".
 * Compatible con la pila personalizada {Pila}.
 */
public class Move {
    private final Object from;
    private final Object to;
    private final List<CartaInglesa> moved;

    // Datos extra para revertir si el origen fue un Tableau
    private final TableauDeck sourceTableau;
    private final CartaInglesa flippedAtSourceTop;


     //Constructor simple (sin flip en la carta del origen).
    public Move(Object from, Object to, List<CartaInglesa> moved) {
        this(from, to, moved, null, null);
    }


     //Constructor completo con información adicional para manejar flips de cartas.

    public Move(Object from, Object to, List<CartaInglesa> moved,
                TableauDeck sourceTableau, CartaInglesa flippedAtSourceTop) {
        this.from = from;
        this.to = to;
        this.moved = new ArrayList<>(moved); // Copiamos para seguridad
        this.sourceTableau = sourceTableau;
        this.flippedAtSourceTop = flippedAtSourceTop;
    }

    /**
     * Deshace el movimiento:
     *  1) Quita las cartas del destino.
     *  2) Vuelve a tapar la carta de origen si se había descubierto.
     *  3) Devuelve las cartas al origen, sin validar reglas.
     */
    public void undo() {
        // 1) Quitar del destino
        if (to instanceof TableauDeck dest) {
            for (int i = 0; i < moved.size(); i++) {
                dest.removerUltimaCarta();
            }
        } else if (to instanceof WastePile dest) {
            for (int i = 0; i < moved.size(); i++) {
                dest.getCarta();
            }
        } else if (to instanceof FoundationDeck dest) {
            for (int i = 0; i < moved.size(); i++) {
                dest.removerUltimaCarta();
            }
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
            for (CartaInglesa c : moved) {
                orig.pushSinValidar(c);
            }
        } else if (from instanceof DrawPile orig) {
            orig.devolverAlFrente(moved);
        }
    }
}
