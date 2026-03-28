package br.com.unidade2.restaurante.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("QueueWithTwoStacks")
class QueueWithTwoStacksTest {

    private QueueWithTwoStacks<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new QueueWithTwoStacks<>();
    }

    @Test
    @DisplayName("Nova fila deve estar vazia e ter tamanho 0")
    void newQueueIsEmpty() {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    @DisplayName("enqueue deve aumentar o tamanho da fila")
    void enqueueIncreasesSize() {
        queue.enqueue(1);
        queue.enqueue(2);
        assertEquals(2, queue.size());
        assertFalse(queue.isEmpty());
    }

    @Test
    @DisplayName("peek deve retornar o primeiro elemento sem removê-lo (FIFO)")
    void peekReturnsFrontWithoutRemoving() {
        queue.enqueue(10);
        queue.enqueue(20);
        assertEquals(10, queue.peek());
        assertEquals(2, queue.size());
    }

    @Test
    @DisplayName("peek em fila vazia deve lançar IllegalStateException")
    void peekOnEmptyQueueThrows() {
        assertThrows(IllegalStateException.class, () -> queue.peek());
    }

    @Test
    @DisplayName("dequeue deve retornar elementos na ordem FIFO")
    void dequeueFollowsFifoOrder() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        assertEquals(1, queue.dequeue());
        assertEquals(2, queue.dequeue());
        assertEquals(3, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("dequeue em fila vazia deve lançar IllegalStateException")
    void dequeueOnEmptyQueueThrows() {
        assertThrows(IllegalStateException.class, () -> queue.dequeue());
    }

    @Test
    @DisplayName("dequeue no único elemento deve deixar a fila vazia")
    void dequeueLastElementLeavesEmpty() {
        queue.enqueue(42);
        assertEquals(42, queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    @DisplayName("Operações intercaladas devem preservar a semântica FIFO")
    void interleavedEnqueueDequeue() {
        queue.enqueue(1);
        queue.enqueue(2);
        assertEquals(1, queue.dequeue());

        queue.enqueue(3);
        assertEquals(2, queue.dequeue());
        assertEquals(3, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("Múltiplos ciclos de enqueue/dequeue devem funcionar corretamente")
    void multipleCyclesAreCorrect() {
        // Primeiro ciclo
        queue.enqueue(10);
        queue.enqueue(20);
        assertEquals(10, queue.dequeue());
        assertEquals(20, queue.dequeue());

        // Segundo ciclo — verifica que as pilhas internas foram esvaziadas corretamente
        queue.enqueue(30);
        queue.enqueue(40);
        assertEquals(30, queue.dequeue());
        assertEquals(40, queue.dequeue());

        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("Fila deve suportar grande volume de operações com ordem correta")
    void largeVolumeMaintainsFifoOrder() {
        int n = 500;
        for (int i = 0; i < n; i++) {
            queue.enqueue(i);
        }
        assertEquals(n, queue.size());
        for (int i = 0; i < n; i++) {
            assertEquals(i, queue.dequeue());
        }
        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("size deve contabilizar elementos das duas pilhas internas")
    void sizeReflectsBothInternalStacks() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.dequeue();          // move 1,2,3 para outputStack; remove 1
        queue.enqueue(4);         // vai para inputStack
        // outputStack: [2, 3], inputStack: [4] → size == 3
        assertEquals(3, queue.size());
    }
}