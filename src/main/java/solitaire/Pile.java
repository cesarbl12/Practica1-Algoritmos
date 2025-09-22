package solitaire;

import java.util.List;

/**
 * Interface Pile actualizada para trabajar con nuestra clase Pila personalizada.
 */
public interface Pile {
    void push(DeckOfCards.CartaInglesa card);
    DeckOfCards.CartaInglesa pop();
    DeckOfCards.CartaInglesa peek();
    boolean isEmpty();
    int size();
    /** Vista inmutable del contenido (tope = último elemento de la lista). */
    List<DeckOfCards.CartaInglesa> asListView();
}

/**
 * Implementación concreta del interface Pile usando nuestra Pila personalizada.
 */
class PileImpl implements Pile {
    private Pila<DeckOfCards.CartaInglesa> pila = new Pila<>();

    @Override
    public void push(DeckOfCards.CartaInglesa card) {
        pila.push(card);
    }

    @Override
    public DeckOfCards.CartaInglesa pop() {
        return pila.pop();
    }

    @Override
    public DeckOfCards.CartaInglesa peek() {
        return pila.peek();
    }

    @Override
    public boolean isEmpty() {
        return pila.isEmpty();
    }

    @Override
    public int size() {
        return pila.size();
    }

    @Override
    public List<DeckOfCards.CartaInglesa> asListView() {
        return pila.toList();
    }
}