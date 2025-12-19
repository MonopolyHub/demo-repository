package dataStructures.stack;

public class Stack<T> {
    private Node<T> head;


    public static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    public void push(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = head;
        head = newNode;
    }
    public T pop() {
        if (head == null) {
            return null;
        }
        T data = head.data;
        head = head.next;
        return data;
    }
}
