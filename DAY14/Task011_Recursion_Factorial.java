package DAY14;



public class Task011_Recursion_Factorial {

    public static long factorial(int n) {
        if (n == 0 || n == 1)
            return 1;
        return n * factorial(n - 1);


    }

    public static void main(String[] args) {
        int num = 5;
        System.out.println("factorail  " + num + " is: " + factorial(num));

    }
}
