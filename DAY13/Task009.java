package DAY13;

import java.util.HashSet;
import java.util.LinkedList;




public class Task009 {
    public static void main(String[] args) {


        LinkedList<String> fruits = new LinkedList<>();
        fruits.add("mango");
        fruits.add("banana");
        fruits.add("grapes");

        System.out.println("fruits:" +fruits);

        fruits.addFirst("pinaple");
        fruits.addLast("orange");

        System.out.println("updated fruits list:" + fruits);
        /*System.out.println("frist fruit from tha last:"+ fruits.getFirst());
        System.out.println("last fruit from the last:"+ fruits.getLast());*/

        for(String values : fruits) {
            System.out.println(values);

        }
        System.out.println("  ");
        for (int i = 0; i < fruits.size() ; i++){
            System.out.println(fruits.get(i));
        }
        System.out.println("  ");
        fruits.forEach(items -> System.out.println(items));

    }
}