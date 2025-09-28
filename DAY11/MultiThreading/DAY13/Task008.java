package DAY13;

import java.util.LinkedList;




public class Task008 {
    public static void main(String[] args) {


        LinkedList<String> fruits = new LinkedList<>();
        fruits.add("mango");
        fruits.add("banana");
        fruits.add("grapes");

        System.out.println("fruits:" +fruits);

        fruits.addFirst("pinaple");
        fruits.addLast("orange");

        System.out.println("updated fruits list:" + fruits);

        fruits.set(1,"ananas");
        System.out.println("after updated new value:" +fruits);
       // System.out.println("frist fruit from tha last:"+ fruits.getFirst());
       // System.out.println("last fruit from the last:"+ fruits.getLast());

    }
}
