package ThreadLearningClass;

public class ThreadBasic implements Runnable  {

    @Override
    public void run() {
        System.out.println("Code Executed by thread: " + Thread.currentThread().getName());
    }
}
