package DAY08;

// Abstract class
abstract class Employees {
    private String name;
    private String address;
    private int number;

    public Employees(String name, String address, int number) {
        System.out.println("Constructing an DAY8.Employee");
        this.name = name;
        this.address = address;
        this.number = number;
    }

    public double computePay() {
        System.out.println("Inside DAY8.Employee computePay");
        return 0.0;
    }

    public void mailCheck() {
        System.out.println("Mailing a check to " + this.name + " " + this.address);
    }

    public String toString() {
        return name + " " + address + " " + number;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String newAddress) {
        address = newAddress;
    }

    public int getNumber() {
        return number;
    }
}

// Concrete subclass
class SalariedEmployee extends Employees {
    public SalariedEmployee(String name, String address, int number) {
        super(name, address, number);
    }

    // Optionally override methods here
}

public class Task039 {

    public static void main(String[] args) {
        // Create an object of the subclass
        SalariedEmployee e = new SalariedEmployee("George W.", "Houston, TX", 43);
        System.out.println("\nCall mailCheck using DAY8.Employee reference--");
        e.mailCheck();
    }
}

