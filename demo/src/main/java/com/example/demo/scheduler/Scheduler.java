package com.example.demo.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.contoller.PlaceOrderControllerRealTime;

import java.io.IOException;

@Component
public class Scheduler {

    @Autowired
    private PlaceOrderControllerRealTime placeOrderControllerRealTime;

    //@Scheduled(cron = "0 0 9 * * ?", zone = "Asia/Kolkata")  
    //@Scheduled(cron = "0 15 9 * * ?", zone = "Asia/Kolkata")
    @Scheduled(cron = "0 30 11 * * ?", zone = "Asia/Kolkata")
    public void schedulePlaceOrderRealTimeUpdate() {
        try {
            placeOrderControllerRealTime.getPlaceOrderRealTimeUpdate();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}