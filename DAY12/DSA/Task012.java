package DAY12.DSA;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.*;

public class Task012 {
    public static void main(String[] args) {
        Hashtable<String, Integer> ht = new Hashtable<>();
        ht.put("Anitha", 101);
        ht.put("Kavitha", 102);
        ht.put("Meera", 103);
        //ht.put(null, 103);   // Hashtable will wont take null value and null key  where HashMap will allow  1 null key and mulitple null values
        // use  get method of Ht
        for (Map.Entry<String, Integer> e : ht.entrySet())
            System.out.println(e.getKey() + " " + e.getValue());
        System.out.println(ht.get("Anitha"));
        System.out.println(ht.get("Kavitha"));
        System.out.println(ht.get("Meera"));
    }

}
