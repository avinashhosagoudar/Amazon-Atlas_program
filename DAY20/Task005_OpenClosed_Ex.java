package DAY20;
import DAY20.Open_close_Task5.*;

public class Task005_OpenClosed_Ex {
    public static void main(String[] args) {
        Shape square = new Square(5);
        Shape circle = new Circle(3);

        AreaComparator comparator = new AreaComparator();
        int result = comparator.compareArea(square, circle);

        if (result > 0)
            System.out.println("Square has larger area.");
        else if (result < 0)
            System.out.println("Circle has larger area.");
        else
            System.out.println("Both have equal area.");
    }
}
