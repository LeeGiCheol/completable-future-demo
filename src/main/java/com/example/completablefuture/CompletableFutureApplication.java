package com.example.completablefuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;


@SpringBootApplication
public class CompletableFutureApplication {


    @RestController
    public static class CompletableFutureDemo {

        AsyncRestTemplate rt = new AsyncRestTemplate();

        @Autowired MyService myService;

        static final String URL1 = "http://localhost:8080/service?req={req}";
        static final String URL2 = "http://localhost:8080/service2?req={req}";

        @GetMapping("/rest")
        public DeferredResult<String> rest(int idx) {
            DeferredResult<String> dr = new DeferredResult<>();

            toCF(rt.getForEntity(URL1, String.class, "h" + idx))
                    .thenCompose(s -> toCF(rt.getForEntity(URL2, String.class, s.getBody())))
                    .thenCompose(s2 -> toCF(myService.work(s2.getBody())))
                    .thenAccept(s3 -> dr.setResult(s3))
                    .exceptionally(e -> {
                        dr.setErrorResult(e.getMessage());
                        return (Void) null;
                    })
            ;







            return dr;
        }

        <T> CompletableFuture<T> toCF(ListenableFuture<T> lf) {
            CompletableFuture<T> cf = new CompletableFuture<>();
            lf.addCallback(cf::complete, cf::completeExceptionally);
            return cf;
        }

    }


    @Service
    public static class MyService {
        @Async
        public ListenableFuture<String> work(String req) {
            return new AsyncResult<>(req + "/asyncwork");
        }
    }

    @Bean
    public ThreadPoolTaskExecutor myThreadPool() {
        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
        te.setCorePoolSize(1);
        te.setMaxPoolSize(1);
        te.initialize();
        return te;
    }


    public static void main(String[] args) {
        SpringApplication.run(CompletableFutureApplication.class, args);
    }

}
