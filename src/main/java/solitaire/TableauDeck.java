package solitaire;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;
import java.util.List;

public class TableauDeck {
    private Pila<CartaInglesa> cartas = new Pila<>(104);

    public void inicializar(List<CartaInglesa> cartasIniciales) {
        cartas.clear();
        for (CartaInglesa c : cartasIniciales) cartas.push(c);
        if (!cartas.pila_vacia()) cartas.peek().makeFaceUp();
    }

    public List<CartaInglesa> removeStartingAt(int value) {
        List<CartaInglesa> removed = new ArrayList<>();
        Pila<CartaInglesa> temp = new Pila<>(cartas.getSize());

        while (!cartas.pila_vacia()) {
            CartaInglesa c = cartas.pop();
            if (c.isFaceup() && c.getValor() <= value) {
                removed.add(0, c);
            } else {
                temp.push(c);
            }
        }
        while (!temp.pila_vacia()) cartas.push(temp.pop());
        return removed;
    }

    public CartaInglesa viewCardStartingAt(int value) {
        Pila<CartaInglesa> temp = new Pila<>(cartas.getSize());
        CartaInglesa found = null;

        while (!cartas.pila_vacia()) {
            CartaInglesa c = cartas.pop();
            if (c.isFaceup() && c.getValor() <= value && found == null) {
                found = c;
            }
            temp.push(c);
        }
        while (!temp.pila_vacia()) cartas.push(temp.pop());
        return found;
    }

    public boolean agregarCarta(CartaInglesa carta) {
        if (sePuedeAgregarCarta(carta)) {
            carta.makeFaceUp();
            cartas.push(carta);
            return true;
        }
        return false;
    }

    public CartaInglesa verUltimaCarta() { return cartas.pila_vacia() ? null : cartas.peek(); }

    public CartaInglesa removerUltimaCarta() {
        CartaInglesa ultima = cartas.pila_vacia() ? null : cartas.pop();
        if (!cartas.pila_vacia()) cartas.peek().makeFaceUp();
        return ultima;
    }

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

    public boolean isEmpty() { return cartas.pila_vacia(); }

    public boolean sePuedeAgregarCarta(CartaInglesa carta) {
        if (cartas.pila_vacia()) return carta.getValor() == 13;
        CartaInglesa ultima = cartas.peek();
        return !ultima.getColor().equals(carta.getColor())
                && ultima.getValor() == carta.getValor() + 1;
    }

    public CartaInglesa getUltimaCarta() { return cartas.pila_vacia() ? null : cartas.peek(); }

    public List<CartaInglesa> getCards() {
        List<CartaInglesa> list = new ArrayList<>();
        Pila<CartaInglesa> temp = new Pila<>(cartas.getSize());
        while (!cartas.pila_vacia()) {
            CartaInglesa c = cartas.pop();
            list.add(0, c);
            temp.push(c);
        }
        while (!temp.pila_vacia()) cartas.push(temp.pop());
        return list;
    }

    public void pushBloqueSinValidar(List<CartaInglesa> bloque) {
        for (CartaInglesa c : bloque) cartas.push(c);
    }
}
