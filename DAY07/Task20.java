
package DAY07;

public class Task20 {
    public static void main(String[]args){
        char [] names = {'A','V','I','N','S','H'};
        


        System.out.println(names.length);

        for (int i = 0; i< names.length ; i++){
            System.out.print(names[i]);
        }
        System.out.println(" \n ");

        System.out.println(names[2]);
        System.out.println(" \n ");
        names[2] = 'N';
        System.out.println(names[2]);

        char [] shallowCopy = names ;
        shallowCopy[0] = 'C';

        System.out.println(names[0]);

        char [] deepCopy = names.clone();
        deepCopy[0] = 'H';
        System.out.println(deepCopy[0]);



    }

}


