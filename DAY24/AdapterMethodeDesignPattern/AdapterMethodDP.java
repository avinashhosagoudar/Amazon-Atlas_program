package DAY24.AdapterMethodeDesignPattern;

public class AdapterMethodDP {

        public static void main(String[] args) {
            System.out.println("Adapter Method Design Pattern");

            // Create adapter that wraps ChargerAdaptee
            ILaptopTarget adapter = new PowerSocketAdapter(new ChargerAdaptee());

            // Pass adapter to laptop
            DellLaptop dell = new DellLaptop(adapter);

            dell.charge();
            dell.removeCharge();
        }
    }

