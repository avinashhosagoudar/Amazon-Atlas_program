package DAY07;

public class Task019 {
    public  static void main(String[]args){
        Task016.Element e1 = Task016.Element.valueOfLabel("Hydrogen");
        System.out.println("By Label(Hydrogen):" + e1);

        Task016.Element e2 = Task016.Element.valueOfAtomicNumber(2);
        System.out.println("By Atomic Number(2):" + e2);

        Task016.Element e3 = Task016.Element.valueOfAtomicWeight(20.180f);
        System.out.println("By Atomic weight(20.180f):" + e3);
    }
}
