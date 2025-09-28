package DAY26.StratergyMethodDesignPattern;

public class StratergryDP {
    public static void main(String[] args) {
        System.out.println("Strategy Method Design PAttern - Behavioral DP");
        // Using Card Payment
        PaymentUsingStrategy cardPayment = new PaymentUsingStrategy(new CardPaymentConcreteStrategy());
        cardPayment.process(500.0);
/*
        // Using Cash Payment
        PaymentUsingStrategy cashPayment = new PaymentUsingStrategy(new CashPaymentConcreteStrategy());
        cashPayment.process(300.0);

        // Using UPI Payment
        PaymentUsingStrategy upiPayment = new PaymentUsingStrategy(new UPIPaymentConcreteStrategy());
        upiPayment.process(250.0);

 */
    }
}
