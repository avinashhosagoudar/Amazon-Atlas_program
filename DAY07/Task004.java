package DAY07;

public class Task004 {
    public static void main(String[]args){
        int a=2;
        int b=5;
        System.out.println("before:" + "a="+ a + "b=" +b );

        int temp = a;
        a = b;
        b = temp;
        System.out.println("after:" + "a="+ a + "b=" +b );

    }
}
