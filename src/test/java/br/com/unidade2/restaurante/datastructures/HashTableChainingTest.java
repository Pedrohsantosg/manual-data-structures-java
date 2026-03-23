package br.com.unidade2.restaurante.datastructures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class HashTableChainingTest {

    @Test
    void deveInserirAtualizarERemoverComChaining() {
        HashTableChaining<String, Integer> table = new HashTableChaining<>();

        table.put("Ana", 1);
        table.put("Bia", 2);
        table.put("Ana", 3);

        assertEquals(3, table.get("Ana"));
        assertEquals(2, table.get("Bia"));

        assertEquals(2, table.remove("Bia"));
        assertNull(table.get("Bia"));
    }
}
