package DAY10.FileHandling;
import java.util.stream.Stream;

public class TaskStream019 {
    public static void main(String[] args) {
        Stream<Integer> nums = Stream
                .iterate(1, n -> n+1)
                .limit(20);

        Stream<Integer> SkipNums = nums.skip(15);

        SkipNums.forEach(System.out::println);


    }
}
