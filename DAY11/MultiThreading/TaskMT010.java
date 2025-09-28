package DAY11.MultiThreading;
import java.util.stream.*;
class TaskMT010 {
    public static void main(String[] args) {
        Stream<String> stream
                = Stream.of("Heelo", "My",
                "name", "is",
                "Avinash ",
                "Hosagoudar");

        stream.forEach(System.out::println);
    }
}

