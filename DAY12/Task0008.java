package DAY12;
import  java.util.*;
import java.util.Scanner;

public class Task0008{
    public static void main(String[]args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a string:");
        String input = sc.nextLine();
        char [] chars = input.toCharArray();

        for (int i = chars.length - 1 ; i >= 0 ; i-- ){
            System.out.print(chars[i]);
        }
    }
}
