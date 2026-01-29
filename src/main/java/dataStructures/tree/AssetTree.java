package dataStructures.tree;

public class AssetTree<T> {

    private Asset root;

    public AssetTree(T base) {
        root = new Asset(base);
    }

    public Asset getRoot() {
        return root;
    }

    public void addProperty( Asset property) {
        root.addChild(property);
    }

    public void removeProperty(T propertyName) {
        for (int i = 0; i < root.childCount; i++) {
            if (root.children[i].getBase().equals(propertyName)) {
                for (int j = i; j < root.childCount - 1; j++) {
                    root.children[j] = root.children[j + 1];
                }
                root.children[root.childCount - 1] = null;
                root.childCount--;
                return;
            }
        }
    }
}