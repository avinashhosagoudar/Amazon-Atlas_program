package DAY16;

import java.util.Arrays;

public class Task0017_Quick_Sort {

    // Main quick sort function
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // Partition the array and get pivot index
            int pivotIndex = partition(arr, low, high);

            // Recursively sort left and right halves
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    // Partition function
    public static int partition(int[] arr, int low, int high) {
        int pivot = arr[high]; // Choose last element as pivot
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                // Swap arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // Place pivot at the correct position
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    // Main method to test
    public static void main(String[] args) {
        int[] arr = {8, 4, 5, 2, 9, 1};
        System.out.println("Before sorting: " + Arrays.toString(arr));

        quickSort(arr, 0, arr.length - 1);

        System.out.println("After sorting: " + Arrays.toString(arr));
    }
}