package DAY10.FileHandling;
import java.io.*;
import java.util.*;
public class TaskFH003
{
    public static void main(String args[]) {
        FileOutputStream outfile = null;
         // to input string from command line
        Scanner sc=new Scanner(System.in);
        String s=sc.nextLine();
        // converting string into byte
        byte b1[] = s.getBytes();

        try
        {
            outfile = new FileOutputStream("FileName02.txt");  // it will check if the the file is present it will overide the content with new data otherwise it will create new file
            outfile.write(b1);
        }
        catch(IOException e)
        {
            System.out.println(e);
            System.exit(-1);
        }
        System.out.println("Write Byte");
        System.out.println("Thank You...!!!");
    }
}
