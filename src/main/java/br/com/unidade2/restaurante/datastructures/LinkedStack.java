package br.com.unidade2.restaurante.datastructures;
/**
 * Implementação de uma Pilha (Stack) utilizando uma lista encadeada interna.
 * Por utilizar uma lista encadeada, a pilha possui
 * dimensionamento dinâmico
 * @author Pedro
 * @since 20/03/2026
 */

public class LinkedStack<T> {

    private final SinglyLinkedList<T> list = new SinglyLinkedList<>();

    public void push(T value) {
        list.addFirst(value);
    }

    public T pop() {
        return list.removeFirst();
    }

    public T peek() {
        return list.peekFirst();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }
}
