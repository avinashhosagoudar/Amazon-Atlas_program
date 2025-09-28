package DAY13;


import java.util.*;
public class Task015 {
    public static void main(String[] args)     {


        LinkedList<String> l = new LinkedList<>();


        l.add("Meher");
        l.add("Prasunamba");
        l.add(".MK");

        System.out.println(l);

        Spliterator<String> it = l.spliterator();


        System.out.println("Splitting the list:");
        it.forEachRemaining(System.out::println);
    }
}
