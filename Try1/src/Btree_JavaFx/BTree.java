package Btree_JavaFx;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.ArrayList;
import Utility.nodeCopy;

//Todo check serializable interface and cloneUtils

//node class
class BTNode<E extends Comparable<E>> implements Serializable {

     int fNumber;
     BTNode<E> parent;
     ArrayList<BTNode<E>> children = new ArrayList<BTNode<E>>();
     ArrayList<E> keys = new ArrayList<>();

    public BTNode() {
    }

    public BTNode(int order) {
        fNumber = order - 1;
    }


    public boolean trueIfLastInternalNode() {//VERY  IMPORTANT CODE:Checks if the node is last internal node
        if (keys.size() == 0)//return false if it is a root node
            return false;

        for (BTNode<E> node : children)
            if (node.keys.size() != 0)//if any children of that node is non zero
                return false;
        return true;
    }


    //basic getters and setters
    //parents
    public BTNode<E> getParent() {
        return parent;
    }

    public void setParent(BTNode<E> parent) {
        this.parent = parent;
    }

    //children
    public ArrayList<BTNode<E>> getChildren() {
        return children;
    }

    public void addChild(int index, BTNode<E> node) {
        children.add(index, node);
    }

    public void removeChild(int index) {
        children.remove(index);
    }

    public BTNode<E> getTheChildAtIndex(int index) {
        return children.get(index);
    }

    //key
    public void addKey(int index, E element) {
        keys.add(index, element);
    }

    public E getKey(int index) {
        return keys.get(index);
    }

    public int getSize() {
        return keys.size();
    }

    public void removeKey(int index) {
        keys.remove(index);
    }

    public boolean isFull() {//check if the node is full
        return fNumber == keys.size();
    }

    public boolean isOverflow() {//check if node has more elements than m-1
        return fNumber < keys.size();
    }

    public boolean isNull() {
        return keys.isEmpty();
    }
}

//tree class
public class BTree<K extends Comparable<K>> implements Serializable {

     int hNumber;
     BTNode<K> root = null;
     int order, index, treeSize;
    public final BTNode<K> nullBTNode = new BTNode<K>();

     LinkedList<BTree<K>> stTrees = new LinkedList<BTree<K>>();//making a linked list of trees

    public BTree(int order) {
        if (order < 3) {
            try {
                throw new Exception("Order of B Tree should be more than 2");
            } catch (Exception e) {
                e.printStackTrace();
            }
            order = 3;// putting order as 3 if order inputted is less than 3
        }
        this.order = order;
        hNumber = (order - 1) / 2;
    }

    //basic getters and setters
    //root
    public boolean isEmpty() {//if tree is empty or not
        return root == null;
    }

    public BTNode<K> getRoot() {
        return root;
    }

    public void setRoot(BTNode<K> root) {
        this.root = root;
    }

    //order
    public int getOrder() {
        return order;
    }

    //linked list of trees
    public LinkedList<BTree<K>> getStTrees() {
        return stTrees;
    }

    public void setStTrees(LinkedList<BTree<K>> stTrees) {
        this.stTrees = stTrees;
    }

    //getting vertices tried dfs for this...got errors so used the verticesCount variable in main
    public int getVerticesNumber() {
        if (isEmpty()) {
            return 0;
        }
        else {
            int vertices = 0;
            BTNode<K> currentNode = root;
            int i=0;
        /*while (!currentNode.equals(nullBTNode)) {
            if(!(currentNode.getChild(i).isNull()))
            {
                vertices += currentNode.getChild(i).getSize();
                i++;
            }
            currentNode = currentNode.getChild(i);
            System.out.println("--------------"+vertices);
        }*/
            // vertices = currentNode.getSize();
            return vertices;
        }
    }


    //getting the height of tree
    public int getHeight() {
        if (isEmpty()) {
            return 0;
        } else {
            return getHeight(root);//calling the function with args
        }
    }

