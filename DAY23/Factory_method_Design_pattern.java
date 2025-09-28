package DAY23;


interface Pizza {
    void preparation();
    void baking();
    void cutting();
    void boxing();
}

class PepperoniPizza implements Pizza {

    @Override
    public void preparation() {
        System.out.println("Preparing Pepperoni Pizza with cheese and toppings...");
    }

    @Override
    public void baking() {
        System.out.println("Baking Pepperoni Pizza at 180 degrees Celsius...");
    }

    @Override
    public void cutting() {
        System.out.println("Cutting Pepperoni Pizza into 8 slices...");
    }

    @Override
    public void boxing() {
        System.out.println("Boxing Pepperoni Pizza...");
    }
}

abstract class PizzaFactory {
    abstract Pizza createPizza();
}

class PepperoniPizzaFactory extends PizzaFactory {
    @Override
    Pizza createPizza() {
        return new PepperoniPizza();
    }
}

public class Factory_method_Design_pattern {
    public static void main(String[] args) {
        PizzaFactory pfobj = new PepperoniPizzaFactory();
        Pizza pobj = pfobj.createPizza();

        pobj.preparation();
        pobj.baking();
        pobj.cutting();
        pobj.boxing();
    }
}
