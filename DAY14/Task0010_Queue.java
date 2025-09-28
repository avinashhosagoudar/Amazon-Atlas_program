package DAY14;

class Node {
    int data;
    Node next;

    Node(int value) {
        data = value;
        next = null;
    }
}


class CustomQueue {
    private Node front, rear;


    public CustomQueue() {
        front = rear = null;
    }


    public void enqueue(int value) {
        Node newNode = new Node(value);

        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
    }


    public int dequeue() {
        if (front == null) {
            System.out.println("Queue is empty");
            return -1;
        }

        int value = front.data;
        front = front.next;

        if (front == null) {
            rear = null;
        }

        return value;
    }


    public int peek() {
        if (front == null) {
            System.out.println("Queue is empty");
            return -1;
        }

        return front.data;
    }
    public boolean isEmpty(){
        return front == null;

    }

    public void display() {
        if (front == null) {
            System.out.println("Queue is empty");
            return;
        }

        Node temp = front;
        System.out.print("Queue: ");
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.println("null");
    }
}


public class Task0010_Queue {
    public static void main(String[] args) {
        CustomQueue queue = new CustomQueue();

        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.enqueue(40);


        queue.display();

        System.out.println("peek element: " + queue.peek());

        System.out.println("Dequeued element: " + queue.dequeue());

        queue.display();

        System.out.println("is queue isEmpty:" +queue.isEmpty() );
    }
}
