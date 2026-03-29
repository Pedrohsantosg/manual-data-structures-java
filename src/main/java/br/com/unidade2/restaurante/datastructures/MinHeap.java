package br.com.unidade2.restaurante.datastructures;

import java.util.Arrays;

/**
 * Implementação de um Min-Heap utilizando um array dinâmico.
 * o menor elemento está sempre na raiz , facilitando
 * o acesso rápido ao item de maior prioridade.
 * @author Pedro, Beatriz, Julio e Livia
 * @since 20/03/2026
 */
public class MinHeap<T extends Comparable<T>> {

    private Object[] elements = new Object[16];
    private int size;

    public void insert(T value) {
        ensureCapacity();
        elements[size] = value;
        siftUp(size);
        size++;
    }

    public T peek() {
        if (size == 0) {
            throw new IllegalStateException("Heap vazia");
        }
        return elementAt(0);
    }

    public T extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap vazia");
        }

        T min = elementAt(0);
        size--;
        elements[0] = elements[size];
        elements[size] = null;
        if (size > 0) {
            siftDown(0);
        }
        return min;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void siftUp(int index) {
        int current = index;
        while (current > 0) {
            int parent = (current - 1) / 2;
            if (elementAt(current).compareTo(elementAt(parent)) >= 0) {
                break;
            }
            swap(current, parent);
            current = parent;
        }
    }

    private void siftDown(int index) {
        int current = index;
        while (true) {
            int left = 2 * current + 1;
            int right = 2 * current + 2;
            int smallest = current;

            if (left < size && elementAt(left).compareTo(elementAt(smallest)) < 0) {
                smallest = left;
            }
            if (right < size && elementAt(right).compareTo(elementAt(smallest)) < 0) {
                smallest = right;
            }
            if (smallest == current) {
                break;
            }

            swap(current, smallest);
            current = smallest;
        }
    }

    @SuppressWarnings("unchecked")
    private T elementAt(int index) {
        return (T) elements[index];
    }

    private void swap(int i, int j) {
        Object temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
    }
}
