package DAY13;

import java.util.LinkedList;




public class Task010 {
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

        int i = 0;
        while (i < fruits.size()){
            System.out.println(fruits.get(i));
            i++;
        }



    }
}
