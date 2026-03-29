package br.com.unidade2.restaurante.datastructures;

/**
 * Implementação de uma Tabela Hash com chaining
 *
 * @author Pedro, Bia, Julio e Livia
 * @since 20/03/2026
 */

public class HashTableChaining<K, V> {

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Entry<K, V>[] buckets;
    private int size;

    /**
     * Constrói uma nova HashTable com capacidade inicial padrão de 16.
     */
    @SuppressWarnings("unchecked")
    public HashTableChaining() {
        buckets = (Entry<K, V>[]) new Entry[16];
    }

    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Chave nula nao permitida");
        }

        int index = indexFor(key, buckets.length);
        Entry<K, V> current = buckets[index];

        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Entry<K, V> entry = new Entry<>(key, value);
        entry.next = buckets[index];
        buckets[index] = entry;
        size++;

        if ((double) size / buckets.length > 0.75) {
            resize();
        }
    }


    /**
     * Retorna o valor para o qual a chave especificada está mapeada.
     *
     * @param key a chave cujo valor associado deve ser retornado.
     * @return o valor associado à chave, ou {@code null} se a chave não for encontrada.
     */
    public V get(K key) {
        if (key == null) {
            return null;
        }

        int index = indexFor(key, buckets.length);
        Entry<K, V> current = buckets[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V remove(K key) {
        if (key == null) {
            return null;
        }

        int index = indexFor(key, buckets.length);
        Entry<K, V> current = buckets[index];
        Entry<K, V> previous = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return current.value;
            }
            previous = current;
            current = current.next;
        }

        return null;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return buckets.length;
    }

    private int indexFor(K key, int capacity) {
        return (key.hashCode() & 0x7fffffff) % capacity;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldBuckets = buckets;
        buckets = (Entry<K, V>[]) new Entry[oldBuckets.length * 2];

        for (Entry<K, V> head : oldBuckets) {
            Entry<K, V> current = head;
            while (current != null) {
                Entry<K, V> next = current.next;
                int index = indexFor(current.key, buckets.length);
                current.next = buckets[index];
                buckets[index] = current;
                current = next;
            }
        }
    }
}
