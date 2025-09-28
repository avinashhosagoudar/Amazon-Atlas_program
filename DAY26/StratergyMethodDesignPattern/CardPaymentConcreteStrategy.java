package DAY26.StratergyMethodDesignPattern;

public class CardPaymentConcreteStrategy implements PaymentStrategy {
    public void process(double price) {
        System.out.println("payment processing using card");
    }
}
