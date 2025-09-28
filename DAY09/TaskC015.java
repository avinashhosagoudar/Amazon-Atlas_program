package DAY09;

class OuterClass4 {
    int x = 10;
    static class InnerClass4 {
        static int y = 15;
    }
}
public class TaskC015 {
    public static void main(String[] args) {
        //OuterClass4.InnerClass4 myInner4 = new OuterClass4.InnerClass4();
        //OuterClass4 myOuter4 = new OuterClass4();

        System.out.println(OuterClass4.InnerClass4.y);

    }
}