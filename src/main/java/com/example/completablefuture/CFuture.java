package com.example.completablefuture;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class CFuture {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(10);
        CompletableFuture
                .supplyAsync(() -> {
                    log.info("runAsync");
                    return 1;
                }, es)
                .thenCompose(s -> {
                    log.info("thenRun {}", s);
                    return CompletableFuture.completedFuture(s + 1);
                })
                .thenApplyAsync(s -> {
                    log.info("thenRun {}", s);
                    return s + 1;
                }, es)
                 .exceptionally(e -> -10)
                .thenAcceptAsync(s2 -> log.info("thenRun {}", s2), es)
         ;

        log.info("exit");

        ForkJoinPool.commonPool().shutdown();
        ForkJoinPool.commonPool().awaitTermination(10, TimeUnit.SECONDS);
    }

}
