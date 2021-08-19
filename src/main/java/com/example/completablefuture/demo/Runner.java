package com.example.completablefuture.demo;

import com.example.completablefuture.demo.domain.Event;
import com.example.completablefuture.demo.domain.ReturnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class Runner implements ApplicationRunner {

    @Autowired
    private RestTemplate restTemplate;

    final String URL1 = "http://localhost:8080/rest-api-1";
    final String URL2 = "http://localhost:8080/rest-api-2";


    @Override
    public void run(ApplicationArguments args) throws Exception {
        defaultRestTemplateExample();
        completableFutureRestTemplateExample();
        completableFutureAllOfRestTemplateExample();
    }

    private void completableFutureAllOfRestTemplateExample() {
        ReturnEvent returnEvent = new ReturnEvent();

        Event event1 = getEvent("LEE", "GICHEOL");
        Event event2 = getEvent("CHEEOLEE", "NICKNAME");

        long api_one_start = System.currentTimeMillis();

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> apiCall(returnEvent, URL1, event1));
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> apiCall(returnEvent, URL2, event2));
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> apiCall(returnEvent, URL1, event1));

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(future1, future2, future3);

        try {
            voidCompletableFuture.get();

            log.info("[CF ALL OF FINALLY returnEvent : {}]", returnEvent);
            log.info("[CF ALL OF API CALL TIME : {}]", System.currentTimeMillis() - api_one_start);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        
    }


    private void completableFutureRestTemplateExample() {
        ReturnEvent returnEvent = new ReturnEvent();

        Event event1 = getEvent("LEE", "GICHEOL");
        Event event2 = getEvent("CHEEOLEE", "NICKNAME");

        long api_one_start = System.currentTimeMillis();

        CompletableFuture<ReturnEvent> future1 = CompletableFuture.supplyAsync(() -> apiCall(URL1, event1));
        CompletableFuture<ReturnEvent> future2 = CompletableFuture.supplyAsync(() -> apiCall(URL1, event1));
        CompletableFuture<ReturnEvent> future3 = CompletableFuture.supplyAsync(() -> apiCall(URL2, event2));


        ReturnEvent result1 = future1.join();
        ReturnEvent result2 = future2.join();
        ReturnEvent result3 = future3.join();

        returnEvent.listAllAdd(result1);
        returnEvent.listAllAdd(result2);
        returnEvent.listAllAdd(result3);


        log.info("[CF FINALLY returnEvent : {}]", returnEvent);
        log.info("[CF API CALL TIME : {}]", System.currentTimeMillis() - api_one_start);
    }


    private void defaultRestTemplateExample() {
        Event event1 = getEvent("LEE", "GICHEOL");
        Event event2 = getEvent("CHEEOLEE", "NICKNAME");

        long api_one_start = System.currentTimeMillis();

        ReturnEvent returnEvent1 = apiCall(URL1, event1);
        String api1Result = returnEvent1.getNameTitle().get(0);

        log.info("[DEFAULT API 1 CALL TIME : {}]", System.currentTimeMillis() - api_one_start);

        long api_two_start = System.currentTimeMillis();

        ReturnEvent returnEvent2 = apiCall(URL2, event2);

        String api2Result = "";
        if (returnEvent2.getNameTitle().size() != 0) {
            api2Result = returnEvent2.getNameTitle().get(0);
            log.info("[DEFAULT API 2 CALL TIME : {}]", System.currentTimeMillis() - api_two_start);
        }

        long api_three_start = System.currentTimeMillis();

        ReturnEvent returnEvent3 = apiCall(URL1, event1);
        String api3Result = returnEvent3.getNameTitle().get(0);

        log.info("[DEFAULT API 3 CALL TIME : {}]", System.currentTimeMillis() - api_three_start);
        log.info("[DEFAULT ALL API CALL TIME : {}]", System.currentTimeMillis() - api_one_start);

        log.info("[DEFAULT FINALLY returnEvent : {}]", api1Result + " , " + api2Result + ", " + api3Result);

    }

    private void apiCall(ReturnEvent returnEvent, String URL, Event event) {
        try {
            ResponseEntity<ReturnEvent> apiResult = restTemplate.postForEntity(URL, event, ReturnEvent.class);
            ReturnEvent body = apiResult.getBody();

            returnEvent.listAllAdd(body);
        } catch (Exception e) {

        }

    }

    private ReturnEvent apiCall(String URL, Event event) {
        try {
            ResponseEntity<ReturnEvent> apiResult = restTemplate.postForEntity(URL, event, ReturnEvent.class);
            return apiResult.getBody();
        } catch (Exception e) {
            return new ReturnEvent();
        }
    }

    private Event getEvent(String name, String title) {
        return new Event(name, title);
    }

}
