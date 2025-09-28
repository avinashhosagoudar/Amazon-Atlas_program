package DAY25.BridgeMethodDP;

public abstract class Shape {
    protected ExcalidrawAPI excalidrawAPI;

    protected Shape(ExcalidrawAPI excalidrawAPI) {
        this.excalidrawAPI = excalidrawAPI;
    }

    public abstract void draw();
}
