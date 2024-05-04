package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.MarketData;

@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, Long> {

	MarketData findByTimestampAndInstrumentToken(String timestamp, String instrumentKey);	

	List<MarketData> findByTimestampAfter(String timestamp);
}
