package DAY24.AdapterMethodeDesignPattern;

// Client-facing class
public class DellLaptop {
    private ILaptopTarget powerAdapter;

    public DellLaptop(ILaptopTarget powerAdapter) {
        this.powerAdapter = powerAdapter;
    }

    public void charge() {
        powerAdapter.charge();
    }

    public void removeCharge() {
        powerAdapter.removeCharge();
    }
}
