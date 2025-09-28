package DAY20.SRP_Task003;

public class DiscountCalculator {
    public double calculateDiscountedPrice(Book book, double discountPercentage) {
        return book.getPrice() * (1 - discountPercentage / 100.0);
    }
}
