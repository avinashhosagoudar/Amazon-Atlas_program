package DAY14;

public class Task012_Recursion_Fibonassi {
    public static int fibonacci(int n){
        if (n <= 1)
            return n;
        return fibonacci(n - 1 )+ fibonacci(n - 2);
    }

    public static void main(String[] args) {
        int n = 10 ;
        System.out.println("Fibonacci :");
        for (int i = 0 ; i < n ; i ++){
            System.out.println((fibonacci(i)));
        }
    }
}
