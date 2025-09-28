package DAY13;
import java.util.*;


public class Task006 {
    public static void main(String[] args) {


        LinkedList<String> fruits = new LinkedList<>();
        fruits.add("mango");
        fruits.add("banana");
        fruits.add("grapes");

        System.out.println("fruits:" +fruits);

        fruits.addFirst("pinaple");
        fruits.addLast("orange");

        System.out.println("updated fruits list:" + fruits);
        System.out.println("frist fruit from tha last:"+ fruits.getFirst());
        System.out.println("last fruit from the last:"+ fruits.getLast());

    }
}
