package DAY13;

import  java.util.*;


public class Task007 {
    public static void main(String[] args) {


        LinkedList<String> fruits = new LinkedList<>();
        fruits.add("mango");
        fruits.add("banana");
        fruits.add("grapes");

        System.out.println("fruits:" +fruits);

        fruits.addFirst("pinaple");
        fruits.addLast("orange");

        System.out.println("updated fruits list:" + fruits);
        System.out.println("removed 1st fruit from tha last:"+ fruits.removeFirst());
        System.out.println("last last fruit from the last:"+ fruits.removeLast());
        System.out.println("updated fruits list:" + fruits);


    }
}