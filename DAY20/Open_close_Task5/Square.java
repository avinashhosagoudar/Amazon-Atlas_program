package DAY20.Open_close_Task5;

public class Square implements Shape {
    private int height;

    public Square(int height) {
        this.height = height;
    }

    public double area() {
        return height * height;
    }
}
