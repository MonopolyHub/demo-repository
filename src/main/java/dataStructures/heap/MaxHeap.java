
package dataStructures.heap;

public class MaxHeap {

    private DataAsset[] a;
    private int n;

    public MaxHeap(int c) {
        a = new DataAsset[c];
        n = 0;
    }

    public void insert(DataAsset v) {
        a[n] = v;
        int i = n;
        n++;

        while (i > 0 && a[(i - 1) / 2].getValue() < a[i].getValue()) {
            swap(i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    public DataAsset extractMax() {
        if (n == 0) return null;
        DataAsset m = a[0];
        a[0] = a[n - 1];
        n--;
        heapify(0);
        return m;
    }

    public int size() {
        return n;
    }

    private void heapify(int i) {
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;

            if (left < n && a[left].getValue() > a[largest].getValue()) largest = left;
            if (right < n && a[right].getValue() > a[largest].getValue()) largest = right;

            if (largest == i) break;

            swap(i, largest);
            i = largest;
        }
    }

    private void swap(int i, int j) {
        DataAsset t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}