    public int getHeight(BTNode<K> node) {
        int height = 0;
        BTNode<K> currentNode = node;
        while (!currentNode.equals(nullBTNode)) {
            currentNode = currentNode.getTheChildAtIndex(0);
            height++;
        }
        return height;
    }


    public BTNode<K> getNode(K key) {
        if (isEmpty()) {
            return nullBTNode;
        }
        BTNode<K> currentNode = root;
        while (!currentNode.equals(nullBTNode)) {
            int i = 0;
            while (i < currentNode.getSize()) {
                if (currentNode.getKey(i).equals(key)) {
                    index = i;
                    return currentNode;
                } else if (currentNode.getKey(i).compareTo(key) > 0) {
                    currentNode = currentNode.getTheChildAtIndex(i);
                    i = 0;
                } else {
                    i++;
                }
            }
            if (!currentNode.isNull()) {
                currentNode = currentNode.getTheChildAtIndex(currentNode.getSize());
            }
        }
        return nullBTNode;
    }

     BTNode<K> getHalfKeys(K key, BTNode<K> fullNode) {
        int fullNodeSize = fullNode.getSize();

        //Add node to the desired location
        for (int i = 0; i < fullNodeSize; i++) {
            if (fullNode.getKey(i).compareTo(key) > 0) {
                fullNode.addKey(i, key);
                break;
            }
        }
        if (fullNodeSize == fullNode.getSize())
            fullNode.addKey(fullNodeSize, key);

        System.out.println("Insert key in between");
        stTrees.add(nodeCopy.clone(this));
        return getHalfKeys(fullNode);
    }

     BTNode<K> getHalfKeys(BTNode<K> fullNode) {
        BTNode<K> newNode = new BTNode<K>(order);
        for (int i = 0; i < hNumber; i++) {
            newNode.addKey(i, fullNode.getKey(0));
            fullNode.removeKey(0);
        }
        return newNode;
    }

     BTNode<K> getRestOfHalfKeys(BTNode<K> halfNode) {
        BTNode<K> newNode = new BTNode<K>(order);
        int halfNodeSize = halfNode.getSize();
        for (int i = 0; i < halfNodeSize; i++) {
            if (i != 0) {
                newNode.addKey(i - 1, halfNode.getKey(1));
                halfNode.removeKey(1);
            }
            newNode.addChild(i, halfNode.getTheChildAtIndex(0));
            halfNode.removeChild(0);
        }
        return newNode;
    }

     void mergeWithFatherNode(BTNode<K> childNode, int index) {
        childNode.getParent().addKey(index, childNode.getKey(0));
        childNode.getParent().removeChild(index);
        childNode.getParent().addChild(index, childNode.getTheChildAtIndex(0));
        childNode.getParent().addChild(index + 1, childNode.getTheChildAtIndex(1));
    }

     void mergeWithFatherNode(BTNode<K> childNode) {
        int fatherNodeSize = childNode.getParent().getSize();
        for (int i = 0; i < fatherNodeSize; i++) {
            if (childNode.getParent().getKey(i).compareTo(childNode.getKey(0)) > 0) {
                mergeWithFatherNode(childNode, i);
                break;
            }
        }
        if (fatherNodeSize == childNode.getParent().getSize()) {
            mergeWithFatherNode(childNode, fatherNodeSize);
        }
        for (int i = 0; i <= childNode.getParent().getSize(); i++)
            childNode.getParent().getTheChildAtIndex(i).setParent(childNode.getParent());
    }

     void setSplitFatherNode(BTNode<K> node) {
        for (int i = 0; i <= node.getSize(); i++)
            node.getTheChildAtIndex(i).setParent(node);
    }

