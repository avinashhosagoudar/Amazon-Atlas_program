package DAY24.AdapterMethodeDesignPattern;

// Concrete Adaptee
public class ChargerAdaptee implements IChargerAdaptee {

    public ChargerAdaptee() {}

    @Override
    public void charge() {
        System.out.println("Charging my laptop...");
    }

    @Override
    public void removeCharge() {
        System.out.println("Laptop is not charging.");
    }
}