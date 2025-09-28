package DAY08;

interface testInterface {
    // public, static and final
    final int tax = 10;
    // public and abstract
    void display();
}
// Class implementing interface
class TestClass implements testInterface {
    public void display(){
        System.out.println("Myclass");
    }
}
class Task041
//Driver Code Starts
{
    public static void main(String[] args)
    {
        TestClass t = new TestClass();
        t.display();
        System.out.println(t.tax);
    }
}
