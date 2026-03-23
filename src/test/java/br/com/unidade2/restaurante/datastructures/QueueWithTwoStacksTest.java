package br.com.unidade2.restaurante.datastructures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class QueueWithTwoStacksTest {

    @Test
    void deveRespeitarOrdemFIFO() {
        QueueWithTwoStacks<Integer> queue = new QueueWithTwoStacks<>();

        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);

        assertEquals(10, queue.dequeue());
        assertEquals(20, queue.dequeue());
        assertEquals(30, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    void deveFalharAoRemoverDeFilaVazia() {
        QueueWithTwoStacks<Integer> queue = new QueueWithTwoStacks<>();
        try {
            queue.dequeue();
            fail("Era esperado IllegalStateException");
        } catch (IllegalStateException exception) {
            assertEquals("Fila vazia", exception.getMessage());
        }
    }
}
