package DAY20;
import   DAY20.SRP_Task003.*;

public class Task003_SRP {
    public static void main(String[] args) {
        Book book = new Book("Effective Java", "Joshua Bloch", 800.0);

        BookFormatter formatter = new BookFormatter();
        DiscountCalculator calculator = new DiscountCalculator();

        System.out.println(formatter.formatBookDetails(book));
        System.out.println("Original Price: ₹" + book.getPrice());
        System.out.println("Discounted Price (15%): ₹" + calculator.calculateDiscountedPrice(book, 15));
    }
}