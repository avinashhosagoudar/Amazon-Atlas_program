package DAY16;
import java.io.*;
public class Task009_Insertion_sort {
    public static void main(String args[]) {
        int n = 5;
        int[] arr = {67, 44, 82, 17, 20};

        for(int i = 1; i<n; i++) {
            int key = arr[i];
            int j = i;
            while(j > 0 && arr[j-1]>key) {
                arr[j] = arr[j-1];
                j--;
            }
            arr[j] = key;
        }
        System.out.print(" After Sorting: ");
        for(int i = 0; i<n; i++)
            System.out.print(arr[i] + " ");
        System.out.println();
    }
}