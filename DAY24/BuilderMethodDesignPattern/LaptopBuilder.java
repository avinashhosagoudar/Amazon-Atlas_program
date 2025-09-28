package DAY24.BuilderMethodDesignPattern;

public interface LaptopBuilder {
    LaptopBuilder buildMemory(int memory);
    LaptopBuilder buildStorage(int storage);
    Laptop build();

}

