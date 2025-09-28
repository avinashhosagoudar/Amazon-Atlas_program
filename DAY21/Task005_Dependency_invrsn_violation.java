package DAY21;/*
package DAY21;

// Low-level module
class Clothes {
    void seeRating() {
        System.out.println("Clothes rating shown.");
    }

    void viewSample() {
        System.out.println("Clothes sample displayed.");
    }
}


class Books {
    void seeRating() {
        System.out.println("Books rating shown.");
    }

    void readSample() {
        System.out.println("Books sample read.");
    }
}

// High-level module
class Cupboard {

    Clothes clothes;
    Books books;

    void addClothes(Clothes clothes) {
        this.clothes = clothes;
        System.out.println("Clothes added to cupboard.");
    }

    void customizeClothes() {
        clothes.seeRating();
        clothes.viewSample();
    }

    void addBooks(Books books) {
        this.books = books;
        System.out.println("Books added to cupboard.");
    }

    void customizeBooks() {
        books.seeRating();
        books.readSample();
    }
}

public class Task005_Dependency_invrsn_violation {
    public static void main(String[] args) {

        Clothes clothes = new Clothes();
        Books books = new Books();


        Cupboard cupboard = new Cupboard();


        cupboard.addClothes(clothes);
        cupboard.customizeClothes();

        System.out.println();


        cupboard.addBooks(books);
        cupboard.customizeBooks();
    }
}
*/
