package DAY07;

public class Task008 {
    public static void main(String[] args) {
        Customer obj = new Customer();
        obj.display();
        obj.accept();

    }



    static class Customer {
        void display() {
            System.out.println("Display method called");
        }

        void accept() {
            System.out.println("Display method called");
        }
    }
}
