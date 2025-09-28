package DAY07;

import java.util.Scanner;
class Task012 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);

        String loginid = "Avinash";
        String pwd = "12345678";
        int Count = 0;

        while(loginid.equals("Avinash") && pwd.equals("12345678")){
            System.out.println(" you have logged in for   "  + Count++);
            System.out.println("enter ur login id and password");
            loginid = sc.nextLine();
            pwd = sc.nextLine();

        }
        Count++;





    }
}
