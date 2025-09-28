package DAY13;

import java.util.LinkedList;



public class Task012 {
    public static void main(String[] args) {


        LinkedList<String> fruits = new LinkedList<>();
        fruits.add("mango");
        fruits.add("banana");
        fruits.add("grapes");

        System.out.println("fruits:" +fruits);

        fruits.push("pinaple");
        fruits.push("oarnge");

       /* fruits.addFirst("pinaple");
        fruits.addLast("orange");*/

        System.out.println("updated fruits list:" + fruits);
        fruits.pop();
        System.out.println("after using pop method:"+ fruits);
        /*System.out.println("last fruit from the last:"+ fruits.getLast());*/

    }
}
