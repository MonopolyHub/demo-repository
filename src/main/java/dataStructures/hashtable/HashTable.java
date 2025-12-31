package dataStructures.hashtable;

import java.util.LinkedList;

public class HashTable<K, V> {

    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int size;
    private LinkedList<Entry<K, V>>[] table;

    public HashTable() {
        this.size = size;
        table = new LinkedList[size];

        for (int i = 0; i < size; i++) {
            table[i] = new LinkedList<>();
        }
    }

    private int hash(K key) {
        int hash = key.hashCode();
        if (hash < 0)
            hash = -hash;
        return hash % size;

    }

    public void put(K key, V value) {
        int index = hash(key);

        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                entry.value = value; // update
                return;
            }
        }

        table[index].add(new Entry<>(key, value)); // insert
    }

    public V get(K key) {
        int index = hash(key);

        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null;
    }

    public boolean remove(K key) {
        int index = hash(key);

        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                table[index].remove(entry);
                return true;
            }
        }

        return false;
    }
    public V[] values() {
        @SuppressWarnings("unchecked")
        V[] result = (V[]) new Object[size];

        int index = 0;
        for (LinkedList<Entry<K, V>> entry : table) {
            if (entry != null) {
                result [index++] = (V) entry;
            }
        }
        return result;
    }

    public int length() {
        return table.length;
    }
}
