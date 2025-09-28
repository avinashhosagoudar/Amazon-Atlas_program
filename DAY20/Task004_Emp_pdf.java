package DAY20;
import DAY20.Employees_SRP_Task04.*;

public class Task004_Emp_pdf {
        public static void main(String[] args) {
            Employee emp = new Employee("John Doe", "john.doe@example.com", 75000);

            PdfReportGenerator reportGenerator = new PdfReportGenerator();
            EmailService emailService = new EmailService();

            reportGenerator.generate(emp);
            emailService.send(emp);
        }
    }

