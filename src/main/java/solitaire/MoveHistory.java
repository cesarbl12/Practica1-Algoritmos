package solitaire;

import java.util.ArrayDeque;
import java.util.Deque;

public class MoveHistory {
    private final Deque<Move> history = new ArrayDeque<>();

    public void record(Move move) { history.push(move); }
    public boolean canUndo() { return !history.isEmpty(); }
    public void undo() { if (!history.isEmpty()) history.pop().undo(); }
    public void clear() { history.clear(); }
}
