package DAY11.MultiThreading;

class RunnableDemo implements Runnable {   // runnable is a predefined interface
    private Thread t;
    private String car;
    RunnableDemo( String name){
        car = name;
        System.out.println("Creating " + car );
    }
    public void run() {
        System.out.println("Running " + car );
        try {
            for(int i = 4; i > 0; i--) {
                System.out.println("Thread: " + car + ", " + i);
// Let the thread sleep for a while.
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread " + car + " interrupted.");
        }
        System.out.println("Thread " + car + " exiting.");

    }
    public void THstart ()
    {
        System.out.println("Starting " + car );
        if (t == null)
        {
            t = new Thread (this, car);
            t.start();// start() is predifened method so it will redirect to run() 10th line
        }
    }
}
public class TaskMT001 {
    public static void main(String args[]) {
        RunnableDemo R1 = new RunnableDemo( "Thread-1");
        R1.THstart();
        RunnableDemo R2 = new RunnableDemo( "Thread-2");
        R2.THstart();
    }
}
