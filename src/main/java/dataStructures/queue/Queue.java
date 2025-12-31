package dataStructures.queue;

public class Queue<T> {

    private Object[] elements;
    private int front, rear, size, capacity;

    public Queue(int capacity) {
        this.capacity = capacity;
        this.elements = new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    public Queue() {
        this(10);
    }

    public void enqueue(T value) {
        if (size == capacity) shift();

        rear = (rear + 1) % capacity;
        elements[rear] = value;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty())
            throw new RuntimeException("Queue is empty");

        T value = (T) elements[front];
        front = (front + 1) % capacity;
        size--;
        return value;
    }

    public void shift() {
        if (isEmpty())
            throw new RuntimeException("Queue is empty");

        for (int i = 0; i < size - 1; i++) {
            elements[(front + i) % capacity] =
                    elements[(front + i + 1) % capacity];
        }

        rear = (rear - 1 + capacity) % capacity;
        elements[rear] = null;
        size--;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
