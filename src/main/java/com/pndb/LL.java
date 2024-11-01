package com.pndb;

import javafx.scene.input.KeyCode;

class Node {
    KeyCode k;
    Node next = null;
    Node prev = null;
}

public class LL {

    private Node start = null, curr = null;
    private int size = 0;

    LL() {

    }

    void add(KeyCode k) {
        Node node = new Node();
        this.size++;
        node.k = k;
        node.prev = this.curr;
        if (start == null) {
            this.start = node;
            this.curr = this.start;
        } else {
            this.curr.next = node;
            this.curr = this.curr.next;
        }
    }

    void printAll() {
        Node n = this.start;
        while (n != null) {
            System.out.println(n.k.getChar());
            n = n.next;
        }
    }

    int getSize() {
        return this.size;
    }
    
    Node getHead() {
        return this.start;
    }
}
