package DAY18;

import java.util.*;

class Task017_radix_Sort{


    static int getMax(int[] arr) {
        int max = arr[0];
        for (int num : arr) {
            if (num > max)
                max = num;
        }
        return max;
    }


    static void countingSort(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n]; // output array
        int[] count = new int[10]; // since digits range from 0 to 9


        for (int i = 0; i < n; i++)
            count[(arr[i] / exp) % 10]++;


        for (int i = 1; i < 10; i++)
            count[i] += count[i - 1];


        for (int i = n - 1; i >= 0; i--) {
            int index = (arr[i] / exp) % 10;
            output[count[index] - 1] = arr[i];
            count[index]--;
        }


        for (int i = 0; i < n; i++)
            arr[i] = output[i];
    }


    static void radixSort(int[] arr) {
        int max = getMax(arr);


        for (int exp = 1; max / exp > 0; exp *= 10)
            countingSort(arr, exp);
    }


    static void printArray(int[] arr) {
        for (int num : arr)
            System.out.print(num + " ");
        System.out.println();
    }

    public static void main(String[] args) {
        int[] arr = {170, 45, 75, 90, 802, 24, 2, 66};

        System.out.println("Original array:");
        printArray(arr);

        radixSort(arr);

        System.out.println("Sorted array:");
        printArray(arr);
    }
}