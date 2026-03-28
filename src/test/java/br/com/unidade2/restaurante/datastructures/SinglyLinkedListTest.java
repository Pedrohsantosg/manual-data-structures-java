package br.com.unidade2.restaurante.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SinglyLinkedList")
class SinglyLinkedListTest {

    private SinglyLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new SinglyLinkedList<>();
    }

    @Test
    @DisplayName("Nova lista deve estar vazia e ter tamanho 0")
    void newListIsEmpty() {
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }


    @Test
    @DisplayName("addFirst deve inserir na cabeça e incrementar size")
    void addFirstIncreasesSize() {
        list.addFirst(10);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("addFirst deve manter a ordem LIFO na cabeça")
    void addFirstMaintainsLifoOrder() {
        list.addFirst(1);
        list.addFirst(2);
        list.addFirst(3);
        assertEquals(3, list.peekFirst());
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("peekFirst deve retornar o elemento da cabeça sem remover")
    void peekFirstDoesNotRemove() {
        list.addFirst(42);
        assertEquals(42, list.peekFirst());
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("peekFirst em lista vazia deve lançar IllegalStateException")
    void peekFirstOnEmptyListThrows() {
        assertThrows(IllegalStateException.class, () -> list.peekFirst());
    }

    @Test
    @DisplayName("removeFirst deve retornar e remover o elemento da cabeça")
    void removeFirstReturnsHead() {
        list.addFirst(5);
        list.addFirst(10);
        assertEquals(10, list.removeFirst());
        assertEquals(1, list.size());
        assertEquals(5, list.peekFirst());
    }

    @Test
    @DisplayName("removeFirst em lista vazia deve lançar IllegalStateException")
    void removeFirstOnEmptyListThrows() {
        assertThrows(IllegalStateException.class, () -> list.removeFirst());
    }

    @Test
    @DisplayName("removeFirst no único elemento deve deixar a lista vazia")
    void removeFirstLastElementLeavesEmpty() {
        list.addFirst(99);
        list.removeFirst();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("Inserção e remoção sequencial devem manter a consistência")
    void sequentialAddAndRemove() {
        for (int i = 1; i <= 5; i++) {
            list.addFirst(i);
        }
        assertEquals(5, list.size());

        for (int i = 5; i >= 1; i--) {
            assertEquals(i, list.removeFirst());
        }
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("addFirst com null deve funcionar (null é valor válido)")
    void addFirstWithNull() {
        SinglyLinkedList<String> strList = new SinglyLinkedList<>();
        strList.addFirst(null);
        assertEquals(1, strList.size());
        assertNull(strList.peekFirst());
    }
}