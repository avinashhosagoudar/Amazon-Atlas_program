package DAY13;/*ackage DAY13;

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

    public CustomLinkedList() {
        head = null;
        size = 0;
    }

    // 1. Create & Insert at End
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

    // 2. Remove node by value
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

    // 3. Display list
    public void display() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data + " -> ");
            temp = temp.next;
        }
        System.out.println("NULL");
    }

    // 4. Size of the list
    public int getSize() {
        return size;
    }

    // 5. Get element at specific index (with index check)
    public Object get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds.");
        }

        Node temp = head;
        for (int i = 0; i < index; i++)
            temp = temp.next;

        return temp.data;
    }
}


public class Task04_nodes {
    public static void main(String[] args) {
        CustomLinkedList list = new CustomLinkedList();

        list.add(10);
        list.add("Java");
        list.add(3.14);
        list.add(true);

        System.out.print("All Elements: ");
        list.display();

        System.out.println("Size: " + list.getSize());

        System.out.println("Element at index 2: " + list.get(2));

        list.remove("Java");

        System.out.print("After removing 'Java': ");
        list.display();

        System.out.println("Size: " + list.getSize());

        // Uncomment this to test IndexOutOfBounds
        // System.out.println(list.get(10)); // Throws exception
    }
}**/