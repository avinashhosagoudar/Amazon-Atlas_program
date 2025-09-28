package DAY20.SRP_Task003;

public class BookFormatter {
    public String formatTitle(Book book) {
        return "Title: " + book.getTitle().toUpperCase();
    }

    public String formatAuthor(Book book) {
        return "Author: " + book.getAuthor();
    }

    public String formatBookDetails(Book book) {
        return formatTitle(book) + "\n" + formatAuthor(book);
    }
}