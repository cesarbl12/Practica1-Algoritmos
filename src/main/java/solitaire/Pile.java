package solitaire;

import java.util.*;

public interface Pile {
    void push(DeckOfCards.CartaInglesa card);
    DeckOfCards.CartaInglesa pop();
    DeckOfCards.CartaInglesa peek();
    boolean isEmpty();
    int size();
    /** Vista inmutable del contenido (tope = último elemento de la lista). */
    List<DeckOfCards.CartaInglesa> asListView();
}
