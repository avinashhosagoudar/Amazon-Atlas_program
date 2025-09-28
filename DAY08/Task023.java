package DAY08;

class Students {
    public int roll_no;
    public String name;

    Students(int Roll_no, String Name) {
        this.roll_no = Roll_no;
        this.name = Name;
    }


}

    public class Task023 {
        public static void main(String[] args) {
            // declares an Array of DAY8.Student
            Students[] arr = new Students[5];;



            // initialize the elements of the array
            arr[0] = new Students(1, "aman");
            arr[1] = new Students(2, "vaibhav");
            arr[2] = new Students(3, "shikar");
            arr[3] = new Students(4, "dharmesh");
            arr[4] = new Students(5, "mohit");

            // accessing the elements of the specified array
            for (int i = 0; i < arr.length; i++)
                System.out.println("Element at " + i + " : { "
                        + arr[i].roll_no + " "
                        + arr[i].name + " }");
        }
    }



