package DAY24.AbstractFactoryDesign;

public class Apple {
    public static Mobile getMobile(String model) {
        if (model.equalsIgnoreCase("iphone16")) {
            return new Mobile("Apple iPhone 16");
        }
        return new NoMobile();
    }
}
