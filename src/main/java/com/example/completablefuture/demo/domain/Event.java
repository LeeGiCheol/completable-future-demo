package com.example.completablefuture.demo.domain;

public class Event {

    public Event(String name, String title) {
        this.name = name;
        this.title = title;
    }

    private String name;

    private String title;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
