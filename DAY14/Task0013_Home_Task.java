package DAY14;

public class Task0013_Home_Task {
    public static boolean search(int[] arr , int index , int target){
        if (index < 0 ) return false;
        if (arr[index] == target) return true;

        return search(arr, index  - 1, target);
    }

    public static void main(String[] args) {
        int[] arr= {2,4,6,8,10};
        int target = 6;

        boolean found = search(arr, arr.length - 1 , target);

        System.out.println(found ? "Found" : "Not Found");


    }
}
