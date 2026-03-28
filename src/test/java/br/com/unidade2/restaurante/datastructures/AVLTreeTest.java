package br.com.unidade2.restaurante.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AVLTree")
class AVLTreeTest {

    private AVLTree<Integer, String> tree;

    @BeforeEach
    void setUp() {
        tree = new AVLTree<>();
    }

    @Test
    @DisplayName("Árvore nova deve estar vazia")
    void newTreeIsEmpty() {
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
    }

    @Test
    @DisplayName("put deve inserir e get deve recuperar o valor correto")
    void putAndGet() {
        tree.put(10, "dez");
        assertEquals("dez", tree.get(10));
        assertEquals(1, tree.size());
    }

    @Test
    @DisplayName("put com chave existente deve atualizar o valor sem aumentar size")
    void putDuplicateKeyUpdatesValue() {
        tree.put(5, "cinco");
        tree.put(5, "CINCO");
        assertEquals("CINCO", tree.get(5));
        assertEquals(1, tree.size());
    }

    @Test
    @DisplayName("get para chave inexistente deve retornar null")
    void getMissingKeyReturnsNull() {
        assertNull(tree.get(999));
    }

    @Test
    @DisplayName("put com chave nula deve lançar IllegalArgumentException")
    void putNullKeyThrows() {
        assertThrows(IllegalArgumentException.class, () -> tree.put(null, "valor"));
    }

    @Test
    @DisplayName("containsKey deve retornar true para chave existente")
    void containsKeyPresent() {
        tree.put(7, "sete");
        assertTrue(tree.containsKey(7));
    }

    @Test
    @DisplayName("containsKey deve retornar false para chave inexistente")
    void containsKeyAbsent() {
        assertFalse(tree.containsKey(7));
    }

    @Test
    @DisplayName("remove deve retornar o valor e decrementar size")
    void removeExistingKey() {
        tree.put(3, "três");
        tree.put(1, "um");
        tree.put(5, "cinco");

        String removed = tree.remove(3);
        assertEquals("três", removed);
        assertEquals(2, tree.size());
        assertFalse(tree.containsKey(3));
    }

    @Test
    @DisplayName("remove de chave inexistente deve retornar null sem alterar size")
    void removeMissingKeyReturnsNull() {
        tree.put(1, "um");
        assertNull(tree.remove(99));
        assertEquals(1, tree.size());
    }

    @Test
    @DisplayName("remove do único elemento deve deixar a árvore vazia")
    void removeLastElementLeavesEmpty() {
        tree.put(1, "um");
        tree.remove(1);
        assertTrue(tree.isEmpty());
    }

    @Test
    @DisplayName("remove de nó folha deve manter estrutura válida")
    void removeLeafNode() {
        tree.put(10, "dez");
        tree.put(5, "cinco");
        tree.put(15, "quinze");

        tree.remove(5);
        assertFalse(tree.containsKey(5));
        assertTrue(tree.containsKey(10));
        assertTrue(tree.containsKey(15));
    }

    @Test
    @DisplayName("remove de nó com dois filhos deve manter estrutura válida")
    void removeNodeWithTwoChildren() {
        tree.put(10, "dez");
        tree.put(5, "cinco");
        tree.put(15, "quinze");
        tree.put(3, "três");
        tree.put(7, "sete");

        tree.remove(5);   // nó com dois filhos (3 e 7)
        assertFalse(tree.containsKey(5));
        assertTrue(tree.containsKey(3));
        assertTrue(tree.containsKey(7));
        assertTrue(tree.containsKey(10));
    }

    @Test
    @DisplayName("Inserção em ordem crescente deve manter altura O(log n)")
    void insertAscendingMaintainsLogHeight() {
        int n = 10;
        for (int i = 1; i <= n; i++) {
            tree.put(i, "v" + i);
        }
        assertTrue(tree.height() <= 2 * (int)(Math.log(n + 1) / Math.log(2)) + 1,
                "Altura esperada O(log n), obtida: " + tree.height());
    }

    @Test
    @DisplayName("Inserção em ordem decrescente deve manter altura O(log n)")
    void insertDescendingMaintainsLogHeight() {
        int n = 10;
        for (int i = n; i >= 1; i--) {
            tree.put(i, "v" + i);
        }
        assertTrue(tree.height() <= 2 * (int)(Math.log(n + 1) / Math.log(2)) + 1,
                "Altura esperada O(log n), obtida: " + tree.height());
    }

    @Test
    @DisplayName("inOrderTraversal deve visitar chaves em ordem crescente")
    void inOrderProducesAscendingKeys() {
        int[] keys = {5, 3, 7, 1, 4, 6, 8};
        for (int k : keys) {
            tree.put(k, "v" + k);
        }

        List<Integer> visited = new ArrayList<>();
        tree.inOrderTraversal((k, v) -> visited.add(k));

        for (int i = 1; i < visited.size(); i++) {
            assertTrue(visited.get(i - 1) < visited.get(i),
                    "Ordem incorreta entre " + visited.get(i - 1) + " e " + visited.get(i));
        }
        assertEquals(keys.length, visited.size());
    }

    @Test
    @DisplayName("inOrderTraversal com visitor nulo deve lançar IllegalArgumentException")
    void inOrderNullVisitorThrows() {
        tree.put(1, "um");
        assertThrows(IllegalArgumentException.class, () -> tree.inOrderTraversal(null));
    }

    @Test
    @DisplayName("inOrderTraversal em árvore vazia não deve invocar o visitor")
    void inOrderOnEmptyTreeNeverCallsVisitor() {
        List<Integer> visited = new ArrayList<>();
        tree.inOrderTraversal((k, v) -> visited.add(k));
        assertTrue(visited.isEmpty());
    }

    @Test
    @DisplayName("Deve suportar inserção, busca e remoção de grande volume de dados")
    void largeVolumeOperations() {
        int n = 200;
        for (int i = 0; i < n; i++) {
            tree.put(i, "val" + i);
        }
        assertEquals(n, tree.size());

        for (int i = 0; i < n; i += 2) {
            tree.remove(i);
        }
        assertEquals(n / 2, tree.size());

        for (int i = 1; i < n; i += 2) {
            assertEquals("val" + i, tree.get(i));
        }
    }
}