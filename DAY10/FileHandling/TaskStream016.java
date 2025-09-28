package DAY10.FileHandling;
import java.util.*;
import java.util.stream.Collectors;

public class TaskStream016 {
    public static void main(String[] args) {
        List<Integer> numbers =  Arrays.asList(4,9,16,25,36);

        List<Integer> oddnumbers = numbers.stream()
                .filter(n -> n % 2  != 0)
                        .collect(Collectors.toList());
        System.out.println(oddnumbers);

    }
}
