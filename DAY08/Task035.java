package DAY08;

public class Task035 {
    static void add(char x, char y) {
        System.out.println("x =\t" + x +'\t'+ "y=" + y);
    }

    static void add(int x, int y) {
        System.out.println("x and y=" + (x + y));
    }

    public static void main(String[] args) {
        add('a', 'b');
        add(100, 100);
    }
}

