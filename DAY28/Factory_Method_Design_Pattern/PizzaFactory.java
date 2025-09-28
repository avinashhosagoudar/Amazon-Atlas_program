package DAY28.Factory_Method_Design_Pattern;

abstract class PizzaFactory {
    abstract Pizza createPizza();
}

class PepperoniPizzaFactory extends PizzaFactory {
    @Override
    Pizza createPizza() {
        return new PepperoniPizza();
    }
}
