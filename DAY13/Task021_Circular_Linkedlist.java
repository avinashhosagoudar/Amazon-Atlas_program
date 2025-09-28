package DAY13;
class Node {
    int data;
    Node next;


    Node(int data) {
        this.data = data;
        this.next = null;
    }
}
class CircularLinkedList {
    private Node head = null;
    private Node tail = null;


    public void add(int value) {
        Node newNode = new Node(value);

        if (head == null) {
            head = tail = newNode;
            newNode.next = head;
        } else {
            tail.next = newNode;
            tail = newNode;
            tail.next = head;
        }
    }


    public void display() {
        if (head == null) {
            System.out.println("Circular Linked List is empty.");
            return;
        }

        Node current = head;
        System.out.print("Circular Linked List: ");
        do {
            System.out.print(current.data + " -> ");
            current = current.next;
        } while (current != head);

        System.out.println("(back to head)");
    }
}
public class Task021_Circular_Linkedlist{
    public static void main(String[] args) {
        CircularLinkedList cll = new CircularLinkedList();

        cll.add(5);
        cll.add(10);
        cll.add(15);
        cll.add(20);


        cll.display();
    }
}