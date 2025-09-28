package DAY20.Open_close_Task5;

public class AreaComparator {
    public int compareArea(Shape s1, Shape s2) {
        return Double.compare(s1.area(), s2.area());
    }
}