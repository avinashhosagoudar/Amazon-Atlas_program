package DAY09;//
//abstract class Persons {
//  private String name;
//  private int age;
//
//    // Constructor
//  public Persons(String name, int age) {
//       this.name = name;
//      this.age = age;
//  }
//
// // Getters and Setters
//    public String getName() { return name; }
// public void setName(String name) { this.name = name; }
//
// public int getAge() { return age; }
//  public void setAge(int age) { this.age = age; }
//
//  // Abstract method
// public abstract String toString();
//}
//
//class Customers extends Persons {
//  private String customerId;
// private String address;
//
//  public Customers(String name, int age, String customerId, String address) {
//     super(name, age);
//     this.customerId = customerId;
//      this.address = address;
//   }
//
//   public String getCustomerId() { return customerId; }
//  public void setCustomerId(String customerId) { this.customerId = customerId; }
//
//   public String getAddress() { return address; }
//    public void setAddress(String address) { this.address = address; }
//
//    @Override
//    public String toString() {
//       return "DAY8.Customer: Name=" + getName() + ", Age=" + getAge() +
//               ", DAY8.Customer ID=" + customerId + ", Address=" + address;
// }
//}
//
//class DAY8.Employee extends Person {
//    private String employeeId;
//  private double salary;
//
//    public DAY8.Employee(String name, int age, String employeeId, double salary) {
//     super(name, age);
//     this.employeeId = employeeId;
//       this.salary = salary;
//  }
//
//   public String getEmployeeId() { return employeeId; }
//   public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
//
//    public double getSalary() { return salary; }
//   public void setSalary(double salary) { this.salary = salary; }
//
//   @Override
// public String toString() {
//       return "DAY8.Employee: Name=" + getName() + ", Age=" + getAge() +
//               ", DAY8.Employee ID=" + employeeId + ", Salary=" + salary;
//  }
//}
//
//class Manager extends DAY8.Employee {
//   private String department;
//    private int teamSize;
//
//  public Manager(String name, int age, String employeeId, double salary, String department, int teamSize) {
//     super(name, age, employeeId, salary);
//       this.department = department;
//       this.teamSize = teamSize;
//    }
//////
//////    public String getDepartment() { return department; }
//////    public void setDepartment(String department) { this.department = department; }
//////
//////    public int getTeamSize() { return teamSize; }
//////    public void setTeamSize(int teamSize) { this.teamSize = teamSize; }
//////
//////    @Override
//////    public String toString() {
//////        return "Manager: Name=" + getName() + ", Age=" + getAge() +
//////                ", DAY8.Employee ID=" + getEmployeeId() + ", Salary=" + getSalary() +
//////                ", Department=" + department + ", Team Size=" + teamSize;
//////    }
//////}
//////
//////public class TaskC016 {
//////    public static void main(String[] args) {
//////        DAY8.Customer cust = new Customers("Alice", 25, "C101", "Bangalore");
//////        DAY8.Employee emp = new DAY8.Employee("Bob", 30, "E202", 55000.0);
//////        Manager mgr = new Manager("Charlie", 40, "M303", 85000.0, "IT", 10);
//////
////        System.out.println(cust);
//////        System.out.println(emp);
/////       System.out.println(mgr);
//    }
//}
//
