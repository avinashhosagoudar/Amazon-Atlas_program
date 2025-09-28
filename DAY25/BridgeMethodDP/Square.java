package DAY25.BridgeMethodDP;

public class Square extends Shape {
    private int side;

    public Square(int side, ExcalidrawAPI excalidrawAPI) {
        super(excalidrawAPI);
        this.side = side;
    }

    @Override
    public void draw() {
        excalidrawAPI.drawSquare(side);
    }
}
