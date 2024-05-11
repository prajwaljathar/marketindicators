package com.example.demo.contoller;


import com.example.demo.model.WatchlistEntity;
import com.example.demo.serviceimpl.WatchlistServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/watchlist")
public class WatchlistController {

    @Autowired
    private WatchlistServiceImpl watchlistServiceImpl;

    @GetMapping("/create")
    public String createAndSaveWatchlist() {
    	watchlistServiceImpl.createAndSaveWatchlist();
        return "Watchlist created and saved successfully!";
    }
}