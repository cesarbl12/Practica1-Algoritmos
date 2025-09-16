package solitaire;

import DeckOfCards.CartaInglesa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Tableau (montón de juego) implementado con pila.
 */
public class TableauDeck {
    private Stack<CartaInglesa> cartas = new Stack<>();

    public void inicializar(List<CartaInglesa> cartasIniciales) {
        cartas.clear();
        for (CartaInglesa c : cartasIniciales) cartas.push(c);
        if (!cartas.isEmpty()) cartas.peek().makeFaceUp();
    }

    public List<CartaInglesa> removeStartingAt(int value) {
        List<CartaInglesa> removed = new ArrayList<>();
        Iterator<CartaInglesa> it = cartas.iterator();
        while (it.hasNext()) {
            CartaInglesa next = it.next();
            if (next.isFaceup() && next.getValor() <= value) {
                removed.add(next);
                it.remove();
            }
        }
        return removed;
    }

    public CartaInglesa viewCardStartingAt(int value) {
        for (CartaInglesa c : cartas) {
            if (c.isFaceup() && c.getValor() <= value) return c;
        }
        return null;
    }

    public boolean agregarCarta(CartaInglesa carta) {
        if (sePuedeAgregarCarta(carta)) {
            carta.makeFaceUp();
            cartas.push(carta);
            return true;
        }
        return false;
    }

    public CartaInglesa verUltimaCarta() { return cartas.isEmpty() ? null : cartas.peek(); }

    public CartaInglesa removerUltimaCarta() {
        CartaInglesa ultima = cartas.isEmpty() ? null : cartas.pop();
        if (!cartas.isEmpty()) cartas.peek().makeFaceUp();
        return ultima;
    }

    @Override
    public String toString() {
        if (cartas.isEmpty()) return "---";
        StringBuilder b = new StringBuilder();
        for (CartaInglesa c : cartas) b.append(c.toString());
        return b.toString();
    }

    public boolean agregarBloqueDeCartas(List<CartaInglesa> cartasRecibidas) {
        if (!cartasRecibidas.isEmpty()) {
            CartaInglesa primera = cartasRecibidas.get(0);
            if (sePuedeAgregarCarta(primera)) {
                for (CartaInglesa c : cartasRecibidas) cartas.push(c);
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() { return cartas.isEmpty(); }

    public boolean sePuedeAgregarCarta(CartaInglesa carta) {
        if (cartas.isEmpty()) return carta.getValor() == 13;
        CartaInglesa ultima = cartas.peek();
        return !ultima.getColor().equals(carta.getColor())
                && ultima.getValor() == carta.getValor() + 1;
    }

    public CartaInglesa getUltimaCarta() { return cartas.isEmpty() ? null : cartas.peek(); }

    public List<CartaInglesa> getCards() { return new ArrayList<>(cartas); }

    /** Para Undo: inserta sin validar */
    public void pushBloqueSinValidar(List<CartaInglesa> bloque) {
        for (CartaInglesa c : bloque) cartas.push(c);
    }
}
