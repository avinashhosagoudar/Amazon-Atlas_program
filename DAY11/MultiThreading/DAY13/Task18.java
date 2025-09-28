package DAY13;

import java.util.HashMap;
import java.util.Map;

public class Task18 {
    public static void main(String [] args ){
        HashMap<String , Integer > demo = new HashMap<>(10);
        demo.put("Avinash" , 103);
        demo.put("Abhi", 103);
        demo.put("kartik" , 202);
        demo.put("kiran", 203);

        HashMap<String, Integer>  demo3 = new HashMap<String,Integer>(demo);


        System.out.println(demo);
        System.out.println("copy data below");

        System.out.println(demo3);



    }


}