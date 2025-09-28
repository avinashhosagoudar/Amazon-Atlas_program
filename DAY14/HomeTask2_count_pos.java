package DAY14;

public class HomeTask2_count_pos {
    public static int sumDigits(int n ){
        if (n ==  0)
            return 0;
        return (n % 10) + sumDigits(n / 10);
    }

    public static void main(String[] args) {
        int number = 12345678;
        System.out.println("sum of digit: " + sumDigits(number));
    }
}
