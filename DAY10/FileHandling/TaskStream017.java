package DAY10.FileHandling;
import java.util.*;
import java.util.stream.Collectors;

public class TaskStream017 {
    public static void main(String[] args) {
        List<Integer> numbers =  Arrays.asList(4,9,16,25,36,16,25);

        List<Integer> RemovDups= numbers.stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.println(RemovDups);




    }
}