package DAY21;/*
package DAY21;

// Violating ISP: Interface forcing unrelated methods
interface ICalcShapesArea {
    void calcArea();
    void calcVolume();
}

// Circle is a 2D shape, so it doesn't need calcVolume
class Circle implements ICalcShapesArea {
    @Override
    public void calcArea() {
        System.out.println("Area of Circle = π * r^2");
    }

    @Override
    public void calcVolume() {
        // Forced to implement even if it's not applicable
        System.out.println("Volume not applicable for Circle");
    }
}

// Sphere is a 3D shape, so both area and volume apply
class Sphere implements ICalcShapesArea {
    @Override
    public void calcArea() {
        System.out.println("Surface Area of Sphere = 4 * π * r^2");
    }

    @Override
    public void calcVolume() {
        System.out.println("Volume of Sphere = (4/3) * π * r^3");
    }
}


public class Task003_Interface_Seg_Violation {
    public static void main(String[] args) {
        ICalcShapesArea circle = new Circle();
        circle.calcArea();
        circle.calcVolume(); // Irrelevant call

        ICalcShapesArea sphere = new Sphere();
        sphere.calcArea();
        sphere.calcVolume();
    }
}
*/
