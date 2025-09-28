package DAY16;

public class Task003_selection_sort {
    public static void main(String[] args) {
        int[] arr = {5, 3, 8, 2};
        int n = arr.length;


        for (int i = n - 1; i >= 1; i--) {
            int maxIndex = 0;

            for (int j = 1; j <= i; j++) {
                if (arr[j] > arr[maxIndex]) {
                    maxIndex = j;
                }
            }


            int temp = arr[i];
            arr[i] = arr[maxIndex];
            arr[maxIndex] = temp;
        }


        System.out.print("Sorted array: ");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
}