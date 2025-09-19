package solitaire;


 //Historial de movimientos implementado con la pila personalizada.
public class MoveHistory {
    private final Pila<Move> history;

    public MoveHistory() {
        // Asignamos una capacidad inicial amplia, puedes ajustar si lo deseas
        history = new Pila<>(500);
    }

    // Registrar un movimiento en el historial
    public void record(Move move) {
        history.push(move);
    }

    // ¿Se puede deshacer un movimiento?
    public boolean canUndo() {
        return !history.pila_vacia();
    }

    // Deshacer el ultimo movimiento registrado
    public void undo() {
        if (!history.pila_vacia()) {
            Move last = history.pop();
            if (last != null) {
                last.undo();
            }
        }
    }

    // Vaciar el historial
    public void clear() {
        while (!history.pila_vacia()) {
            history.pop();
        }
    }
}
