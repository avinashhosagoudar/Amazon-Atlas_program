package DAY25.CompositeMethodDesignPattern;

public class CompositeMethodDP {

        public static void main(String[] args) {
            System.out.println("Composite Method DP - Structural DP");

            Company softwareCompany = new Software(1, "Software Department");
            Company hrDepartment = new HR(2, "HR Department");

            CompanyHead companyHead = new CompanyHead(3, "ABC Company");
            companyHead.addDepartment(softwareCompany);
            companyHead.addDepartment(hrDepartment);

            companyHead.displayName();
        }
    }

