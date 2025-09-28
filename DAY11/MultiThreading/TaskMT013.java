package DAY11.MultiThreading;

class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Code executed in a new thread via Runnable.");
    }
}


class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Code executed in a new thread via Thread extension.");
    }
}

public class TaskMT013 {
    public static void main(String[] args) {


        MyRunnable runnableInstance = new MyRunnable();
        MyThread threadInstance = new MyThread();

        Thread t1 = new Thread(runnableInstance);

        t1.start(); // For MyRunnable
        threadInstance.start(); // For MyThread
    }
}

