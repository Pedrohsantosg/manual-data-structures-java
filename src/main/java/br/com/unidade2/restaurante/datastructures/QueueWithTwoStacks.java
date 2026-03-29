package br.com.unidade2.restaurante.datastructures;
/**
 * Implementação de uma Fila utilizando duas pilhas internas (LinkedStack).
 * @author Pedro
 * @since 20/03/2026
 */

public class QueueWithTwoStacks<T> {

    private final LinkedStack<T> inputStack = new LinkedStack<>();
    private final LinkedStack<T> outputStack = new LinkedStack<>();

    public void enqueue(T value) {
        inputStack.push(value);
    }

    public T dequeue() {
        shiftIfNeeded();
        if (outputStack.isEmpty()) {
            throw new IllegalStateException("Fila vazia");
        }
        return outputStack.pop();
    }

    public T peek() {
        shiftIfNeeded();
        if (outputStack.isEmpty()) {
            throw new IllegalStateException("Fila vazia");
        }
        return outputStack.peek();
    }

    public boolean isEmpty() {
        return inputStack.isEmpty() && outputStack.isEmpty();
    }

    public int size() {
        return inputStack.size() + outputStack.size();
    }

    private void shiftIfNeeded() {
        if (outputStack.isEmpty()) {
            while (!inputStack.isEmpty()) {
                outputStack.push(inputStack.pop());
            }
        }
    }
}
