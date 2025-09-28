package DAY14;

import java.util.Stack;

public class Task006_Stack {
    public static void main(String[] args) {
        Stack<Integer> demo = new Stack<>();

        demo.push(10);
        demo.push(20);
        demo.push(30);
        demo.push(40);
        demo.push(50);

        int pos = demo.search(40); // in stack LIFO basicaly here it wont start index with 0th instead it will start from the 1

        System.out.println("position of the value 40 : "+ pos);
        System.out.println("stack:" + demo);
    }
}
