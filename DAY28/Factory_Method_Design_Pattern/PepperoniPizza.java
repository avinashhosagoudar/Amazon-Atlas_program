package DAY28.Factory_Method_Design_Pattern;

class PepperoniPizza implements Pizza {

    @Override
    public void preparation() {
        System.out.println("Preparing Pepperoni Pizza ");
    }

    @Override
    public void baking() {
        System.out.println("Baking Pepperoni Pizza ");
    }

    @Override
    public void cutting() {
        System.out.println("Cutting Pepperoni Pizza ");
    }

    @Override
    public void boxing() {
        System.out.println("Boxing Pepperoni Pizza...");
    }
}