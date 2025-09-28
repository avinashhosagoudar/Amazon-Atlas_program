package DAY10.FileHandling;
import java.util.*;
import java.util.stream.*;

public class TaskStream015 {
    public static void main(String[] args) {
        List<Integer> numbers =  Arrays.asList(4,9,16,25,36);

        List<Double> sqroot = numbers.stream()
                .map(num -> Math.sqrt(num))
                        .collect(Collectors.toList());
                        sqroot.forEach(System.out::println);


    }





}
