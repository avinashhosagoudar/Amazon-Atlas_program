package DAY21;


 interface IProduct {
    void SeeReviews();
    void getSample();
}

// Low-level module
 class Clothes implements IProduct {
    @Override
    public void SeeReviews() {
        System.out.println("Clothes rating shown.");
    }

    @Override
    public void getSample() {
        System.out.println("Clothes sample displayed.");
    }
}

// Low-level module
 class Books implements IProduct {
    @Override
    public void SeeReviews() {
        System.out.println("Books rating shown.");
    }

    @Override
    public void getSample() {
        System.out.println("Books sample read.");
    }
}

// High-level module
 class Cupboard {
    private IProduct product;

    public void addProduct(IProduct product) {
        this.product = product;
        System.out.println("Product added to cupboard.");
    }

    public void customizeProduct() {
        product.SeeReviews();
        product.getSample();
    }
}


public class Task006_Dependency_invrsn_Impltn{
    public static void main(String[] args) {
        IProduct clothes = new Clothes();
        IProduct books = new Books();

        Cupboard cupboard = new Cupboard();

       
        cupboard.addProduct(clothes);
        cupboard.customizeProduct();

        System.out.println();


        cupboard.addProduct(books);
        cupboard.customizeProduct();
    }
}
