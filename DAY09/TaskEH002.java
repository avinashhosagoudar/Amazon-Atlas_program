package DAY09;

public class TaskEH002 {
    public static void main(String[]args){
        try{
        int[] arr = {1,2,3,4};
        System.out.println(arr[5]);
    }
        catch (Exception e){
            System.out.println("something went wrong");

        }
        finally {
            System.out.println("finally ");
        }
        }
}