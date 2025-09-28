package DAY07;

import  java.util.Scanner;
public class Task007 {
    public static void main(String []args){
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your ID ");
        String id = sc.nextLine();
        System.out.println("Enter your password");
        String pass =sc.nextLine();

        System.out.println("your login id \t" + id + "and password\t" + pass);
        sc.close();
        
    }


}

