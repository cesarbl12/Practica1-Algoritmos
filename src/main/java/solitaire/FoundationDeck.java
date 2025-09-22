package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;

/**
 * Foundation (palo único de As a Rey) implementado con Pila personalizada.
 */
public class FoundationDeck {
    private Palo palo;
    private Pila<CartaInglesa> cartas = new Pila<>();

    public FoundationDeck(Palo palo) {
        this.palo = palo;
    }

    public FoundationDeck(CartaInglesa carta) {
        palo = carta.getPalo();
        if (carta.getValorBajo() == 1) {
            cartas.push(carta);
        }
    }

    public boolean agregarCarta(CartaInglesa carta) {
        if (!carta.tieneElMismoPalo(palo)) return false;
        if (cartas.isEmpty()) {
            if (carta.getValorBajo() == 1) {
                cartas.push(carta);
                return true;
            }
        } else {
            CartaInglesa top = cartas.peek();
            if (top.getValorBajo() + 1 == carta.getValorBajo()) {
                cartas.push(carta);
                return true;
            }
        }
        return false;
    }

    /** Para Undo: push sin validar reglas. */
    public void pushSinValidar(CartaInglesa c) {
        cartas.push(c);
    }

    public CartaInglesa removerUltimaCarta() {
        return cartas.isEmpty() ? null : cartas.pop();
    }

    public boolean estaVacio() {
        return cartas.isEmpty();
    }

    public CartaInglesa getUltimaCarta() {
        return cartas.isEmpty() ? null : cartas.peek();
    }

    public Palo getPalo() {
        return palo;
    }

    @Override
    public String toString() {
        if (cartas.isEmpty()) return "---";
        StringBuilder b = new StringBuilder();
        java.util.List<CartaInglesa> todasCartas = cartas.toList();
        for (CartaInglesa c : todasCartas) {
            b.append(c.toString());
        }
        return b.toString();
    }
}