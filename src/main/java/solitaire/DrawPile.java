package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Mazo;
import java.util.ArrayList;
import java.util.List;

public class DrawPile {
    private Pila<CartaInglesa> cartas = new Pila<>(104);
    private int cuantasCartasSeEntregan = 3;

    public DrawPile() {
        Mazo mazo = new Mazo();
        for (CartaInglesa c : mazo.getCartas()) cartas.push(c);
        setCuantasCartasSeEntregan(3);
    }

    public void setCuantasCartasSeEntregan(int cuantas) {
        this.cuantasCartasSeEntregan = cuantas;
    }

    public int getCuantasCartasSeEntregan() { return cuantasCartasSeEntregan; }

    public List<CartaInglesa> getCartas(int cantidad) {
        List<CartaInglesa> retiradas = new ArrayList<>();
        for (int i = 0; i < cantidad && !cartas.pila_vacia(); i++) {
            retiradas.add(cartas.pop());
        }
        return retiradas;
    }

    public List<CartaInglesa> retirarCartas() {
        List<CartaInglesa> retiradas = new ArrayList<>();
        int maximo = Math.min(cartas.getSize(), cuantasCartasSeEntregan);
        for (int i = 0; i < maximo; i++) {
            CartaInglesa c = cartas.pop();
            c.makeFaceUp();
            retiradas.add(c);
        }
        return retiradas;
    }

    public boolean hayCartas() { return !cartas.pila_vacia(); }

    public int size() { return cartas.getSize(); }

    public CartaInglesa verCarta() { return cartas.pila_vacia() ? null : cartas.peek(); }

    public void recargar(List<CartaInglesa> cartasAgregar) {
        cartas.clear();
        for (CartaInglesa c : cartasAgregar) {
            c.makeFaceDown();
            cartas.push(c);
        }
    }

    public void devolverAlFrente(List<CartaInglesa> devueltas) {
        if (devueltas == null || devueltas.isEmpty()) return;
        for (int i = devueltas.size() - 1; i >= 0; i--) {
            CartaInglesa c = devueltas.get(i);
            c.makeFaceDown();
            cartas.push(c);
        }
    }

    @Override
    public String toString() { return cartas.pila_vacia() ? "-E-" : "@"; }
}
