package DAY14;
import java.util.Stack;

public class Task005_Stack {
    public static void main(String[] args) {
        Stack<Integer> demo = new Stack<>();

        demo.push(10);
        demo.push(20);
        demo.push(30);
        demo.push(40);
        demo.push(50);

        System.out.println("stack:" + demo);
        System.out.println("peak value from the stack:" + demo.peek());
        System.out.println("poping value:" + demo.pop());
        System.out.println("after poping stack value:" + demo);


       /* while ( !demo.isEmpty()){
            System.out.println("values from the stack" + demo);
        }*/
    }
}
