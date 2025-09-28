package DAY08;

class Employee{
    private int pwd;
    protected int salary;
    public int empid;

}
class Hr extends Employee{
    Hr() {
        super();
        //super.pwd = 1254;   // it is private so we cant access without using getter and setter
        super.salary = 25000; // it is protected within the same progarm we can access the va
        super.empid = 2345;
       // System.out.println(empid());
        System.out.println(salary);
        System.out.println(empid);
    }
}

public class Task037 {
    public static void main(String[]args){
        Hr h = new Hr();
        //System.out.println(h.empid());
        System.out.println(h.salary);
        System.out.println(h.empid);

    }

}


/*Class DAY8.Employee{
Private int pwd;
Protected int Salary;
Public int empid:



}
Class DAY8.Hr extends DAY8.Employee {
    super.pwd = 1254; //===============>  ??????
    super.Salary = 50000; //==================>  ?
    Super.empid = 10001; // ======================>?
}

Class Driver{
psvm(){
    Access all variables…????
    super.pwd = 1254; //===============>  ??????
    super.Salary = 50000; //==================>  ?
    Super.empid = 10001; // ======================>?
}
}*/

