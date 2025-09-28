package DAY16;

 class RecLoop {

    public int calc(int n) {
        if (n == 0) return 0;      // base case
        return n + calc(n - 1);    // recursive case
    }
}
public class Task0011_recursive_code {
    public static void main(String[] args) {
        RecLoop r = new RecLoop();
        System.out.println(r.calc(5));  // Output: 15 (5+4+3+2+1+0)
    }
}

