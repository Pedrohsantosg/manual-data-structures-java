package br.com.unidade2.restaurante.datastructures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class MinHeapTest {

    @Test
    void deveExtrairEmOrdemCrescente() {
        MinHeap<Integer> heap = new MinHeap<>();

        heap.insert(7);
        heap.insert(1);
        heap.insert(4);
        heap.insert(2);

        assertEquals(1, heap.extractMin());
        assertEquals(2, heap.extractMin());
        assertEquals(4, heap.extractMin());
        assertEquals(7, heap.extractMin());
    }
}
