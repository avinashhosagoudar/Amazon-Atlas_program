package DAY14;/*
package DAY14;

class Node {
    int data;
    Node next;

    // Constructor
    Node(int value) {
        data = value;
        next = null;
    }
}

class CircularLinkedList {
    private Node head;

    // Insert at end
    public void insertAtEnd(int value) {
        Node newNode = new Node(value);
        if (head == null) {
            head = newNode;
            newNode.next = head;
        } else {
            Node temp = head;
            while (temp.next != head) {
                temp = temp.next;
            }
            temp.next = newNode;
            newNode.next = head;
        }
    }

    // Delete a node by value
    public void delete(int value) {
        if (head == null) return;

        Node current = head, prev = null;

        // If head is the node to be deleted
        if (head.data == value) {
            // Only one node
            if (head.next == head) {
                head = null;
                return;
            }

            // Find the last node to update its next
            Node last = head;
            while (last.next != head) {
                last = last.next;
            }

            last.next = head.next;
            head = head.next;
            return;
        }

        // For non-head nodes
        do {
            prev = current;
            current = current.next;
            if (current.data == value) {
                prev.next = current.next;
                return;
            }
        } while (current != head);
    }

    // Display the list
    public void display() {
        if (head == null) {
            System.out.println("List is empty");
            return;
        }

        Node temp = head;
        System.out.print("Circular Linked List: ");
        do {
            System.out.print(temp.data + " -> ");
            temp = temp.next;
        } while (temp != head);

        System.out.println("(back to head)");
    }
}

public class TaskLL004 {
    public static void main(String[] args) {
        CircularLinkedList list = new CircularLinkedList();

        list.insertAtEnd(10);
        list.insertAtEnd(20);
        list.insertAtEnd(30);
        list.insertAtEnd(40);

        list.display(); // Before deletion

        list.delete(10);  // Delete head
        list.display();

        list.delete(30);  // Delete middle node
        list.display();

    }
}
*/
