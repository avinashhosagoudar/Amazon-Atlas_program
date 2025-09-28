package DAY24.AdapterMethodeDesignPattern;

// Adapter class: implements target, uses adaptee
public class PowerSocketAdapter implements ILaptopTarget {
    private IChargerAdaptee charger;

    public PowerSocketAdapter(IChargerAdaptee charger) {
        this.charger = charger;
    }

    @Override
    public void charge() {
        charger.charge();
    }

    @Override
    public void removeCharge() {
        charger.removeCharge();
    }
}
