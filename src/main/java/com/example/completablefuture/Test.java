package com.example.completablefuture;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
public class Test {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            log.info("future-1");
            return "1";
        });

        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            log.info("future-2");
            return "2";
        });

        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> {
            log.info("future-3");
            return "3";
        });

        List<CompletableFuture<String>> futures = Arrays.asList(f1, f2, f3);
        CompletableFuture<List<String>> listCompletableFuture = CompletableFuture.allOf(f1, f2, f3)
                .thenApply(s -> {
                    List<String> result = futures.stream()
                            .map(pageContentFuture -> pageContentFuture.join())
                            .collect(Collectors.toList());
                    log.info(result.toString());
                    return result;
                });


        List<String> x = listCompletableFuture.get();
        System.out.println(x);


    }

}
