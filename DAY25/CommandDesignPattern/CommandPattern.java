package DAY25.CommandDesignPattern;

public class CommandPattern {

        public static void main(String[] args) {
            System.out.println("Command Pattern - Behavioural DP");
            Task task = new Task();
            Mom remote = new Mom();

            Command onCommand = new DoTask(task);
            Command offCommand = new DontDoTask(task);

            remote.setCommand(onCommand); //ac
            remote.executeCommand();   //button press

            remote.setCommand(offCommand); //ac
            remote.executeCommand(); //again clicking the button..

        }
    }
