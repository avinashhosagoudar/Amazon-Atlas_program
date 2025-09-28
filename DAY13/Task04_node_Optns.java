package DAY13;/*
package DAY13;

import java.util.Scanner;

class Node {
    Object data;
    Node next;

    Node(Object data) {
        this.data = data;
        this.next = null;
    }
}

class CustomLinkedList {
    private Node head;
    private int size;

    public void add(Object data) {
        Node newNode = new Node(data);
        size++;

        if (head == null) {
            head = newNode;
            return;
        }

        Node temp = head;
        while (temp.next != null)
            temp = temp.next;

        temp.next = newNode;
    }

    public void remove(Object data) {
        if (head == null) return;

        if (head.data.equals(data)) {
            head = head.next;
            size--;
            return;
        }

        Node temp = head;
        while (temp.next != null && !temp.next.data.equals(data)) {
            temp = temp.next;
        }

        if (temp.next != null) {
            temp.next = temp.next.next;
            size--;
        } else {
            System.out.println("Value not found.");
        }
    }

    public void display() {
        if (head == null) {
            System.out.println("List is empty.");
            return;
        }

        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data + " -> ");
            temp = temp.next;
        }
        System.out.println("NULL");
    }

    public int getSize() {
        return size;
    }

    public Object get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds.");

        Node temp = head;
        for (int i = 0; i < index; i++)
            temp = temp.next;

        return temp.data;
    }
}


public class Task04_node_Optns {
    public static void main(String[] args) {
        CustomLinkedList list = new CustomLinkedList();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add Element");
            System.out.println("2. Remove Element");
            System.out.println("3. Display List");
            System.out.println("4. Get Size");
            System.out.println("5. Get Element by Index");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume leftover newline

            switch (choice) {
                case 1:
                    System.out.print("Enter any value (string, number, etc.): ");
                    String value = sc.nextLine();
                    list.add(value);
                    break;

                case 2:
                    System.out.print("Enter value to remove: ");
                    String toRemove = sc.nextLine();
                    list.remove(toRemove);
                    break;

                case 3:
                    list.display();
                    break;

                case 4:
                    System.out.println("Size: " + list.getSize());
                    break;

                case 5:
                    try {
                        System.out.print("Enter index: ");
                        int index = sc.nextInt();
                        System.out.println("Element at index " + index + ": " + list.get(index));
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 6:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}*/
