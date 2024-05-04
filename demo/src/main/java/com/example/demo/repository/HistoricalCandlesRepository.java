package com.example.demo.repository;

import com.example.demo.model.HistoricalCandles;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HistoricalCandlesRepository extends JpaRepository<HistoricalCandles, Long> {

	
    // Add custom query methods if needed

	HistoricalCandles findByInstrumentKeyAndCandleTime(String instrumentToken, LocalDateTime marketDataDateTime);

    @Query("SELECT MAX(c.candleTime) FROM HistoricalCandles c")
    LocalDateTime findHighestDate();

    HistoricalCandles findByCandleTime(LocalDateTime candleTime); // Modify method name and parameter type

	
}
