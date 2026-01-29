package dataStructures.tree;


public class Asset<T, M> {
    T base;
    Asset<M, ?>[] children;
    int childCount;

    public Asset(T base) {
        this.base = base;
        this.children = new Asset[10];
        this.childCount = 0;
    }

    public void addChild(Asset<M, ?> child) {
        if (childCount < children.length) {
            children[childCount++] = child;
        } else {
            System.out.println("Child limit reached (10).");
        }
    }

    public Asset<M, ?> getChild(int index) {
        return children[index];
    }

    public T getBase() {
        return base;
    }

    public int getChildCount() {
        return childCount;
    }

    // جستجو بر اساس مقدار base فرزند
    public Boolean search(M valueToFind) {
        for (int i = 0; i < childCount; i++) {
            Asset<M, ?> asset = children[i];
            if (asset.getBase() != null && asset.getBase().equals(valueToFind)) {
                return true;
            }
        }
        return false;
    }

    public int searchIndex(M valueToFind) {
        for (int i = 0; i < childCount; i++) {
            Asset<M, ?> asset = children[i];
            if (asset.getBase() != null && asset.getBase().equals(valueToFind)) {
                return i;
            }
        }
        return -1;
    }

    public Boolean removeChild(Asset<M, ?> childToRemove) {
        int indexToRemove = -1;
        for (int i = 0; i < childCount; i++) {
            if (children[i] == childToRemove) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove != -1) {
            for (int i = indexToRemove; i < childCount - 1; i++) {
                children[i] = children[i + 1];
            }
            children[childCount - 1] = null;
            childCount--;
            return true;
        }
        return false;
    }

    // متد traverse برای پیمایش درخت (می‌تواند به صورت بازگشتی یا غیربازگشتی باشد)
    public void traverse() {
        System.out.println("Node: " + base);
        for (int i = 0; i < childCount; i++) {
            children[i].traverse(); // بازگشتی
        }
    }

    // متد کمکی برای جستجو در درخت
    public static <T, M> Asset<T, M> findAsset(Asset<T, M> root, M valueToFind) {
        if (root.getBase().equals(valueToFind)) {
            return root;
        }
        for (int i = 0; i < root.getChildCount(); i++) {
            Asset<T, M> found = findAsset((Asset<T, M>) root.getChild(i), valueToFind);
            if (found != null) {
                return found;
            }
        }
        return null;
    }
}
