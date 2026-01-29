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

    public Stack() {
        this(64);
    }

    public void push(T value) {
        if (top == capacity - 1) {
            grow();
        }
        elements[++top] = value;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty())
            throw new RuntimeException("Stack underflow");

        return (T) elements[top--];
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return (T) elements[top];
    }

    public int size() {
        return top + 1;
    }

    public void clear() {
        // keep capacity, just reset top
        top = -1;
    }

    private void grow() {
        int newCap = capacity * 2;
        Object[] newArr = new Object[newCap];
        System.arraycopy(elements, 0, newArr, 0, capacity);
        elements = newArr;
        capacity = newCap;
    }

    public boolean isEmpty() {
        return top == -1;
    }
}