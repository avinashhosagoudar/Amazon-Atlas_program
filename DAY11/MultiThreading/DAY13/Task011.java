package DAY13;

import java.util.Arrays;
import java.util.LinkedList;




public class Task011 {
    public static void main(String[] args) {


        LinkedList<String> fruits = new LinkedList<>();
        fruits.add("mango");
        fruits.add("banana");
        fruits.add("grapes");

        System.out.println("fruits:" +fruits);

        fruits.addFirst("pinaple");
        fruits.addLast("orange");

        System.out.println("updated fruits list:" + fruits);

        Object [] arr = fruits.toArray();
        System.out.println(Arrays.toString(arr));

    }
}
