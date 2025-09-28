package DAY10.FileHandling;

public class TaskFH010 {
    public static void main(String[]args){
        String input = "Avii";
        char [] chars = input.toCharArray();

        for (int i = chars.length - 1 ; i >= 0 ; i-- ){
            System.out.print(chars[i]);
        }
    }
}
