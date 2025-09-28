package DAY25.CompositeMethodDesignPattern;

public class HR implements Company {
    private int id;
    private String name;

    public HR(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void displayName() {
        System.out.println("HR Department: " + name);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}