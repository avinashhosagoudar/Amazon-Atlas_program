package DAY16;

import java.util.Arrays;

public class Task0014_Merge_Sort{


    public static int[] mergeSort(int[] a) {
        if (a.length <= 1) {
            return a;
        }

        int mid = a.length / 2;


        int[] left = Arrays.copyOfRange(a, 0, mid);
        int[] right = Arrays.copyOfRange(a, mid, a.length);


        left = mergeSort(left);
        right = mergeSort(right);


        return merge(left, right);
    }


    public static int[] merge(int[] a, int[] b) {
        int[] c = new int[a.length + b.length];
        int i = 0, j = 0, k = 0;


        while (i < a.length && j < b.length) {
            if (a[i] <= b[j]) {
                c[k++] = a[i++];
            } else {
                c[k++] = b[j++];
            }
        }


        while (i < a.length) {
            c[k++] = a[i++];
        }


        while (j < b.length) {
            c[k++] = b[j++];
        }

        return c;
    }


    public static void main(String[] args) {
        int[] arr = {8, 4, 5, 2, 9, 1};
        System.out.println("Before sorting: " + Arrays.toString(arr));
        int[] sorted = mergeSort(arr);
        System.out.println("After sorting: " + Arrays.toString(sorted));
    }
}