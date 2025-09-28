package DAY24.BuilderMethodDesignPattern;


public class LaptopDirector {
    private LaptopBuilder laptopBuilder;

    public LaptopDirector(LaptopBuilder laptopBuilder) {
        this.laptopBuilder = laptopBuilder;
    }

    public Laptop constructLaptop() {
        return laptopBuilder
                .buildMemory(16)   // example: 16 GB RAM
                .buildStorage(512) // example: 512 GB SSD
                .build();
    }

}
