package DAY14;

import java.util.Stack;

public class Task008_Stack {
    public static void main(String[] args) {
        Stack<Integer> demo = new Stack<>();

        demo.push(10);
        demo.push(20);
        demo.push(30);
        demo.push(40);
        demo.push(50);

        System.out.println("stack:" + demo);
        System.out.println("peak value from the stack:" + demo.peek());
    }
}
