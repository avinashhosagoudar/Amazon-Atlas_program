package DAY21;


abstract class BirdsThatFly {
    abstract void fly();
}


abstract class BirdsThatDontFly {
    abstract void speciality();
}


class Eagle extends BirdsThatFly {
    @Override
    public void fly() {
        System.out.println("Eagles fly high");
    }
}


class Ostrich extends BirdsThatDontFly {
    @Override
    public void speciality() {
        System.out.println("Ostrich lays big eggs and runs fast");
    }
}


public class Task002_Lisko_Impltn {
    public static void main(String[] args) {
        BirdsThatFly eagle = new Eagle();
        eagle.fly();

        BirdsThatDontFly ostrich = new Ostrich();
        ostrich.speciality();
    }
}
