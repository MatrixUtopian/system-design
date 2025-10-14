package org.example;

import ThreadLearningClass.CustomRejectHandler;
import ThreadLearningClass.CustomThreadFactory;
import ThreadLearningClass.ThreadBasic;

import java.util.concurrent.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
//        Main.threadBasics();
        Main.threadExecutor();
    }

    private static void threadBasics() {
        Thread thread1 = new Thread(() -> {
            System.out.println("Code Executed by thread: " + Thread.currentThread().getName());
        } );
        thread1.start();
        System.out.println("Main thread finished: " + Thread.currentThread().getName());
    }

    private static void threadExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 5,
                TimeUnit.MINUTES, new ArrayBlockingQueue<>(2), new CustomThreadFactory(), new CustomRejectHandler());

        for(int i = 0; i < 8; i++) {
            executor.submit( () -> {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Code Executed by thread: " + Thread.currentThread().getName());
            });
        }
        executor.shutdown();
    }
}