     void processOverflow(BTNode<K> currentNode) {
        BTNode<K> newNode = getHalfKeys(currentNode);
        for (int i = 0; i <= newNode.getSize(); i++) {
            newNode.addChild(i, currentNode.getTheChildAtIndex(0));
            currentNode.removeChild(0);
        }
        BTNode<K> originalNode = getRestOfHalfKeys(currentNode);
        currentNode.addChild(0, newNode);
        currentNode.addChild(1, originalNode);
        originalNode.setParent(currentNode);
        newNode.setParent(currentNode);
        setSplitFatherNode(originalNode);
        setSplitFatherNode(newNode);

        System.out.println("Enter the key in the middle. This is caused due to overflow");

        stTrees.add(nodeCopy.clone(this));
    }


    public void insert(K key) {

        if (isEmpty()) {
            root = new BTNode<K>(order);
            root.addKey(0, key);
            treeSize++;
            root.setParent(nullBTNode);
            root.addChild(0, nullBTNode);
            root.addChild(1, nullBTNode);

            System.out.println("Root inserted");
            stTrees.add(nodeCopy.clone(this));
            return;
        }

        BTNode<K> currentNode = root;

        while (!currentNode.trueIfLastInternalNode()) {
            int i = 0;
            while (i < currentNode.getSize()) {

                if (currentNode.trueIfLastInternalNode()) { // break if currentNode is leaf
                    i = currentNode.getSize();
                } else if (currentNode.getKey(i).compareTo(key) > 0) {
                    currentNode = currentNode.getTheChildAtIndex(i);
                    i = 0;
                } else {
                    i++;
                }
            }
            if (!currentNode.trueIfLastInternalNode())
                currentNode = currentNode.getTheChildAtIndex(currentNode.getSize());
        }


        if (!currentNode.isFull()) {
            int i = 0;
            while (i < currentNode.getSize()) {
                //insertion when key> insertKey
                if (currentNode.getKey(i).compareTo(key) > 0) {
                    currentNode.addKey(i, key);
                    currentNode.addChild(currentNode.getSize(), nullBTNode);
                    treeSize++;

                    System.out.println("Inserted inside the node");
                    stTrees.add(nodeCopy.clone(this));
                    return;
                } else {
                    i++;
                }
            }
            currentNode.addKey(currentNode.getSize(), key);
            currentNode.addChild(currentNode.getSize(), nullBTNode);
            treeSize++;

            System.out.println("Inserted at the end of node");
            stTrees.add(nodeCopy.clone(this));
        } else {

            BTNode<K> newChildNode = getHalfKeys(key, currentNode);
            for (int i = 0; i < hNumber; i++) { // insert after bottom up split
                newChildNode.addChild(i, currentNode.getTheChildAtIndex(0));
                currentNode.removeChild(0);
            }
            newChildNode.addChild(hNumber, nullBTNode);
 // todo ye comment htadein?
// Lay a half-hybrid, just like that, the current node will be a hybrid of the middle key
// Move up 1 item (used to be the father)
            BTNode<K> originalFatherNode = getRestOfHalfKeys(currentNode);
            currentNode.addChild(0, newChildNode);
            currentNode.addChild(1, originalFatherNode);
            originalFatherNode.setParent(currentNode);
            newChildNode.setParent(currentNode);
            treeSize++;

            stTrees.add(nodeCopy.clone(this));

            // If on current, child node cap is higher
            // and node entered on
            if (!currentNode.getParent().equals(nullBTNode)) {
                while (!currentNode.getParent().isOverflow() && !currentNode.getParent().equals(nullBTNode)) {
                    boolean flag = currentNode.getSize() == 1 && !currentNode.getParent().isOverflow();
                    if (currentNode.isOverflow() || flag) {
                        mergeWithFatherNode(currentNode);
                        currentNode = currentNode.getParent();

                        System.out.println("Key inserted in between");
                        stTrees.add(nodeCopy.clone(this));

                        if (currentNode.isOverflow()) {
                            processOverflow(currentNode);
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }


     int findChild(BTNode<K> node) {
        if (!node.equals(root)) {
            BTNode<K> fatherNode = node.getParent();

            for (int i = 0; i <= fatherNode.getSize(); i++) {
                if (fatherNode.getTheChildAtIndex(i).equals(node))
                    return i;
            }
        }
        return -1;
    }


     BTNode<K> balanceDeletedNode(BTNode<K> node) {
        boolean flag;
        int nodeIndex = findChild(node);
        K pair;
        BTNode<K> fatherNode = node.getParent();
        BTNode<K> currentNode;
        if (nodeIndex == 0) {
            currentNode = fatherNode.getTheChildAtIndex(1);
            flag = true;
        } else {
            currentNode = fatherNode.getTheChildAtIndex(nodeIndex - 1);
            flag = false;
        }

        int currentSize = currentNode.getSize();
        if (currentSize > hNumber) {
            if (flag) {
                pair = fatherNode.getKey(0);
                node.addKey(node.getSize(), pair);
                fatherNode.removeKey(0);
                pair = currentNode.getKey(0);
                currentNode.removeKey(0);
                node.addChild(node.getSize(), currentNode.getTheChildAtIndex(0));
                currentNode.removeChild(0);
                fatherNode.addKey(0, pair);
                if (node.trueIfLastInternalNode()) {
                    node.removeChild(0);
                }
                System.out.println("Case 2a:");
                stTrees.add(nodeCopy.clone(this));

            } else {
                pair = fatherNode.getKey(nodeIndex - 1);
                node.addKey(0, pair);
                fatherNode.removeKey(nodeIndex - 1);
                pair = currentNode.getKey(currentSize - 1);
                currentNode.removeKey(currentSize - 1);
                node.addChild(0, currentNode.getTheChildAtIndex(currentSize));
                currentNode.removeChild(currentSize);
                fatherNode.addKey(nodeIndex - 1, pair);
                if (node.trueIfLastInternalNode()) {
                    node.removeChild(0);
                }
                System.out.println("Case 2a:");
                stTrees.add(nodeCopy.clone(this));
            }
            return node;
        } else {
            if (flag) {
                currentNode.addKey(0, fatherNode.getKey(0));
                fatherNode.removeKey(0);
                fatherNode.removeChild(0);
                if (root.getSize() == 0) {
                    root = currentNode;
                    currentNode.setParent(nullBTNode);
                }
                if (node.getSize() == 0) {
                    currentNode.addChild(0, node.getTheChildAtIndex(0));
                    currentNode.getTheChildAtIndex(0).setParent(currentNode);
                }
                for (int i = 0; i < node.getSize(); i++) {
                    currentNode.addKey(i, node.getKey(i));
                    currentNode.addChild(i, node.getTheChildAtIndex(i));
                    currentNode.getTheChildAtIndex(i).setParent(currentNode);
                }
                // Case 2b.1
                System.out.println("Case 2b: Merging");
//				stepMess.add("Merging");
                stTrees.add(nodeCopy.clone(this));
            } else {
                currentNode.addKey(currentNode.getSize(), fatherNode.getKey(nodeIndex - 1));
                fatherNode.removeKey(nodeIndex - 1);
                fatherNode.removeChild(nodeIndex);
                if (root.getSize() == 0) {
                    root = currentNode;
                    currentNode.setParent(nullBTNode);
                }
                int currentNodeSize = currentNode.getSize();
                if (node.getSize() == 0) {
                    currentNode.addChild(currentNodeSize, node.getTheChildAtIndex(0));
                    currentNode.getTheChildAtIndex(currentNodeSize).setParent(currentNode);
                }
                for (int i = 0; i < node.getSize(); i++) {
                    currentNode.addKey(currentNodeSize + i, node.getKey(i));
                    currentNode.addChild(currentNodeSize + i, node.getTheChildAtIndex(i));
                    currentNode.getTheChildAtIndex(currentNodeSize + i).setParent(currentNode);
                }
                System.out.println("Case 2b: Merging");
                stTrees.add(nodeCopy.clone(this));
            }
            return fatherNode;
        }
    }


     BTNode<K> replaceNode(BTNode<K> node) {
        BTNode<K> currentNode = node.getTheChildAtIndex(index + 1);
        while (!currentNode.trueIfLastInternalNode()) {
            currentNode = currentNode.getTheChildAtIndex(0);
        }

        if (currentNode.getSize() - 1 < hNumber) {
            currentNode = node.getTheChildAtIndex(index);
            int currentNodeSize = currentNode.getSize();
            while (!currentNode.trueIfLastInternalNode()) {
                currentNode = currentNode.getTheChildAtIndex(currentNodeSize);
            }
            node.addKey(index, currentNode.getKey(currentNodeSize - 1));
            currentNode.removeKey(currentNodeSize - 1);
            currentNode.addKey(currentNodeSize - 1, node.getKey(index + 1));
            node.removeKey(index + 1);
            index = currentNode.getSize() - 1;
            System.out.println("Replaced single child");
            stTrees.add(nodeCopy.clone(this));
        } else {
            node.addKey(index + 1, currentNode.getKey(0));
            currentNode.removeKey(0);
            currentNode.addKey(0, node.getKey(index));
            node.removeKey(index);
            index = 0;
            // Case 3b
            System.out.println("Replaced");
            stTrees.add(nodeCopy.clone(this));
        }
        return currentNode;
    }



    /* // todo cases
     * Case 1: If k is in the node x which is a leaf and x.size -1 >= halfNumber
     * Case 2: If k is in the node x which is a leaf and x.size -1 < halfNumber Case
     * 3: If k is in the node x and x is an internal node (not a leaf)
     */
    public void delete(K key) {
        stTrees.add(nodeCopy.clone(this));
        BTNode<K> node = getNode(key);
        BTNode<K> deleteNode = null;
        if (node.equals(nullBTNode)) // node not found
            return;


        if (node.equals(root) && node.getSize() == 1 && node.trueIfLastInternalNode()) { // if it is root
            root = null;
            treeSize--;

            System.out.println("Deleted");
            stTrees.add(nodeCopy.clone(this));
        } else {
            boolean flag = true;
            boolean isReplaced = false;
            // TODO: case 3
            if (!node.trueIfLastInternalNode()) {
                node = replaceNode(node);
                deleteNode = node;
                isReplaced = true;
            }

            if (node.getSize() - 1 < hNumber) {
                // TODO: case 2
                node = balanceDeletedNode(node);
                if (isReplaced) {
                    for (int i = 0; i <= node.getSize(); i++) {
                        for (int j = 0; i < node.getTheChildAtIndex(i).getSize(); j++) {
                            if (node.getTheChildAtIndex(i).getKey(j).equals(key)) {
                                deleteNode = node.getTheChildAtIndex(i);
                                break;
                            }
                        }
                    }
                }
            } else if (node.trueIfLastInternalNode()) {
                // TODO: Case 1
                System.out.println("Deleted");
                node.removeChild(0);
            }

            while (!node.getTheChildAtIndex(0).equals(root) && node.getSize() < hNumber && flag) {
                System.out.println("delete");
                if (node.equals(root)) {
                    for (int i = 0; i <= root.getSize(); i++) {
                        if (root.getTheChildAtIndex(i).getSize() == 0) {
                            flag = true;
                            break;
                        } else {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    node = balanceDeletedNode(node);
                }
            }

            if (deleteNode == null) {
                node = getNode(key);
            } else {
                node = deleteNode;
            }

            if (!node.equals(nullBTNode)) {
                for (int i = 0; i < node.getSize(); i++) {
                    if (node.getKey(i) == key) {
                        node.removeKey(i);
                    }
                }
                treeSize--;

                System.out.println("Deleted :" + key);
                stTrees.add(nodeCopy.clone(this));
            }
        }
    }
}
