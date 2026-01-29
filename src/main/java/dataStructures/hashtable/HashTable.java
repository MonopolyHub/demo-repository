package dataStructures.hashtable;

/**
 * A small educational HashTable implementation.
 *
 * Manual implementation (no java.util.HashMap / LinkedList).
 *
 * Collision handling: separate chaining.
 */
public class HashTable<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double MAX_LOAD_FACTOR = 0.75;

    private static class EntryNode<K, V> {
        K key;
        V value;
        EntryNode<K, V> next;

        EntryNode(K key, V value, EntryNode<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private EntryNode<K, V>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public HashTable() {
        this.buckets = (EntryNode<K, V>[]) new EntryNode[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        if ((size + 1) > buckets.length * MAX_LOAD_FACTOR) {
            rehash(buckets.length * 2);
        }

        int index = indexFor(key, buckets.length);
        EntryNode<K, V> head = buckets[index];

        for (EntryNode<K, V> cur = head; cur != null; cur = cur.next) {
            if (cur.key.equals(key)) {
                cur.value = value;
                return;
            }
        }

        buckets[index] = new EntryNode<>(key, value, head);
        size++;
    }

    public V get(K key) {
        if (key == null) {
            return null;
        }
        int index = indexFor(key, buckets.length);
        for (EntryNode<K, V> cur = buckets[index]; cur != null; cur = cur.next) {
            if (cur.key.equals(key)) {
                return cur.value;
            }
        }
        return null;
    }

    public boolean remove(K key) {
        if (key == null) {
            return false;
        }
        int index = indexFor(key, buckets.length);
        EntryNode<K, V> cur = buckets[index];
        EntryNode<K, V> prev = null;
        while (cur != null) {
            if (cur.key.equals(key)) {
                if (prev == null) {
                    buckets[index] = cur.next;
                } else {
                    prev.next = cur.next;
                }
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    /**
     * Returns all values as a compact array (size = number of keys).
     * This is useful for reports, snapshots, etc.
     */
    @SuppressWarnings("unchecked")
    public V[] values() {
        V[] out = (V[]) new Object[size];
        int i = 0;
        for (int b = 0; b < buckets.length; b++) {
            for (EntryNode<K, V> cur = buckets[b]; cur != null; cur = cur.next) {
                out[i++] = cur.value;
            }
        }
        return out;
    }

    private int indexFor(K key, int mod) {
        int h = key.hashCode();
        if (h < 0) h = -h;
        return h % mod;
    }

    @SuppressWarnings("unchecked")
    private void rehash(int newCapacity) {
        EntryNode<K, V>[] old = buckets;
        buckets = (EntryNode<K, V>[]) new EntryNode[newCapacity];
        size = 0;
        for (int b = 0; b < old.length; b++) {
            for (EntryNode<K, V> cur = old[b]; cur != null; cur = cur.next) {
                put(cur.key, cur.value);
            }
        }
    }
}
