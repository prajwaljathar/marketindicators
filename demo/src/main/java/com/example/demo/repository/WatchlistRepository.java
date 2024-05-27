package com.example.demo.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.model.WatchlistEntity;

@Repository
public interface WatchlistRepository extends JpaRepository<WatchlistEntity, String> {

	//Optional<WatchlistEntity> findById(String instrumentKey);
	Optional<WatchlistEntity> findByInstrumentKey(String instrumentKey);
	
//    @Query("SELECT w FROM WatchlistEntity w WHERE LOWER(w.trade) = LOWER(:tradeValue)")
//    List<WatchlistEntity> findByTradeIgnoreCase(@Param("tradeValue") String tradeValue);
    
    @Query("SELECT w FROM WatchlistEntity w " +
            "WHERE LOWER(w.trade) IN ('yes5', 'yes20') " +
            "ORDER BY " +
            "CASE " +
            "WHEN LOWER(w.trade) = 'yes20' THEN 1 " +
            "WHEN LOWER(w.trade) = 'yes5' THEN 2 " +
            "END")
     List<WatchlistEntity> findByTradeYes5OrYes20();
}


