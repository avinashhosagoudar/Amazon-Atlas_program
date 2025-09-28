package DAY12.DSA;
import java.util.*;

public class Task0016 {
    public static void main(String[] args) {
        HashMap<String, Integer> ht = new HashMap<>();
        ht.put("Kartik", 102);
        ht.put("Abhishek", 101);
        ht.put(null, 103);
        ht.put(null, 104); // it will take only one null value
        // use  get method of Ht
        for (Map.Entry<String, Integer> e : ht.entrySet())
            System.out.println(e.getKey() + " " + e.getValue());
        /*System.out.println(ht.get("Anitha"));
        System.out.println(ht.get("Kavitha"));
        System.out.println(ht.get("Meera"));*/

        Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());
        syncMap.put("Sundar", 401);
        syncMap.put("Mandar", null);
        syncMap.put(null, 402);
        syncMap.put("Arush", 403);

        System.out.println("\nusing Synchronized method:\n");
        for (Map.Entry<String, Integer> b : syncMap.entrySet())
            System.out.println(b.getKey() + " " + b.getValue());

    }
}
