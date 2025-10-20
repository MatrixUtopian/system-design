package org.example;

import ThreadLearningClass.CustomRejectHandler;
import ThreadLearningClass.CustomThreadFactory;
import ThreadLearningClass.ThreadBasic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    static void main() {
//        Main.threadBasics();
//        Main.threadExecutor();
//        Main.threadExecutorFuture();
//        Main.threadExecutorCallable();
        Main.threadSupplyAsync();
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

    private static void threadExecutorFuture() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 1,
                TimeUnit.MINUTES, new ArrayBlockingQueue<>(2), new CustomThreadFactory(), new CustomRejectHandler());
        Future<?> future =  executor.submit( () -> {
            try {
                Thread.sleep(7000);
                System.out.println("Code Executed by thread: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println("Is done: " + future.isDone());

        try {
            future.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            System.out.println("Timeout Exception");
        } catch (Exception e) {

        }


        try {
            future.get();
        } catch (Exception e) {
            System.out.println("Error while waiting");
        }

        System.out.println("Is done: " + future.isDone());
        System.out.println("Is cancelled: " + future.isCancelled());
        executor.shutdown();
    }

    private static void threadExecutorCallable() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 1,
                TimeUnit.MINUTES, new ArrayBlockingQueue<>(2), new CustomThreadFactory(), new CustomRejectHandler());
        // Runnable doesn't return anything
        Future<?> future =  executor.submit( () -> {
                    System.out.println("Code Executed by thread: " + Thread.currentThread().getName());
                });
        // Callable returns a value
        Future<Integer> callable =  executor.submit( () -> {
            System.out.println("Code Executed by thread: " + Thread.currentThread().getName());
            return 10;
        });
        System.out.println("Callable: " + callable.toString());
        executor.shutdown();
    }

    private static void threadSupplyAsync() {

        CompletableFuture<List<Integer>> completableFuture = CompletableFuture.supplyAsync(() -> {
            List<Integer> list = new ArrayList<>();
            System.out.println("Code Executed by thread: " + Thread.currentThread().getName());
            for(int i = 1 ; i < 101; i++) {
                list.add(i);
            }
            return list;
        }).thenComposeAsync( list -> CompletableFuture.supplyAsync(() -> {
            System.out.println("Code Executed by thread: " + Thread.currentThread().getName());
                for(int i = 101 ; i < 201; i++) {
                    list.add(i);
                }
                return list;
        }));

        try {
            System.out.println(completableFuture.get());
        } catch (Exception e) {

        }
    }
}
