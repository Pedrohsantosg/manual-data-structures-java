package br.com.unidade2.restaurante.datastructures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class AVLTreeTest {

    @Test
    void deveInserirBuscarERemover() {
        AVLTree<Integer, String> tree = new AVLTree<>();

        tree.put(30, "A");
        tree.put(20, "B");
        tree.put(40, "C");
        tree.put(10, "D");

        assertEquals("A", tree.get(30));
        assertEquals("D", tree.get(10));
        assertTrue(tree.height() <= 3);

        assertEquals("B", tree.remove(20));
        assertNull(tree.get(20));
    }
}
