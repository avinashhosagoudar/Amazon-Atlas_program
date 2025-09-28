package DAY20;

import java.io.FileWriter;
import java.io.IOException;

public class ManagingFiles {
    public void saveData(Customer customer) {
        try {
            FileWriter fw = new FileWriter(customer.getName().trim() + ".txt");
            fw.write("The customer name is: " + customer.getName() + "\t");
            fw.write("The customer ID is: " + customer.getCustID() + "\t");
            fw.close(); // Important to close the writer
            System.out.println("The data is saved in the file with your name.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

