package DAY24.AbstractFactoryDesign;

public class ClientAbstractFactory {
    public static void main(String[] args) {
        Mobile Mobj = MobileStore.getMobile("Apple", "iphone16");
        Mobj.getDesc();
        System.out.println("...");
    }
}
