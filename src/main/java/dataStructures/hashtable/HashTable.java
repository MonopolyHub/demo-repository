package dataStructures.hashtable;

public class HashTable {

    private String[] keys;
    private int size;

    public HashTable(int capacity) {
        keys = new String[capacity];
        size = 0;
    }

    private int hash(String key) {
        return Math.abs(key.hashCode()) % keys.length;
    }

    public void put(String key) {
        int index = hash(key);

        while (keys[index] != null) {
            index = (index + 1) % keys.length;
        }

        keys[index] = key;
        size++;
    }

    public boolean contains(String key) {
        int index = hash(key);

        while (keys[index] != null) {
            if (keys[index].equals(key))
                return true;
            index = (index + 1) % keys.length;
        }

        return false;
    }
}