package DAY10.FileHandling;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskStreams014 {
     static   ArrayList<String> a = new ArrayList<>();
      public  static List getValues(){

        a.add("Avinash , Patil");
        a.add("Abhishek , Hosagoudar");
        a.add("Avinash , Hosagoudar ");
        a.add("kartik , patil");

        return a;
    }

    public static void main(String[] args) {
        List<String> names = getValues();
        names.stream()
                .filter((p) -> p.startsWith("Avinash")) // it will search for the starting name with Avinash and pass the value to p
                .map((p) -> p.toUpperCase(Locale.ROOT)) // here will to uppercase and pass into p again
                .sorted().                                    // sort to alphabet wise
                forEach((p) -> System.out.println(p));  // it will print final value
    }
}
