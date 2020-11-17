package bTreeCode;


public class Btree {
    public BTreeNode rootNode; // Pointer to root node
    public int minimumDegree; // Minimum degree

    // Constructor (Initializes tree as empty)
    Btree(int min) {
        this.rootNode = null;
        this.minimumDegree = min;
    }

    // function to traverse the tree
    public void traverse() {
        if (this.rootNode != null)
            this.rootNode.traverse();
        System.out.println();
    }

    // function to search a key in this tree
    public BTreeNode search(int key) {
        if (this.rootNode == null)
            return null;
        else
            return this.rootNode.search(key);
    }
}

// A BTree node
class BTreeNode {
    int[] keysArray; // An array of keys
    int minDeg; // Minimum degree (defines the range for number of keys)
    BTreeNode[] childPointerArray; // An array of child pointers
    int noOfKeys; // Current number of keys
    boolean isLeaf; // Is true when node is leaf. Otherwise false

    // Constructor
    BTreeNode(int minDeg, boolean isLeaf) {
        this.minDeg = minDeg;
        this.isLeaf = isLeaf;
        this.keysArray = new int[2 * minDeg - 1];
        this.childPointerArray = new BTreeNode[2 * minDeg];
        this.noOfKeys = 0;
    }

    // A function to traverse all nodes in a subtree rooted with this node
    public void traverse() {

        // There are n keys and n+1 children, travers through n keys
        // and first n children
        int i = 0;
        for (i = 0; i < this.noOfKeys; i++) {

            // If this is not leaf, then before printing key[i],
            // traverse the subtree rooted with child C[i].
            if (!this.isLeaf) {
                childPointerArray[i].traverse();
            }
            System.out.print(keysArray[i] + " ");
        }

        // Print the subtree rooted with last child
        if (!isLeaf)
            childPointerArray[i].traverse();
    }

    // A function to search a key in the subtree rooted with this node.
    BTreeNode search(int k) { // returns NULL if k is not present.

        // Find the first key greater than or equal to k
        int i = 0;
        while (i < noOfKeys && k > keysArray[i])
            i++;

        // If the found key is equal to k, return this node
        if (keysArray[i] == k)
            return this;

        // If the key is not found here and this is a leaf node
        if (isLeaf)
            return null;

        // Go to the appropriate child
        return childPointerArray[i].search(k);

    }
}
