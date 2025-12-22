package dataStructures.heap;

public class MaxHeap {
`
    private int[] heap;
    private int size;
    private int capacity;

    public MaxHeap(int capacity) {
        this.capacity = capacity;
        this.heap = new int[capacity];
        this.size = 0;
    }

    public void insert(int value) {
        if (size == capacity)
            throw new RuntimeException("Heap full");

        heap[size] = value;
        heapifyUp(size);
        size++;
    }

    public int extractMax() {
        if (size == 0)
            throw new RuntimeException("Heap empty");

        int max = heap[0];
        heap[0] = heap[--size];
        heapifyDown(0);
        return max;
    }

    private void heapifyUp(int index) {
        while (index > 0 && heap[index] > heap[(index - 1) / 2]) {
            swap(index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    private void heapifyDown(int index) {
        int largest = index;
        int left = 2 * index + 1;
        int right = 2 * index + 2;

        if (left < size && heap[left] > heap[largest])
            largest = left;

        if (right < size && heap[right] > heap[largest])
            largest = right;

        if (largest != index) {
            swap(index, largest);
            heapifyDown(largest);
        }
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}