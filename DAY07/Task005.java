package DAY07;

public class Task005 {
    static int add(int x,int y){
        return x+y;
    }
    static double sub(int x,int y){
        return x-y;
    }
    static int mul(int x,int y){
        return x*y;
    }
    static double div(int x,int y){
        return x/y;
    }
    

    public static void main(String[]args){
        System.out.println("addition="+ add(5,10));
        System.out.println("Subtraction="+ sub(5,10));
        System.out.println("multiplication="+ mul(5,10));
        System.out.println("divison="+ div(5,10));

    }
}

