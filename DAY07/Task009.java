package DAY07;

import java.util.Scanner;
public class Task009 {

        public static void main(String[] args) {
                
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter value of a = ");
            int a = sc.nextInt();
            System.out.println("Enter value of b = ");
            int b = sc.nextInt();

            if (a > b){
                System.out.println("a is greater");
            }
            else{
                System.out.println("b is greater than a");
            }



        }
    }



