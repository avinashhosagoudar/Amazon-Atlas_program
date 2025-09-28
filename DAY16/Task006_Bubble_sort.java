package DAY16;

import java.io.*;
import java.util.*;
public class Task006_Bubble_sort {
    public static void main(String args[]) {
        int n = 5;
        int[] arr = {67, 44, 82, 17, 20};

        for(int i = 0; i<n; i++) {

            for(int j = 0; j<n-i-1; j++) {
                if(arr[j] > arr[j+1]) { //when the current item is bigger than next then  swaping it
                    int temp;
                    temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;

                }
            }
        }
        System.out.print("Ater Sorting : ");
        for(int i = 0; i<n; i++)
            System.out.print(arr[i] + " ");
        System.out.println();
    }
}
