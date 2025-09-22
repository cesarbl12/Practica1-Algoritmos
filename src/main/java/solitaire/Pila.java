package solitaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación genérica de una Pila (LIFO) mejorada.
 * @param <T> Tipo de datos que almacena la pila.
 */
@SuppressWarnings("unchecked")
public class Pila<T> {
    private int tope;
    private T[] pila;
    private int capacidad;

    // Constructor: crea una pila con capacidad fija
    public Pila(int size) {
        pila = (T[]) new Object[size];
        this.tope = -1;
        this.capacidad = size;
    }

    // Constructor por defecto con capacidad de 1000
    public Pila() {
        this(1000);
    }

    // Apilar un dato
    public void push(T dato) {
        if (pila_llena()) {
            // Expandir la pila si está llena
            expandir();
        }
        tope++;
        pila[tope] = dato;
    }

    // Expandir capacidad de la pila
    private void expandir() {
        int nuevaCapacidad = capacidad * 2;
        T[] nuevaPila = (T[]) new Object[nuevaCapacidad];
        for (int i = 0; i <= tope; i++) {
            nuevaPila[i] = pila[i];
        }
        pila = nuevaPila;
        capacidad = nuevaCapacidad;
    }

    // Desapilar y devolver el tope
    public T pop() {
        if (pila_vacia()) {
            return null;
        } else {
            T dato = pila[tope];
            pila[tope] = null; // limpiar referencia
            tope--;
            return dato;
        }
    }

    // Ver el tope sin eliminarlo
    public T peek() {
        return pila_vacia() ? null : pila[tope];
    }

    // Ver un elemento en posición i (0 = fondo, tope = último)
    public T peekAt(int index) {
        if (index < 0 || index > tope) return null;
        return pila[index];
    }

    // ¿Está vacía?
    public boolean pila_vacia() {
        return tope == -1;
    }

    public boolean isEmpty() {
        return pila_vacia();
    }

    // ¿Está llena?
    public boolean pila_llena() {
        return tope == capacidad - 1;
    }

    // Tamaño actual
    public int size() {
        return tope + 1;
    }

    // Vaciar pila
    public void clear() {
        for (int i = 0; i <= tope; i++) {
            pila[i] = null;
        }
        tope = -1;
    }

    // Convertir a lista inmutable (orden: fondo a tope)
    public List<T> toList() {
        List<T> lista = new ArrayList<>();
        for (int i = 0; i <= tope; i++) {
            lista.add(pila[i]);
        }
        return lista;
    }

    // Crear un iterador simple para recorrer desde el fondo
    public PilaIterator<T> iterator() {
        return new PilaIterator<>(this);
    }

    // Buscar elemento y devolver índice (-1 si no se encuentra)
    public int indexOf(T elemento) {
        for (int i = 0; i <= tope; i++) {
            if (pila[i] != null && pila[i].equals(elemento)) {
                return i;
            }
        }
        return -1;
    }

    // Remover elementos desde una posición específica hacia arriba
    public List<T> removeFrom(int fromIndex) {
        List<T> removed = new ArrayList<>();
        if (fromIndex >= 0 && fromIndex <= tope) {
            for (int i = fromIndex; i <= tope; i++) {
                removed.add(pila[i]);
            }
            // Limpiar referencias removidas
            for (int i = fromIndex; i <= tope; i++) {
                pila[i] = null;
            }
            tope = fromIndex - 1;
        }
        return removed;
    }

    // Insertar múltiples elementos sin validación (para undo)
    public void pushAll(List<T> elementos) {
        for (T elemento : elementos) {
            push(elemento);
        }
    }

    // Remover múltiples elementos del tope
    public List<T> popMultiple(int count) {
        List<T> result = new ArrayList<>();
        for (int i = 0; i < count && !isEmpty(); i++) {
            result.add(0, pop()); // Insertar al inicio para mantener orden
        }
        return result;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i <= tope; i++) {
            sb.append(pila[i]);
            if (i < tope) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // Clase interna para el iterador
    public static class PilaIterator<T> {
        private final Pila<T> pila;
        private int currentIndex;
        private boolean canRemove;

        public PilaIterator(Pila<T> pila) {
            this.pila = pila;
            this.currentIndex = 0;
            this.canRemove = false;
        }

        public boolean hasNext() {
            return currentIndex <= pila.tope;
        }

        public T next() {
            if (!hasNext()) return null;
            T element = pila.pila[currentIndex];
            currentIndex++;
            canRemove = true;
            return element;
        }

        public void remove() {
            if (!canRemove) return;

            // Mover elementos hacia atrás para llenar el hueco
            int removeIndex = currentIndex - 1;
            for (int i = removeIndex; i < pila.tope; i++) {
                pila.pila[i] = pila.pila[i + 1];
            }
            pila.pila[pila.tope] = null;
            pila.tope--;
            currentIndex--;
            canRemove = false;
        }
    }
}