package br.com.unidade2.restaurante.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HashTableChaining")
class HashTableChainingTest {

    private HashTableChaining<String, Integer> table;

    @BeforeEach
    void setUp() {
        table = new HashTableChaining<>();
    }

    @Test
    @DisplayName("Tabela nova deve ter size 0 e capacidade inicial 16")
    void newTableHasZeroSizeAndDefaultCapacity() {
        assertEquals(0, table.size());
        assertEquals(16, table.capacity());
    }

    @Test
    @DisplayName("put deve inserir e get deve recuperar o valor correto")
    void putAndGet() {
        table.put("alice", 1);
        assertEquals(1, table.get("alice"));
        assertEquals(1, table.size());
    }

    @Test
    @DisplayName("put com chave existente deve atualizar o valor sem aumentar size")
    void putDuplicateKeyUpdatesValue() {
        table.put("bob", 10);
        table.put("bob", 20);
        assertEquals(20, table.get("bob"));
        assertEquals(1, table.size());
    }

    @Test
    @DisplayName("get para chave inexistente deve retornar null")
    void getMissingKeyReturnsNull() {
        assertNull(table.get("naoexiste"));
    }

    @Test
    @DisplayName("put com chave nula deve lançar IllegalArgumentException")
    void putNullKeyThrows() {
        assertThrows(IllegalArgumentException.class, () -> table.put(null, 1));
    }

    @Test
    @DisplayName("get com chave nula deve retornar null")
    void getNullKeyReturnsNull() {
        assertNull(table.get(null));
    }

    @Test
    @DisplayName("containsKey deve retornar true para chave presente")
    void containsKeyPresent() {
        table.put("carol", 5);
        assertTrue(table.containsKey("carol"));
    }

    @Test
    @DisplayName("containsKey deve retornar false para chave ausente")
    void containsKeyAbsent() {
        assertFalse(table.containsKey("ninguem"));
    }

    @Test
    @DisplayName("remove deve retornar o valor e decrementar size")
    void removeExistingKey() {
        table.put("dave", 99);
        Integer removed = table.remove("dave");
        assertEquals(99, removed);
        assertEquals(0, table.size());
        assertFalse(table.containsKey("dave"));
    }

    @Test
    @DisplayName("remove de chave inexistente deve retornar null sem alterar size")
    void removeMissingKeyReturnsNull() {
        table.put("eve", 7);
        assertNull(table.remove("fantasma"));
        assertEquals(1, table.size());
    }

    @Test
    @DisplayName("remove com chave nula deve retornar null")
    void removeNullKeyReturnsNull() {
        assertNull(table.remove(null));
    }

    @Test
    @DisplayName("Chaves com mesmo índice de bucket devem ser armazenadas e recuperadas corretamente")
    void collisionHandledByChaining() {
        HashTableChaining<Integer, String> intTable = new HashTableChaining<>();
        intTable.put(0, "zero");
        intTable.put(16, "dezesseis");
        intTable.put(32, "trinta_e_dois"); // mesmo bucket

        assertEquals("zero", intTable.get(0));
        assertEquals("dezesseis", intTable.get(16));
        assertEquals("trinta_e_dois", intTable.get(32));
        assertEquals(3, intTable.size());
    }

    @Test
    @DisplayName("remove em bucket com colisão deve remover apenas o alvo")
    void removeInChainRemovesOnlyTarget() {
        HashTableChaining<Integer, String> intTable = new HashTableChaining<>();
        intTable.put(0, "zero");
        intTable.put(16, "dezesseis");

        intTable.remove(0);
        assertNull(intTable.get(0));
        assertEquals("dezesseis", intTable.get(16));
        assertEquals(1, intTable.size());
    }

    @Test
    @DisplayName("Tabela deve dobrar de capacidade ao ultrapassar load factor 0.75")
    void resizeDoublesCapacityOnHighLoad() {
        for (int i = 0; i < 13; i++) {
            table.put("key" + i, i);
        }
        assertEquals(32, table.capacity());
        assertEquals(13, table.size());
    }

    @Test
    @DisplayName("Dados devem ser acessíveis após o resize")
    void dataAccessibleAfterResize() {
        for (int i = 0; i < 13; i++) {
            table.put("key" + i, i * 10);
        }
        for (int i = 0; i < 13; i++) {
            assertEquals(i * 10, table.get("key" + i));
        }
    }

    @Test
    @DisplayName("Deve suportar grande volume de inserções, buscas e remoções")
    void largeVolumeOperations() {
        int n = 500;
        for (int i = 0; i < n; i++) {
            table.put("k" + i, i);
        }
        assertEquals(n, table.size());

        for (int i = 0; i < n; i += 2) {
            table.remove("k" + i);
        }
        assertEquals(n / 2, table.size());

        for (int i = 1; i < n; i += 2) {
            assertEquals(i, table.get("k" + i));
        }
        for (int i = 0; i < n; i += 2) {
            assertNull(table.get("k" + i));
        }
    }
}