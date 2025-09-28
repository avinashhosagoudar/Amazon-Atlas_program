package DAY13;/*
package DAY13;

class Node {
    int data;
    Node prev, next;

    Node(int data) {
        this.data = data;
        prev = next = null;
    }
}

class DoublyLinkedList {
    Node head, tail;

    // Add node at the end
    void add(int data) {
        Node newNode = new Node(data);

        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    // Display from head to tail
    void displayForward() {
        Node temp = head;
        System.out.print("Forward: ");
        while (temp != null) {
            System.out.print(temp.data + " <-> ");
            temp = temp.next;
        }
        System.out.println("null");
    }

    // Display from tail to head
    void displayBackward() {
        Node temp = tail;
        System.out.print("Backward: ");
        while (temp != null) {
            System.out.print(temp.data + " <-> ");
            temp = temp.prev;
        }
        System.out.println("null");
    }
}

public class DAY7.Task016 {
    public static void main(String[] args) {
        DoublyLinkedList list = new DoublyLinkedList();

        list.add(10);
        list.add(20);
        list.add(30);

        list.displayForward();   // Forward: 10 <-> 20 <-> 30 <-> null
        list.displayBackward();  // Backward: 30 <-> 20 <-> 10 <-> null
    }
}*/
