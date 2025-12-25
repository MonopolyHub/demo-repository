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

    public void enqueue(T value) {
        if (size == capacity)
            throw new RuntimeException("Queue is full");

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

    public boolean isEmpty() {
        return size == 0;
    }
}