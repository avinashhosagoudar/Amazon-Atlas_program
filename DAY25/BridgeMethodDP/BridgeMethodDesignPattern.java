package DAY25.BridgeMethodDP;

public class BridgeMethodDesignPattern {

        public static void main(String[] args) {
            System.out.println("Bridge Method Design Pattern - Structural DP!");

            ExcalidrawAPI frameAPI = new DrawingFrame();
            ExcalidrawAPI pictureAPI = new DrawingPicture();

            Shape square1 = new Square(5, frameAPI);
            Shape square2 = new Square(10, pictureAPI);

            square1.draw();
            square2.draw();
        }
    }
