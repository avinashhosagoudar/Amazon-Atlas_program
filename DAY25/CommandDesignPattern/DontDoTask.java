package DAY25.CommandDesignPattern;

public class DontDoTask implements Command{
    private Task task;

    public  DontDoTask(Task task) {
        this.task = task;
    }
    public void doIt() {
        this.task.dont();
    }
}