package DAY25.CommandDesignPattern;

public class Mom {
    Command command;
    public void setCommand(Command command) {
        this.command = command;
    }
    public void executeCommand() {
        this.command.doIt();
    }
}
