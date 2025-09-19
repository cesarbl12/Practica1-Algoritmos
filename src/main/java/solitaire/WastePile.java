package solitaire;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;
import java.util.List;

public class WastePile {
    private Pila<CartaInglesa> cartas = new Pila<>(104); // tamaño suficiente

    public void addCartas(List<CartaInglesa> nuevas) {
        for (CartaInglesa c : nuevas) cartas.push(c);
    }

    public List<CartaInglesa> emptyPile() {
        List<CartaInglesa> pile = new ArrayList<>();
        while (!cartas.pila_vacia()) pile.add(0, cartas.pop());
        return pile;
    }

    public CartaInglesa verCarta() { return cartas.pila_vacia() ? null : cartas.peek(); }

    public CartaInglesa getCarta() { return cartas.pila_vacia() ? null : cartas.pop(); }

    public boolean hayCartas() { return !cartas.pila_vacia(); }

    public int size() { return cartas.getSize(); }

    @Override
    public String toString() {
        if (cartas.pila_vacia()) return "---";
        CartaInglesa top = cartas.peek();
        top.makeFaceUp();
        return top.toString();
    }
}
