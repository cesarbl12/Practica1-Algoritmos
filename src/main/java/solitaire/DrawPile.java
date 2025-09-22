package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Mazo;

import java.util.ArrayList;
import java.util.List;

/**
 * Mazo de robar (DrawPile) implementado con Pila personalizada.
 */
public class DrawPile {
    private Pila<CartaInglesa> cartas = new Pila<>();  // ← AQUÍ está el cambio
    private int cuantasCartasSeEntregan = 3;

    public DrawPile() {
        Mazo mazo = new Mazo();
        List<CartaInglesa> cartasMazo = mazo.getCartas();  // ← También cambié esto
        for (CartaInglesa c : cartasMazo) {
            cartas.push(c);
        }
        setCuantasCartasSeEntregan(3);
    }

    public void setCuantasCartasSeEntregan(int cuantas) {
        this.cuantasCartasSeEntregan = cuantas;
    }

    public int getCuantasCartasSeEntregan() { return cuantasCartasSeEntregan; }

    public List<CartaInglesa> getCartas(int cantidad) {
        List<CartaInglesa> retiradas = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            retiradas.add(cartas.pop());
        }
        return retiradas;
    }

    public List<CartaInglesa> retirarCartas() {
        List<CartaInglesa> retiradas = new ArrayList<>();
        int maximo = Math.min(cartas.size(), cuantasCartasSeEntregan);
        for (int i = 0; i < maximo; i++) {
            CartaInglesa c = cartas.pop();
            c.makeFaceUp();
            retiradas.add(c);
        }
        return retiradas;
    }

    public boolean hayCartas() { return !cartas.isEmpty(); }

    public int size() { return cartas.size(); }

    public CartaInglesa verCarta() { return cartas.isEmpty() ? null : cartas.peek(); }

    public void recargar(List<CartaInglesa> cartasAgregar) {
        cartas.clear();
        for (CartaInglesa c : cartasAgregar) {
            c.makeFaceDown();
            cartas.push(c);
        }
    }

    /** Para Undo: devolver cartas al tope de la pila. */
    public void devolverAlFrente(List<CartaInglesa> devueltas) {
        if (devueltas == null || devueltas.isEmpty()) return;
        for (int i = devueltas.size() - 1; i >= 0; i--) {
            CartaInglesa c = devueltas.get(i);
            c.makeFaceDown();
            cartas.push(c);
        }
    }

    @Override
    public String toString() { return cartas.isEmpty() ? "-E-" : "@"; }
}
