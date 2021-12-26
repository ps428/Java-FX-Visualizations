package PrincetonCodeBTree;

/******************************************************************************
 *  Compilation:  javac BTree.java
 *  Execution:    java BTree
 *  Dependencies: System.out.java
 *
 *  B-tree.
 *
 *  Limitations
 *  -----------
 *   -  Assumes M is even and M >= 4
 *   -  should b be an array of children or list (it would help with
 *      casting to make it a list)
 *
 ******************************************************************************/


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static Media.audioTest.*;

/**
 *  The {@code BTree} class represents an ordered symbol table of generic
 *  key-value pairs.
 *  It supports the <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>size</em>, and <em>is-empty</em> methods.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}—setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  This implementation uses a B-tree. It requires that
 *  the key type implements the {@code Comparable} interface and calls the
 *  {@code compareTo()} and method to compare two keys. It does not call either
 *  {@code equals()} or {@code hashCode()}.
 *  The <em>get</em>, <em>put</em>, and <em>contains</em> operations
 *  each make log<sub><em>m</em></sub>(<em>n</em>) probes in the worst case,
 *  where <em>n</em> is the number of key-value pairs
 *  and <em>m</em> is the branching factor.
 *  The <em>size</em>, and <em>is-empty</em> operations take constant time.
 *  Construction takes constant time.
 *  <p>
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/62btree">Section 6.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class BTree<Key extends Comparable<Key>, Value> extends Application {
    // max children per B-tree node = M-1
    // (must be even and greater than 2)
    private static final int M = 4;

    //ADDED BY ME
    public static int i=0;
    private HBox arrayBox = new HBox();
    static BTree<String, String> st = new BTree<>();

    //ADDITION OVER

    private Node root;       // root of the B-tree
    private int height;      // height of the B-tree
    private int n;           // number of key-value pairs in the B-tree

    // helper B-tree node data type
    private static final class Node {
        private int m;                             // number of children
        private Entry[] children = new Entry[M];   // the array of children

        // create a node with k children
        private Node(int k) {
            m = k;
        }
    }

    // internal nodes: only use key and next
    // external nodes: only use key and value
    private static class Entry {
        private Comparable key;
        private Object val;
        private Node next;     // helper field to iterate over array entries
        public Entry(Comparable key, Object val, Node next) {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }
    }

    /**
     * Initializes an empty B-tree.
     */
    public BTree() {
        root = new Node(0);
    }


    /**
     * Returns true if this symbol table is empty.
     * @return {@code true} if this symbol table is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Returns the height of this B-tree (for debugging).
     *
     * @return the height of this B-tree
     */
    public int height() {
        return height;
    }


    /**
     * Returns the value associated with the given key.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     *         and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        return search(root, key, height);
    }

    private Value search(Node x, Key key, int ht) {
        Entry[] children = x.children;

        // external node
        if (ht == 0) {
            for (int j = 0; j < x.m; j++) {
                if (eq(key, children[j].key)) return (Value) children[j].val;
            }
        }

        // internal node
        else {
            for (int j = 0; j < x.m; j++) {
                if (j+1 == x.m || less(key, children[j+1].key))
                    return search(children[j].next, key, ht-1);
            }
        }
        return null;
    }


    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("argument key to put() is null");
        Node u = insert(root, key, val, height);
        n++;
        if (u == null) return;

        // need to split root
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        root = t;
        height++;
    }

    private Node insert(Node h, Key key, Value val, int ht) {
        int j;
        Entry t = new Entry(key, val, null);

        // external node
        if (ht == 0) {
            for (j = 0; j < h.m; j++) {
                if (less(key, h.children[j].key)) break;
            }
        }

        // internal node
        else {
            for (j = 0; j < h.m; j++) {
                if ((j+1 == h.m) || less(key, h.children[j+1].key)) {
                    Node u = insert(h.children[j++].next, key, val, ht-1);
                    if (u == null) return null;
                    t.key = u.children[0].key;
                    t.val = null;
                    t.next = u;
                    break;
                }
            }
        }

        for (int i = h.m; i > j; i--)
            h.children[i] = h.children[i-1];
        h.children[j] = t;
        h.m++;
        if (h.m < M) return null;
        else         return split(h);
    }

    // split node in half
    private Node split(Node h) {
        Node t = new Node(M/2);
        h.m = M/2;
        for (int j = 0; j < M/2; j++)
            t.children[j] = h.children[M/2+j];
        return t;
    }

    /**
     * Returns a string representation of this B-tree (for debugging).
     *
     * @return a string representation of this B-tree.
     */
    public String toString() {
        return toString(root, height, "") + "\n";
    }

    private String toString(Node h, int ht, String indent) {
        StringBuilder s = new StringBuilder();
        Entry[] children = h.children;

        if (ht == 0) {
            for (int j = 0; j < h.m; j++) {
                s.append(indent + children[j].key + " " + children[j].val + "\n");
            }
        }
        else {
            for (int j = 0; j < h.m; j++) {
                if (j > 0) s.append(indent + "(" + children[j].key + ")\n");
                s.append(toString(children[j].next, ht-1, indent + "     "));
            }
        }
        return s.toString();
    }


    // comparison functions - make Comparable instead of Key to avoid casts
    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }


    /**
     * Unit tests the {@code BTree} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
    launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //ADDED BY ME
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: #2C2F33;");
        pane.setPrefSize(1000,1000);

        Stage stage = new Stage();
        stage.setTitle("B Tree Visualization");

        HBox controls = new HBox();
        controls.setStyle("-fx-background-color: #23272A;");

        final javafx.scene.control.TextField field = new javafx.scene.control.TextField();//IT IS FINAL CAUSE IN LAMBDA FUNCTIONS, THE VARIABLES SHOULD BE EITHER FINAL OR ATOMIC INTEGER
        javafx.scene.control.Button addButton = new Button("Add to BTree");
        addButton.setStyle("-fx-background-color: #21d4ec ; ");
        addButton.setOnAction(event -> {
                    try {
                        int tmp =Integer.parseInt(field.getText());
                        st.put(Integer.toString(i),Integer.toString(tmp));
                        //try {
                            addedAudio();
                       // }
                        /*catch (Exception ex) {
                          //  JOptionPane.showMessageDialog(null, ex);
                        }*/
                       // primaryStage.show();
                        i++;

                        //todo add this new node to hash map of nodes
                    } catch (NumberFormatException ex) {
                        Alert a = new Alert(Alert.AlertType.ERROR, "Must be an integer.");
                        errorAudio();
                        a.show();
                    }
                }
        );

        AtomicInteger find= new AtomicInteger();//MADE IT ATOMIC INTEGER AS THERE WAS AN ERROR IN IDE WHEN I WAS USING A GENERAL INT INSIDE THE LAMBDA FUNCTION. THROUGH ITS RECOMMENDATION, I MADE find ATOMIC iNTEGER
        AtomicBoolean foundORNot = new AtomicBoolean(false);//SAME REASONING

        //to-do missing audio when i uncomment searchButton :DONE was defining addButton function again instead of defining searchButton

        javafx.scene.control.Button searchButton = new Button("Search node");
        searchButton.setStyle("-fx-background-color: #21d4ec ; ");
        searchButton.setOnAction(event -> {
                    try {
                        foundORNot.set(false);
                        for(int j=0;j<st.size();j++){
                            if(st.get(Integer.toString(j)).equals(field.getText())){
                                find.set(j);
                            }
                        }
                        if (st.get(Integer.toString(find.get()))!=null){
                            foundORNot.set(true);
                        }

                        if(foundORNot.get()){
                            foundAudio();
                            //todo change this node's color in hash map of nodes
                        }
                        else {
                            notFoundAudio();
                            Alert a = new Alert(Alert.AlertType.ERROR, "Node not found.");
                            a.show();
                        }
                    } catch (NumberFormatException ex) {
                        Alert a = new Alert(Alert.AlertType.ERROR, "Must be an integer.");
                        errorAudio();
                        a.show();
                    }
                }
        );


        javafx.scene.control.Button deleteButton = new Button("Delete node");
        deleteButton.setStyle("-fx-background-color: #21d4ec ; ");
        deleteButton.setOnAction(event -> {
                    try {
                        for(int j=0;j<st.size();j++){
                            if(st.get(Integer.toString(j)).equals(field.getText())){
                                find.set(j);
                            }
                        }
                        if (st.get(Integer.toString(find.get()))!=null){
                            foundORNot.set(true);
                        }

                        //todo if found then delete this, else if not found then throw error
                        deletedAudio();

                    } catch (NumberFormatException ex) {
                        Alert a = new Alert(Alert.AlertType.ERROR, "Must be an integer.");
                        errorAudio();
                        a.show();
                    }
                }
        );




        controls.getChildren().addAll(field, addButton, deleteButton,  searchButton);
        controls.setSpacing(15.0);
        pane.getChildren().add(controls);

        arrayBox.setAlignment(Pos.CENTER);
        arrayBox.setSpacing(15.0);
        //arrayBox.getChildren().add(new BinaryHeapNode(-1).getArrayBox());
        pane.getChildren().add(arrayBox);


        Scene scene = new Scene(pane, 1000, 1000);
        stage.setScene(scene);
        //stage.show();
        //ADDITION OVER

        stage.show();

        arrayBox.setTranslateX(30);
        arrayBox.setTranslateY(pane.getHeight() - 60);

        System.out.println("size:    " + st.size());
        System.out.println("height:  " + st.height());
        System.out.println(st);
        System.out.println();
    }


}

/******************************************************************************
 *  Copyright 2002-2020, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/


///ADDED BY ME
class DrawNode{
    Text txt;
    Circle circle;

    DrawNode(String s, double x, double y, Color fillColor, Color borderColor, Color textColor) {
        circle = new Circle(10,fillColor);
        circle.setStroke(borderColor);
        circle.setCenterX(x);
        circle.setCenterY(y);

        txt = new Text(x + 5 - s.length(), y + 5, s);
        txt.setFill(textColor);
        txt.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 8));
    }
}
//ADDITION OVER
