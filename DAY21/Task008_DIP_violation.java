package DAY21;

public class Task008_DIP_violation {
}

interface SwitchOnOff {
    void turnOn();
    void turnOff();
}
class LightBulb {
    void turnOn() {
        System.out.println("light turned on");
    }
    void turnOff() {
        System.out.println("light is off");
    }
}
class Switch {
    LightBulb lbulbobj;
    Switch(LightBulb lbulbobj) {
        this.lbulbobj = lbulbobj;
    }
    void operates(){
        lbulbobj.turnOn();
    }
    public static void main(String[] args) {
        LightBulb lbulbobj = new LightBulb();
        Switch Switchobj = new Switch(lbulbobj);
        Switchobj.operates();
    }
}