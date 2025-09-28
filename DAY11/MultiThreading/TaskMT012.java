package DAY11.MultiThreading;



public class TaskMT012 extends  Thread {
    public void run(){
        System.out.println("thread started.");
    }
    public static void main(String args[]){

        TaskMT012 t1 = new TaskMT012();
        Thread t2 = new Thread(t1);
        t2.start();
    }
}
