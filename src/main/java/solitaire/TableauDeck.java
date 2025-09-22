package solitaire;

import DeckOfCards.CartaInglesa;

import java.util.ArrayList;
import java.util.List;

/**
 * Tableau (montón de juego) implementado con Pila personalizada.
 */
public class TableauDeck {
    private Pila<CartaInglesa> cartas = new Pila<>();

    public void inicializar(List<CartaInglesa> cartasIniciales) {
        cartas.clear();
        for (CartaInglesa c : cartasIniciales) {
            cartas.push(c);
        }
        if (!cartas.isEmpty()) {
            cartas.peek().makeFaceUp();
        }
    }

    public List<CartaInglesa> removeStartingAt(int value) {
        List<CartaInglesa> removed = new ArrayList<>();

        // Encontrar el índice de la primera carta que cumple la condición
        int startIndex = -1;
        List<CartaInglesa> todasLasCartas = cartas.toList();

        for (int i = 0; i < todasLasCartas.size(); i++) {
            CartaInglesa carta = todasLasCartas.get(i);
            if (carta.isFaceup() && carta.getValor() <= value) {
                startIndex = i;
                break;
            }
        }

        // Si encontramos una carta válida, remover desde esa posición
        if (startIndex != -1) {
            removed = cartas.removeFrom(startIndex);
        }

        return removed;
    }

    public CartaInglesa viewCardStartingAt(int value) {
        List<CartaInglesa> todasLasCartas = cartas.toList();
        for (CartaInglesa c : todasLasCartas) {
            if (c.isFaceup() && c.getValor() <= value) {
                return c;
            }
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

    public CartaInglesa verUltimaCarta() {
        return cartas.isEmpty() ? null : cartas.peek();
    }

    public CartaInglesa removerUltimaCarta() {
        CartaInglesa ultima = cartas.isEmpty() ? null : cartas.pop();
        if (!cartas.isEmpty()) {
            cartas.peek().makeFaceUp();
        }
        return ultima;
    }

    @Override
    public String toString() {
        if (cartas.isEmpty()) return "---";
        StringBuilder b = new StringBuilder();
        List<CartaInglesa> todasCartas = cartas.toList();
        for (CartaInglesa c : todasCartas) {
            b.append(c.toString());
        }
        return b.toString();
    }

    public boolean agregarBloqueDeCartas(List<CartaInglesa> cartasRecibidas) {
        if (!cartasRecibidas.isEmpty()) {
            CartaInglesa primera = cartasRecibidas.get(0);
            if (sePuedeAgregarCarta(primera)) {
                for (CartaInglesa c : cartasRecibidas) {
                    cartas.push(c);
                }
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return cartas.isEmpty();
    }

    public boolean sePuedeAgregarCarta(CartaInglesa carta) {
        if (cartas.isEmpty()) {
            return carta.getValor() == 13;
        }
        CartaInglesa ultima = cartas.peek();
        return !ultima.getColor().equals(carta.getColor())
                && ultima.getValor() == carta.getValor() + 1;
    }

    public CartaInglesa getUltimaCarta() {
        return cartas.isEmpty() ? null : cartas.peek();
    }

    public List<CartaInglesa> getCards() {
        return cartas.toList();
    }

    /** Para Undo: inserta sin validar */
    public void pushBloqueSinValidar(List<CartaInglesa> bloque) {
        cartas.pushAll(bloque);
    }
}