package br.com.unidade2.restaurante.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LinkedStack")
class LinkedStackTest {

    private LinkedStack<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new LinkedStack<>();
    }

    @Test
    @DisplayName("Nova pilha deve estar vazia e ter tamanho 0")
    void newStackIsEmpty() {
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("push deve inserir elementos e incrementar size")
    void pushIncreasesSize() {
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.size());
        assertFalse(stack.isEmpty());
    }

    @Test
    @DisplayName("peek deve retornar o topo sem remover")
    void peekDoesNotRemove() {
        stack.push(7);
        stack.push(14);
        assertEquals(14, stack.peek());
        assertEquals(2, stack.size());
    }

    @Test
    @DisplayName("peek em pilha vazia deve lançar IllegalStateException")
    void peekOnEmptyStackThrows() {
        assertThrows(IllegalStateException.class, () -> stack.peek());
    }

    @Test
    @DisplayName("pop deve retornar o topo e decrementar size (LIFO)")
    void popReturnsTopAndDecrements() {
        stack.push(100);
        stack.push(200);
        stack.push(300);

        assertEquals(300, stack.pop());
        assertEquals(200, stack.pop());
        assertEquals(100, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    @DisplayName("pop em pilha vazia deve lançar IllegalStateException")
    void popOnEmptyStackThrows() {
        assertThrows(IllegalStateException.class, () -> stack.pop());
    }

    @Test
    @DisplayName("Pilha deve obedecer estritamente a ordem LIFO")
    void lifoOrder() {
        int[] values = {5, 10, 15, 20, 25};
        for (int v : values) {
            stack.push(v);
        }
        for (int i = values.length - 1; i >= 0; i--) {
            assertEquals(values[i], stack.pop());
        }
    }

    @Test
    @DisplayName("push e pop intercalados devem manter a consistência")
    void interleavedPushPop() {
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.pop());
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(1, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    @DisplayName("Pilha deve suportar grande volume de operações")
    void largeVolume() {
        int n = 1_000;
        for (int i = 0; i < n; i++) {
            stack.push(i);
        }
        assertEquals(n, stack.size());
        for (int i = n - 1; i >= 0; i--) {
            assertEquals(i, stack.pop());
        }
        assertTrue(stack.isEmpty());
    }
}
