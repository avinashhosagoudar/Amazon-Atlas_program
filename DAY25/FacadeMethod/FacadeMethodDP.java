package DAY25.FacadeMethod;

public class FacadeMethodDP {

        public static void main(String[] args) {
            System.out.println("Facade Method DP - Structural Design Pattern");
            MallFacade mallFacade = new MallFacade();
            mallFacade.getItems("Fruits");
            // mallFacade.transferMoney("123", "7777", 500.0);
            mallFacade.payBill("123", "billno 44", 500.0);
        }
    }
