package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;

public class FoundationDeck {
    private Palo palo;
    private Pila<CartaInglesa> cartas = new Pila<>(13);

    public FoundationDeck(Palo palo) { this.palo = palo; }

    public FoundationDeck(CartaInglesa carta) {
        palo = carta.getPalo();
        if (carta.getValorBajo() == 1) cartas.push(carta);
    }

    public boolean agregarCarta(CartaInglesa carta) {
        if (!carta.tieneElMismoPalo(palo)) return false;
        if (cartas.pila_vacia()) {
            if (carta.getValorBajo() == 1) { cartas.push(carta); return true; }
        } else {
            CartaInglesa top = cartas.peek();
            if (top.getValorBajo() + 1 == carta.getValorBajo()) {
                cartas.push(carta);
                return true;
            }
        }
        return false;
    }

    public void pushSinValidar(CartaInglesa c) { cartas.push(c); }

    public CartaInglesa removerUltimaCarta() { return cartas.pila_vacia() ? null : cartas.pop(); }

    public boolean estaVacio() { return cartas.pila_vacia(); }

    public CartaInglesa getUltimaCarta() { return cartas.pila_vacia() ? null : cartas.peek(); }

    public Palo getPalo() { return palo; }

    @Override
    public String toString() {
        if (cartas.pila_vacia()) return "---";
        StringBuilder b = new StringBuilder();
        Pila<CartaInglesa> temp = new Pila<>(cartas.getSize());
        while (!cartas.pila_vacia()) {
            CartaInglesa c = cartas.pop();
            b.append(c.toString());
            temp.push(c);
        }
        while (!temp.pila_vacia()) cartas.push(temp.pop());
        return b.toString();
    }
}
