package solitaire;

import DeckOfCards.CartaInglesa;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Montón de descarte (Waste) implementado con una pila.
 */
public class WastePile {
    private Stack<CartaInglesa> cartas = new Stack<>();

    public void addCartas(List<CartaInglesa> nuevas) {
        for (CartaInglesa c : nuevas) cartas.push(c);
    }

    public List<CartaInglesa> emptyPile() {
        List<CartaInglesa> pile = new ArrayList<>();
        while (!cartas.isEmpty()) pile.add(0, cartas.pop());
        return pile;
    }

    public CartaInglesa verCarta() { return cartas.isEmpty() ? null : cartas.peek(); }

    public CartaInglesa getCarta() { return cartas.isEmpty() ? null : cartas.pop(); }

    public boolean hayCartas() { return !cartas.isEmpty(); }

    public int size() { return cartas.size(); }

    @Override
    public String toString() {
        if (cartas.isEmpty()) return "---";
        CartaInglesa top = cartas.peek();
        top.makeFaceUp();
        return top.toString();
    }
}
