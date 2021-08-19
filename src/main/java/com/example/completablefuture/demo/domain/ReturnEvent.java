package com.example.completablefuture.demo.domain;

import java.util.ArrayList;
import java.util.List;

public class ReturnEvent {

    public ReturnEvent() {}
    public ReturnEvent(List<String> nameTitle) {
        this.nameTitle = nameTitle;
    }


    private List<String> nameTitle = new ArrayList<>();


    public void listAllAdd(ReturnEvent returnEvent) {
        this.nameTitle.addAll(returnEvent.getNameTitle());
    }



    public List<String> getNameTitle() {
        return nameTitle;
    }

    public void setNameTitle(List<String> nameTitle) {
        this.nameTitle = nameTitle;
    }


    @Override
    public String toString() {
        return "ReturnEvent{" +
                "nameTitle=" + nameTitle +
                '}';
    }
}
