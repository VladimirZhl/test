package org.example;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final AtomicInteger counterA = new AtomicInteger(1000);
    private static final AtomicInteger counterB = new AtomicInteger(1000);
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<?>> futureMList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Future<?> future = executor.submit(() -> {
            Random random = new Random();
            int randomInt = random.nextInt(100) + 1;
                lock.lock();
                try {
                    if (counterB.get() >= randomInt) {
                        counterA.addAndGet(randomInt);
                        counterB.addAndGet(-randomInt);
                    }
                } finally {
                    lock.unlock();
                }
            });
            futureMList.add(future);
        }

        for(Future<?> future : futureMList) {
           try{
               future.get();}
           catch (InterruptedException | ExecutionException e)
           {e.printStackTrace();
           }
        }
        executor.shutdown();
        System.out.println(counterA.get() + " "+ counterB.get());
        // Это новая ветка и отдельный  1 коммит
        //Изменениея в мастере
        // Это новая ветка и отдельный  2 коммит
        //КОммит в мастере последний

        //Изменения для REBASE

//        ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(
//                5,
//                r -> {
//                    Thread t = new Thread(r);
//                    t.setName("custom-scheduler-bla-" + t.getId());
//                    return t;
//                }
//        );
//
//       List<Future<String>> futures = new ArrayList<>();
//
//       for (int i = 0; i < 5; i++) {
//           int taskId = i;
//           Future<String>future = executor.submit(()-> {
//               return "Task #" + taskId +" "+ Thread.currentThread().getName();
//           });
//           futures.add(future);
//       }
//       for (Future<String> future : futures) {
//           System.out.println(future.get());
//       }
//        executor.shutdown();
//        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "Hello");
//        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "World");
//        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> "Hello").thenApply(String::toUpperCase);
//
//        CompletableFuture<String> combined = f1.thenCombine(f2, (a, b) -> a + " " + b).thenCombine(f3, (a, b) -> a + " " + b);
//        System.out.println(combined.join());
//
//        CompletableFuture f4 = CompletableFuture.supplyAsync(() -> "Hello");
    }
}