package DAY24.AbstractFactoryDesign;

public class MobileStore {
    private MobileStore() {
        System.out.println("Hello, welcome to the world of Mobile");
    }

    public static Mobile getMobile(String brand, String model) {
        if (brand.equalsIgnoreCase("Apple")) {
            System.out.println("Here are your Apple Models");
            return Apple.getMobile(model);
        }
        // Example for Samsung
        // else if (brand.equalsIgnoreCase("Samsung")) {
        //     return Samsung.getMobile(model);
        // }

        return new NoMobile();
    }
}
