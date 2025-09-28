package DAY21;/*
package DAY21;

abstract class Bird {
    abstract void fly();
}

class Eagle extends Bird {
    @Override
    public void fly() {
        System.out.println("Eagles fly high");
    }
}

class Ostrich extends Bird {
    @Override
    public void fly() { // This is a dummy/incorrect implementation
        System.out.println("I can't fly, but I lay big eggs");
    }
}

public class Task001_Lisko_Violation {
    public static void main(String[] args) {
        Bird eagle = new Eagle();
        Bird ostrich = new Ostrich();

        eagle.fly();   //Valid
        ostrich.fly(); //  Violates LSP: Ostrich shouldn't be expected to fly
    }
}*/
