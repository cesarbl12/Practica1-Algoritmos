package solitaire;

/**
 * Historial de movimientos usando Pila personalizada.
 */
public class MoveHistory {
    private final Pila<Move> history = new Pila<>();

    public void record(Move move) {
        history.push(move);
    }

    public boolean canUndo() {
        return !history.isEmpty();
    }

    public void undo() {
        if (!history.isEmpty()) {
            Move lastMove = history.pop();
            lastMove.undo();
        }
    }

    public void clear() {
        history.clear();
    }
}