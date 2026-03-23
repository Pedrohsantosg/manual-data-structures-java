package br.com.unidade2.restaurante.datastructures;

import java.util.function.BiConsumer;

public class AVLTree<K extends Comparable<K>, V> {

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> left;
        private Node<K, V> right;
        private int height;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node<K, V> root;
    private int size;

    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Chave nula nao permitida");
        }
        root = insert(root, key, value);
    }

    public V get(K key) {
        Node<K, V> current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                return current.value;
            }
            current = cmp < 0 ? current.left : current.right;
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V remove(K key) {
        Holder<V> holder = new Holder<>();
        root = remove(root, key, holder);
        if (holder.present) {
            size--;
        }
        return holder.value;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int height() {
        return height(root);
    }

    public void inOrderTraversal(BiConsumer<K, V> visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException("Visitor nao pode ser nulo");
        }
        traverseInOrder(root, visitor);
    }

    private Node<K, V> insert(Node<K, V> node, K key, V value) {
        if (node == null) {
            size++;
            return new Node<>(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }

        updateHeight(node);
        return rebalance(node);
    }

    private Node<K, V> remove(Node<K, V> node, K key, Holder<V> holder) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key, holder);
        } else if (cmp > 0) {
            node.right = remove(node.right, key, holder);
        } else {
            holder.value = node.value;
            holder.present = true;

            if (node.left == null || node.right == null) {
                Node<K, V> child = node.left != null ? node.left : node.right;
                return child;
            }

            Node<K, V> successor = min(node.right);
            node.key = successor.key;
            node.value = successor.value;
            node.right = removeSuccessor(node.right);
        }

        updateHeight(node);
        return rebalance(node);
    }

    private Node<K, V> removeSuccessor(Node<K, V> node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = removeSuccessor(node.left);
        updateHeight(node);
        return rebalance(node);
    }

    private Node<K, V> min(Node<K, V> node) {
        Node<K, V> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private Node<K, V> rebalance(Node<K, V> node) {
        int balance = balanceFactor(node);

        if (balance > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        if (balance < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    private Node<K, V> rotateRight(Node<K, V> y) {
        Node<K, V> x = y.left;
        Node<K, V> t2 = x.right;

        x.right = y;
        y.left = t2;

        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node<K, V> rotateLeft(Node<K, V> x) {
        Node<K, V> y = x.right;
        Node<K, V> t2 = y.left;

        y.left = x;
        x.right = t2;

        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private int balanceFactor(Node<K, V> node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(Node<K, V> node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    private int height(Node<K, V> node) {
        return node == null ? 0 : node.height;
    }

    private void traverseInOrder(Node<K, V> node, BiConsumer<K, V> visitor) {
        if (node == null) {
            return;
        }
        traverseInOrder(node.left, visitor);
        visitor.accept(node.key, node.value);
        traverseInOrder(node.right, visitor);
    }

    private static class Holder<V> {
        private V value;
        private boolean present;
    }
}
