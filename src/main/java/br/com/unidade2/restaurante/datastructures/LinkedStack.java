package br.com.unidade2.restaurante.datastructures;

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
