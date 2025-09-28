package DAY13;

import java.util.LinkedList;




public class Task013 {
    public static void main(String[] args) {


        LinkedList<String> fruits = new LinkedList<>();
        fruits.add("mango");
        fruits.add("banana");
        fruits.add("grapes");

        System.out.println("fruits:" +fruits);

        fruits.addFirst("pinaple");
        fruits.addLast("orange");

        System.out.println("updated fruits list:" + fruits);
        LinkedList<String> cloned = (LinkedList<String>) fruits.clone();
        System.out.println("cloned values" +cloned);


    }
}