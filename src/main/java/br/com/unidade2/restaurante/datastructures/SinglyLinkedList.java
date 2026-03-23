package br.com.unidade2.restaurante.datastructures;

public class SinglyLinkedList<T> {

    private static class Node<T> {
        private final T value;
        private Node<T> next;

        private Node(T value) {
            this.value = value;
        }
    }

    private Node<T> head;
    private int size;

    public void addFirst(T value) {
        Node<T> newNode = new Node<>(value);
        newNode.next = head;
        head = newNode;
        size++;
    }

    public T removeFirst() {
        if (head == null) {
            throw new IllegalStateException("Lista vazia");
        }

        T value = head.value;
        head = head.next;
        size--;
        return value;
    }

    public T peekFirst() {
        if (head == null) {
            throw new IllegalStateException("Lista vazia");
        }
        return head.value;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
