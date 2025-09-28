package DAY18;
import java.util.Scanner;

public class Task03 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a number: ");
        int num = sc.nextInt();

        String numStr = Integer.toString(Math.abs(num)); //Math.abs helps to remove negative sign

        int count = numStr.length();

        System.out.println("It's a " + count + " digit number");

        sc.close();
    }
}


