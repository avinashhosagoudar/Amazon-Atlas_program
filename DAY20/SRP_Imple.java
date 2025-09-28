package DAY20;

public class SRP_Imple {
    public static void main(String[] args) {
        Customer cobj = new Customer("Prasunamba", "C001");
        ManagingFiles mobj = new ManagingFiles();
        mobj.saveData(cobj);
    }
}
