package DAY21;



// Loose Coupling Example

// Interface - acts like a contract
interface Person {
    void setRoll(int roll);
    int getRoll();
}

// Student class implements Person (loose coupling through interface)
class Student implements Person {
    private int roll_no = 0;

    @Override
    public int getRoll() {
        System.out.println("getRoll method");
        return roll_no;
    }

    @Override
    public void setRoll(int roll) {
        if (!(roll > 100)) { // roll number should not be greater than 100
            roll_no = roll;
        }
    }
}

public class Task007_loose_coupling {
    public static void main(String[] args) {
        // Loose coupling: we use the Person interface as reference type
        Person pobj = new Student(); // not tightly bound to Student
        pobj.setRoll(10);
        System.out.println("The roll no of student is " + pobj.getRoll());
    }
}
