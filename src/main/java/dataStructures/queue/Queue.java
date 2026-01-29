package dataStructures.queue;

/**
 * Simple FIFO queue based on circular array (manual implementation).
 */
public class Queue<T> {

    private Object[] elements;
    private int front;
    private int size;

    public Queue() {
        this(16);
    }

    public Queue(int capacity) {
        if (capacity <= 0) capacity = 16;
        this.elements = new Object[capacity];
        this.front = 0;
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(T value) {
        if (size == elements.length) {
            grow();
        }
        int rear = (front + size) % elements.length;
        elements[rear] = value;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        T value = (T) elements[front];
        elements[front] = null;
        front = (front + 1) % elements.length;
        size--;
        return value;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        return (T) elements[front];
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[(front + i) % elements.length] = null;
        }
        front = 0;
        size = 0;
    }

    private void grow() {
        Object[] newArr = new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[(front + i) % elements.length];
        }
        elements = newArr;
        front = 0;
    }
}
