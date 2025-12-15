package model;

public class LinkedList<T> {

    public class Node {
        public T data;
        public Node next;
        public Node prev;

        public Node(T data) {
            this.data = data;
        }
    }

    private Node head;
    private Node tail;
    private int size = 0;

    public Node add(T data) {
        Node newNode = new Node(data);

        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
        }
        tail = newNode;
        size++;
        return newNode; 
    }


    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }


    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
