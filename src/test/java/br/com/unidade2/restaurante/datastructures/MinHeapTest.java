package br.com.unidade2.restaurante.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MinHeap")
class MinHeapTest {

    private MinHeap<Integer> heap;

    @BeforeEach
    void setUp() {
        heap = new MinHeap<>();
    }

    @Test
    @DisplayName("Heap nova deve estar vazia")
    void newHeapIsEmpty() {
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
    }

    @Test
    @DisplayName("insert deve aumentar o tamanho")
    void insertIncreasesSize() {
        heap.insert(10);
        heap.insert(5);
        assertEquals(2, heap.size());
        assertFalse(heap.isEmpty());
    }

    @Test
    @DisplayName("peek deve retornar o menor elemento sem removê-lo")
    void peekReturnsMininumWithoutRemoving() {
        heap.insert(30);
        heap.insert(10);
        heap.insert(20);
        assertEquals(10, heap.peek());
        assertEquals(3, heap.size());
    }

    @Test
    @DisplayName("peek em heap vazia deve lançar IllegalStateException")
    void peekOnEmptyHeapThrows() {
        assertThrows(IllegalStateException.class, () -> heap.peek());
    }

    @Test
    @DisplayName("extractMin deve retornar e remover o menor elemento")
    void extractMinReturnsSmallerst() {
        heap.insert(5);
        heap.insert(1);
        heap.insert(3);

        assertEquals(1, heap.extractMin());
        assertEquals(2, heap.size());
    }

    @Test
    @DisplayName("extractMin em heap vazia deve lançar IllegalStateException")
    void extractMinOnEmptyHeapThrows() {
        assertThrows(IllegalStateException.class, () -> heap.extractMin());
    }

    @Test
    @DisplayName("extractMin repetido deve retornar elementos em ordem crescente (heap sort)")
    void extractMinProducesSortedOrder() {
        int[] values = {7, 2, 9, 1, 5, 3, 8, 4, 6};
        for (int v : values) {
            heap.insert(v);
        }

        List<Integer> sorted = new ArrayList<>();
        while (!heap.isEmpty()) {
            sorted.add(heap.extractMin());
        }

        for (int i = 1; i < sorted.size(); i++) {
            assertTrue(sorted.get(i - 1) <= sorted.get(i),
                    "Ordem errada: " + sorted.get(i - 1) + " > " + sorted.get(i));
        }
        assertEquals(values.length, sorted.size());
    }

    @Test
    @DisplayName("peek após cada insert deve sempre retornar o mínimo global")
    void peekAlwaysReturnsGlobalMinimum() {
        heap.insert(50);
        assertEquals(50, heap.peek());

        heap.insert(30);
        assertEquals(30, heap.peek());

        heap.insert(10);
        assertEquals(10, heap.peek());

        heap.insert(20);
        assertEquals(10, heap.peek()); // 10 ainda é o mínimo
    }

    @Test
    @DisplayName("Heap deve crescer além da capacidade inicial de 16")
    void heapGrowsBeyondInitialCapacity() {
        int n = 32; // > capacidade inicial 16
        for (int i = n; i >= 1; i--) {
            heap.insert(i);
        }
        assertEquals(n, heap.size());
        assertEquals(1, heap.extractMin());
    }

    @Test
    @DisplayName("Duplicatas devem ser aceitas e extraídas corretamente")
    void duplicateValuesAreHandled() {
        heap.insert(5);
        heap.insert(5);
        heap.insert(5);
        assertEquals(3, heap.size());
        assertEquals(5, heap.extractMin());
        assertEquals(5, heap.extractMin());
        assertEquals(5, heap.extractMin());
        assertTrue(heap.isEmpty());
    }

    @Test
    @DisplayName("Heap deve manter propriedade min-heap com grande volume")
    void largeVolumeMaintsMinHeapProperty() {
        int n = 1_000;
        for (int i = n; i >= 1; i--) {
            heap.insert(i);
        }

        int previous = Integer.MIN_VALUE;
        while (!heap.isEmpty()) {
            int current = heap.extractMin();
            assertTrue(current >= previous, "Ordem violada: " + current + " < " + previous);
            previous = current;
        }
    }

    @Test
    @DisplayName("MinHeap deve ser utilizável como heapsort estável crescente")
    void heapSortAscending() {
        int[] input  = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
        int[] expected = input.clone();
        Arrays.sort(expected);

        for (int v : input) heap.insert(v);

        int[] result = new int[input.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = heap.extractMin();
        }

        assertArrayEquals(expected, result);
    }
}
