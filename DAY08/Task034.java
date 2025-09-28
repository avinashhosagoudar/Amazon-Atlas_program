package DAY08;

public class Task034 {
    void add(int x , int y) {
        System.out.println("X+Y: \t"+ (x+y));
    }
    void add(int x , int y , int z){
        System.out.println("X+Y+z: \t"+ (x+y+z));
    }
    public static void main(String[]args){
        Task034 obj = new Task034();
        obj.add(10,20);
        obj.add(10,20,30);
    }
}
