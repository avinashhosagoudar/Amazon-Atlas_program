package DAY12.DSA;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.*;

public class Task013 {
    public static void main(String[] args) {
        HashMap<String, Integer> ht = new HashMap<>();
        ht.put("Anitha", 102);
        ht.put("Kavitha", 101);
        ht.put("Meera", 103);
        ht.put(null, 103);
        // use  get method of Ht
        for (Map.Entry<String, Integer> e : ht.entrySet())
            System.out.println(e.getKey() + " " + e.getValue());
        System.out.println(ht.get("Anitha"));
        System.out.println(ht.get("Kavitha"));
        System.out.println(ht.get("Meera"));
    }

}
