package dataStructures.stack;

public class Stack<T> {

    private Object[] elements;
    private int top;
    private int capacity;

    public Stack(int capacity) {
        this.capacity = capacity;
        this.elements = new Object[capacity];
        this.top = -1;
    }

    public void push(T value) {
        if (top == capacity - 1)
            throw new RuntimeException("Stack overflow");

        elements[++top] = value;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty())
            throw new RuntimeException("Stack underflow");

        return (T) elements[top--];
    }

    public boolean isEmpty() {
        return top == -1;
    }
}