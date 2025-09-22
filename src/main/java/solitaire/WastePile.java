package solitaire;

import DeckOfCards.CartaInglesa;

import java.util.ArrayList;
import java.util.List;

/**
 * Montón de descarte (Waste) implementado con Pila personalizada.
 */
public class WastePile {
    private Pila<CartaInglesa> cartas = new Pila<>();

    public void addCartas(List<CartaInglesa> nuevas) {
        for (CartaInglesa c : nuevas) {
            cartas.push(c);
        }
    }

    public List<CartaInglesa> emptyPile() {
        List<CartaInglesa> pile = new ArrayList<>();
        while (!cartas.isEmpty()) {
            pile.add(0, cartas.pop()); // Insertar al inicio para mantener orden inverso
        }
        return pile;
    }

    public CartaInglesa verCarta() {
        return cartas.isEmpty() ? null : cartas.peek();
    }

    public CartaInglesa getCarta() {
        return cartas.isEmpty() ? null : cartas.pop();
    }

    public boolean hayCartas() {
        return !cartas.isEmpty();
    }

    public int size() {
        return cartas.size();
    }

    @Override
    public String toString() {
        if (cartas.isEmpty()) return "---";
        CartaInglesa top = cartas.peek();
        top.makeFaceUp();
        return top.toString();
    }
}