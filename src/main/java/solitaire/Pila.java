package solitaire;

/**
 * Implementación genérica de una Pila (LIFO).
 * @param <T> Tipo de datos que almacena la pila.
 */
@SuppressWarnings("unchecked")
public class Pila<T> {
    private int tope;
    private T[] pila;

    // Constructor: crea una pila con capacidad fija
    public Pila(int size) {
        pila = (T[]) new Object[size];
        this.tope = -1;
    }

    // Apilar un dato
    public void push(T dato) {
        if (pila_llena()) {
            System.out.println("Desbordamiento: la pila está llena");
        } else {
            tope++;
            pila[tope] = dato;
        }
    }

    // Desapilar y devolver el tope
    public T pop() {
        if (pila_vacia()) {
            System.out.println("Subdesbordamiento: la pila está vacía");
            return null;
        } else {
            T dato = pila[tope];
            pila[tope] = null; // limpiar referencia
            tope--;
            return dato;
        }
    }

    // Ver el elemento en el tope sin sacarlo
    public T peek() {
        return pila_vacia() ? null : pila[tope];
    }

    // ¿Está vacía la pila?
    public boolean pila_vacia() {
        return tope == -1;
    }

    /** ¿Está llena la pila? */
    public boolean pila_llena() {
        return tope == pila.length - 1;
    }

    //Tamaño actual de la pila
    public int size() {
        return tope + 1;
    }

    public int getSize() {
        return tope + 1;
    }

    //Capacidad máxima de la pila
    public int capacity() {
        return pila.length;
    }

    //Vaciar completamente la pila
    public void clear() {
        while (!pila_vacia()) {
            pop();
        }
    }

    @Override
    public String toString() {
        if (pila_vacia()) return "[VACÍA]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i <= tope; i++) {
            sb.append(pila[i]);
            if (i < tope) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    public static String invierteCadena(String cadena) {
        Pila<Character> pila = new Pila<>(cadena.length());
        for (int i = 0; i < cadena.length(); i++) {
            pila.push(cadena.charAt(i));
        }
        StringBuilder invertida = new StringBuilder();
        while (!pila.pila_vacia()) {
            invertida.append(pila.pop());
        }
        return invertida.toString();
    }

    public static boolean revisarSintaxis(String cadena) {
        Pila<Character> pila = new Pila<>(cadena.length());
        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            if (c == '(' || c == '{' || c == '[') {
                pila.push(c);
            } else if (c == ')' || c == '}' || c == ']') {
                if (pila.pila_vacia()) return false;
                char abierto = pila.pop();
                if ((c == ')' && abierto != '(') ||
                        (c == '}' && abierto != '{') ||
                        (c == ']' && abierto != '[')) {
                    return false;
                }
            }
        }
        return pila.pila_vacia();
    }

    public static int sumarPila(Pila<Integer> pila) {
        if (pila.pila_vacia()) {
            return 0;
        } else {
            int elemento = pila.pop();
            int sumaRestante = sumarPila(pila);
            return elemento + sumaRestante;
        }
    }
}
