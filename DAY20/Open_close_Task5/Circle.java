package DAY20.Open_close_Task5;

public class Circle implements Shape {
    private int r;

    public Circle(int r) {
        this.r = r;
    }

    public double area() {
        return Math.PI * r * r;
    }
}
