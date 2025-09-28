package DAY25.CompositeMethodDesignPattern;

import java.util.ArrayList;
import java.util.List;

// Composite component
public class CompanyHead implements Company {
    private int id;
    private String name;
    private List<Company> subDepartments;

    public CompanyHead(int id, String name) {
        this.id = id;
        this.name = name;
        this.subDepartments = new ArrayList<>();
    }

    @Override
    public void displayName() {
        System.out.println("Company Head: " + name);
        subDepartments.forEach(Company::displayName);
    }

    public void addDepartment(Company company) {
        subDepartments.add(company);
    }

    public void removeDepartment(Company company) {
        subDepartments.remove(company);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Company> getSubDepartments() {
        return subDepartments;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubDepartments(List<Company> subDepartments) {
        this.subDepartments = subDepartments;
    }
}