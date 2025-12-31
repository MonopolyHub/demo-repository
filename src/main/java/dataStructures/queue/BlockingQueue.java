package dataStructures.queue;

public class BlockingQueue<T> {

    private Object[] elements;
    private int front, rear, size, capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
        this.elements = new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    public BlockingQueue() {
        this(10);
    }

    // معادل put()
    public synchronized void enqueue(T value) {
        while (size == capacity) {
            try {
                wait(); // صف پره → صبر کن
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        rear = (rear + 1) % capacity;
        elements[rear] = value;
        size++;

        notifyAll(); // شاید کسی منتظر dequeue بوده
    }

    // معادل take()
    @SuppressWarnings("unchecked")
    public synchronized T dequeue() {
        while (size == 0) {
            try {
                wait(); // صف خالیه → صبر کن
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        T value = (T) elements[front];
        elements[front] = null;
        front = (front + 1) % capacity;
        size--;

        notifyAll(); // شاید کسی منتظر enqueue بوده
        return value;
    }

    public synchronized boolean isEmpty() {
        return size == 0;
    }

    public synchronized int size() {
        return size;
    }
}
