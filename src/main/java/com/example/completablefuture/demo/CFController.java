package com.example.completablefuture.demo;

import com.example.completablefuture.demo.domain.Event;
import com.example.completablefuture.demo.domain.ReturnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class CFController {

    @PostMapping("/rest-api-1")
    public ResponseEntity<?> restApi_1(@RequestBody Event event) throws InterruptedException {
        log.info("restApi_1 in");
        Thread.sleep(3000L);

        ReturnEvent returnEvent = new ReturnEvent(List.of(event.getName() + " " + event.getTitle()));

        log.info("restApi_1 event : " + returnEvent);

        return ResponseEntity.ok().body(returnEvent);
    }

    @PostMapping("/rest-api-2")
    public ResponseEntity<?> restApi_2(@RequestBody Event event) throws InterruptedException {
        log.info("restApi_2 in");

        Thread.sleep(1000L);

        ReturnEvent returnEvent = new ReturnEvent(List.of(event.getName() + " " + event.getTitle()));

        log.info("restApi_2 event : " + returnEvent);

        return ResponseEntity.badRequest().build();
    }

}
