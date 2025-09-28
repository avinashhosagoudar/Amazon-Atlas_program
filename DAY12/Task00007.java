package DAY12;

import java.util.Arrays;

public class Task00007 {
    public static void main(String[] args) {
        int [] arr = {10,20,30,40};

        int n = arr.length;
        for (int i = 0 ; i < n /2 ; i++){
            int temp = arr[i];
            arr[i] = arr[n - 1 - i];
            arr[n - 1 - i] = temp;
        }
        System.out.println(Arrays.toString(arr));
    }
}
