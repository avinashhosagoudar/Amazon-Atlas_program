package DAY13;/*
package DAY13;

import java.util.Scanner;

class Node {
    Object data; // Accept any type of data
    Node next;

    Node(Object value) {
        data = value;
        next = null;
    }
}

class LinkedList {
    private Node head;

    public LinkedList() {
        head = null;
    }

    // Insert at end
    public void insertAtEnd(Object value) {
        Node newNode = new Node(value);
        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }

    // Display the list
    public void display() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data + " -> ");
            temp = temp.next;
        }
        System.out.println("NULL");
    }
}

public class Task04 {
    public static void main(String[] args) {
        LinkedList list = new LinkedList();
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many elements? ");
        int n = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < n; i++) {
            System.out.print("Enter value " + (i + 1) + ": ");
            String input = scanner.nextLine();
            // Store input as Object (you can later convert if needed)
            list.insertAtEnd(input);
        }

        System.out.print("Linked List: ");
        list.display();
    }
}*/
