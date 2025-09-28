package DAY11.MultiThreading;

class SharedResource {
    private boolean ready = false;

    synchronized void produce() {
        try {
            while (ready) {
                wait();
            }
            System.out.println("Producing...");
            ready = true;
            notify();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized void consume() {
        try {
            while (!ready) {
                wait();
            }
            System.out.println("Consuming...");
            ready = false;
            notify();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class TaskMT009 {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        Thread producer = new Thread(resource::produce); // :: ---  this is used to call the method reference in java , call the produce() method on the resourse object when the thread runs
        Thread consumer = new Thread(resource::consume);

        producer.start();
        consumer.start();
    }
}
