package DAY21;


interface ICalcArea {
    void calcArea();
    void calcPerimeter();
}

interface ICalcVolume {
    void calcVolume();
}


class Circle implements ICalcArea {
    @Override
    public void calcArea() {
        System.out.println("Area of Circle = π * r^2");
    }

    @Override
    public void calcPerimeter() {
        System.out.println("Perimeter of Circle = 2 * π * r");
    }
}


class Sphere implements ICalcArea, ICalcVolume {
    @Override
    public void calcArea() {
        System.out.println("Surface Area of Sphere = 4 * π * r^2");
    }

    @Override
    public void calcPerimeter() {
        // Not typically defined, but we can handle or leave it empty
        System.out.println("Perimeter not defined for Sphere");
    }

    @Override
    public void calcVolume() {
        System.out.println("Volume of Sphere = (4/3) * π * r^3");
    }
}


public class Task004_Interface_Seg_Impltn {
    public static void main(String[] args) {
        ICalcArea circle = new Circle();
        circle.calcArea();
        circle.calcPerimeter();

        Sphere sphere = new Sphere();
        sphere.calcArea();
        sphere.calcPerimeter();
        sphere.calcVolume();
    }
}
